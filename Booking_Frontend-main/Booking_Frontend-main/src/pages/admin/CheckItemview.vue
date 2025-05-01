<template>
  <v-container class="admin-container">
    <!-- Dashboard Header -->
    <v-card flat class="header-card mb-6">
      <v-card-title class="text-h4 font-weight-light">
        <v-icon color="primary" size="36" class="mr-3">mdi-shield-check</v-icon>
        Product Approval
      </v-card-title>
      <v-card-subtitle class="text-body-1">
        Review product submission
      </v-card-subtitle>
    </v-card>

    <!-- Product Details -->
    <v-row v-if="product">
      <v-col cols="12">
        <v-card class="product-card" elevation="2">
          <v-row no-gutters>
            <!-- Left Column - Media -->
            <v-col cols="12" md="5">
              <!-- Image -->
              <div class="image-gallery">
                <v-carousel height="300" hide-delimiters>
                  <v-carousel-item
                    v-for="(image, i) in product.files"
                    :key="i"
                    :src="image.fileUrl"
                    cover
                  ></v-carousel-item>
                </v-carousel>
              </div>

              <!-- Verification Document -->
              <div class="files-section pa-4" v-if="product.verificationDocument">
                <h3 class="text-h6 mb-3">Verification Document</h3>
                <div class="file-list">
                  <v-chip
                    class="ma-1"
                    color="blue-lighten-1"
                    @click="viewFile(product.verificationDocument)"
                  >
                    <v-icon left>mdi-file-document</v-icon>
                    Verification Document
                  </v-chip>
                </div>
              </div>
            </v-col>

            <!-- Right Column - Details -->
            <v-col cols="12" md="7">
              <v-card-text class="pa-6">
                <!-- Product Header -->
                <div class="d-flex justify-space-between align-start mb-4">
                  <div>
                    <h2 class="text-h5 mb-1">{{ product.name }}</h2>
                    <v-chip-group class="mt-2">
                      <v-chip size="small" color="primary" variant="outlined">
                        {{ product.pricePerDay.toLocaleString() }} тг/day
                      </v-chip>
                      <v-chip size="small" variant="outlined">
                        {{ product.categoryMap }}
                      </v-chip>
                      <v-chip size="small" :color="product.available ? 'success' : 'error'">
                        {{ product.available ? 'Available' : 'Not Available' }}
                      </v-chip>
                    </v-chip-group>
                  </div>
                </div>

                <!-- Description -->
                <p class="text-body-1 mb-6">{{ product.description }}</p>

                <!-- Specifications -->
                <v-expansion-panels flat class="mb-6">
                  <v-expansion-panel>
                    <v-expansion-panel-title>
                      <v-icon size="small" class="mr-2">mdi-cog</v-icon>
                      Product Specifications
                    </v-expansion-panel-title>
                    <v-expansion-panel-text>
                      <v-table density="compact">
                        <tbody>
                          <tr v-for="(value, key) in parsedAttributes" :key="key">
                            <td class="text-capitalize font-weight-medium">{{ key }}</td>
                            <td class="text-end">{{ value }}</td>
                          </tr>
                        </tbody>
                      </v-table>
                    </v-expansion-panel-text>
                  </v-expansion-panel>
                </v-expansion-panels>

                <v-card-actions class="px-0">
                  <v-btn
                    color="success"
                    variant="flat"
                    size="large"
                    @click="approveProduct"
                    class="action-btn"
                  >
                    <template v-slot:prepend>
                      <v-icon>mdi-check</v-icon>
                    </template>
                    Approve
                  </v-btn>

                  <v-btn
                    color="error"
                    variant="outlined"
                    size="large"
                    @click="rejectProduct"
                    class="action-btn"
                  >
                    <template v-slot:prepend>
                      <v-icon>mdi-close</v-icon>
                    </template>
                    Reject
                  </v-btn>
                </v-card-actions>
              </v-card-text>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>


    <v-row v-else>
      <v-col cols="12" class="text-center">
        <v-progress-circular indeterminate size="64"></v-progress-circular>
      </v-col>
    </v-row>

    <!-- Success Notification -->
    <v-snackbar v-model="showSuccess" color="success" location="top right" :timeout="3000">
      <div class="d-flex align-center">
        <v-icon class="mr-3">mdi-check-circle</v-icon>
        <div class="text-subtitle-1">{{ successMessage }}</div>
      </div>
    </v-snackbar>

    <v-dialog v-model="approveDialog" max-width="500">
  <v-card>
    <v-card-title class="d-flex align-center">
      <v-icon color="success" size="32" class="mr-3">mdi-check-circle</v-icon>
      <span class="text-h5">Confirm Approval</span>
    </v-card-title>

    <v-card-text class="text-body-1 pa-4">
      Are you sure you want to approve this product?
      <span class="font-weight-medium">"{{ product?.name }}"</span> will be published and made available to users.
    </v-card-text>

    <v-card-actions class="pa-4">
      <v-spacer></v-spacer>
      <v-btn variant="text" @click="approveDialog = false">Cancel</v-btn>
      <v-btn
        color="success"
        variant="flat"
        @click="confirmApprove"
        :loading="approving"
      >
        Confirm Approval
      </v-btn>
    </v-card-actions>
  </v-card>
