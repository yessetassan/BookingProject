<template>
  <Header/>
  <v-container class="admin-container">
    
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
            
            <v-col cols="12" md="5">
              
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

           
            <v-col cols="12" md="7">
              <v-card-text class="pa-6">
               
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
                  v-if="showPayButton"
                    color="success"
                    variant="flat"
                    size="large"
                    @click="navigatePayPage()"
                    class="action-btn"
                  >
                    <!-- <template v-slot:prepend>
                      <v-icon>mdi-check</v-icon>
                    </template> -->
                    Pay with card
                  </v-btn>


                  <v-btn
                  v-if="showBookingHistoryButton"
                    color="primary"
                    variant="flat"
                    size="large"
                    @click="navigateBookingHistoryPage()"
                    class="action-btn"
                  >
                    <!-- <template v-slot:prepend>
                      <v-icon>mdi-check</v-icon>
                    </template> -->
                    Booking History
                  </v-btn>

                </v-card-actions>
              </v-card-text>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>

    <!-- Loading State -->
    <v-row v-else>
      <v-col cols="12" class="text-center">
        <v-progress-circular indeterminate size="64"></v-progress-circular>
      </v-col>
    </v-row>



  </v-container>
</template>

<script setup>
import Header from '../components/Header.vue';

import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const product = ref(null)
const loading = ref(false)



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
    const token = localStorage.getItem('authToken')
    const response = await fetch(`http://localhost:8080/api/items/${route.params.id}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })

    if (!response.ok) {
      throw new Error('Failed to fetch product details')
    }

    const data = await response.json()
    product.value = data.body
  } catch (error) {
    console.error('Error fetching product details:', error)
  } finally {
    loading.value = false
  }
}


const viewFile = (fileUrl) => {
  window.open(fileUrl, '_blank')
}

onMounted(() => {
  fetchProduct()
})

const showPayButton = computed(() => {
  return route.query.from === 'approved-my-items'
})

const navigatePayPage = () => {
  router.push({
    path: '/payment',
    query: {itemId: route.params.id}
  })
}

const showBookingHistoryButton = computed(() => {
  return route.query.from === 'approved-my-items'
})
const navigateBookingHistoryPage = () => {
  router.push({
    path: '/owner-booking-history',
    query: { itemId: route.params.id }
  });
}
</script>

<style scoped>
.admin-container {
  max-width: 1400px;
  margin-top: 60px;
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