</v-dialog>

<!-- Rejection Confirmation Dialog -->
<v-dialog v-model="rejectDialog" max-width="500">
  <v-card>
    <v-card-title class="d-flex align-center">
      <v-icon color="error" size="32" class="mr-3">mdi-alert-circle</v-icon>
      <span class="text-h5">Confirm Rejection</span>
    </v-card-title>

    <v-card-text class="text-body-1 pa-4">
      <div class="mb-4">
        Are you sure you want to reject this product?
        <span class="font-weight-medium">"{{ product?.name }}"</span> will be moved to rejected items.
      </div>

      <v-textarea
        v-model="rejectionReason"
        label="Reason for rejection (optional)"
        variant="outlined"
        rows="3"
        hide-details
        class="mt-2"
      ></v-textarea>
    </v-card-text>

    <v-card-actions class="pa-4">
      <v-spacer></v-spacer>
      <v-btn variant="text" @click="rejectDialog = false">Cancel</v-btn>
      <v-btn
        color="error"
        variant="flat"
        @click="confirmReject"
        :loading="rejecting"
      >
        Confirm Rejection
      </v-btn>
    </v-card-actions>
  </v-card>
</v-dialog>
  </v-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

import axios from "@/axios.js";

const route = useRoute()
const router = useRouter()
const product = ref(null)
const loading = ref(false)
const approving = ref(false)
const rejecting = ref(false)
const showSuccess = ref(false)
const successMessage = ref('')

const approveDialog = ref(false)
const rejectDialog = ref(false)
const rejectionReason = ref('')

const approveProduct = () => {
  approveDialog.value = true
}

const rejectProduct = () => {
  rejectDialog.value = true
}


const parsedAttributes = computed(() => {
  if (!product.value?.attributes) return {}
  try {
    return JSON.parse(product.value.attributes)
  } catch (e) {
    return {}
  }
})

const fetchProduct = async () => {
  try {
    loading.value = true
    const token = localStorage.getItem('authToken');
    if (!token) throw new Error('Authentication required');
    const response = await axios.get(`/api/items/${route.params.id}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })




    product.value = response.data.body
  } catch (error) {
    console.error('Error fetching product details:', error)
  } finally {
    loading.value = false
  }
}


const confirmApprove = async () => {
  try {
    approving.value = true
    const token = localStorage.getItem('authToken')
    if (!token) throw new Error('Authentication required');

    const response = await axios.post(
      `/api/items/approve/${route.params.id}`,
      {
        page: 0,
        size: 20,
        name: "",
        categoryId: null,
        filterAttributes: []
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      }
    );


    successMessage.value = 'Product approved successfully'
    showSuccess.value = true
    approveDialog.value = false

    setTimeout(() => {
      router.push('/admin-main-view')
    }, 1500)
  } catch (error) {
    console.error('Error approving product:', error)
  } finally {
    approving.value = false
  }
}

const confirmReject = async () => {
  try {
    rejecting.value = true
    const token = localStorage.getItem('authToken');
    if (!token) throw new Error('Authentication required');

    const response = await axios.post(
      `/api/items/reject/${route.params.id}`,
      {
        page: 0,
        size: 20,
        name: "",
        categoryId: null,
        filterAttributes: [],
        rejectionReason: rejectionReason.value
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      }
    );



    successMessage.value = 'Product rejected successfully'
    showSuccess.value = true
    rejectDialog.value = false

    setTimeout(() => {
      router.push('/admin-main-view')
    }, 1500)
  } catch (error) {
    console.error('Error rejecting product:', error)
  } finally {
    rejecting.value = false
  }
}

const viewFile = (fileUrl) => {
  window.open(fileUrl, '_blank')
}

onMounted(() => {
  fetchProduct()
})
</script>

<style scoped>
.admin-container {
  max-width: 1400px;
}

.header-card {
  background: transparent;
}

.product-card {
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 24px;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.product-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(0,0,0,0.1) !important;
}

.image-gallery {
  border-bottom: 1px solid rgba(0,0,0,0.1);
}

.files-section {
  border-top: 1px solid rgba(0,0,0,0.1);
}

.file-list {
  display: flex;
  flex-wrap: wrap;
}

.action-btn {
  min-width: 160px;
}

.v-card-actions {
  border-top: 1px solid rgba(0,0,0,0.1);
  padding-top: 16px;
}
</style>
