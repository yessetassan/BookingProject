<template>
  <Header/>
  <div class="payment-history-container">

    <div class="history-header">
      <div class="header-content">
        <div class="header-icon">
          <svg width="32" height="32" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M16 28C22.6274 28 28 22.6274 28 16C28 9.37258 22.6274 4 16 4C9.37258 4 4 9.37258 4 16C4 22.6274 9.37258 28 16 28Z" fill="#4E60FF" fill-opacity="0.1"/>
            <path d="M16 24C20.4183 24 24 20.4183 24 16C24 11.5817 20.4183 8 16 8C11.5817 8 8 11.5817 8 16C8 20.4183 11.5817 24 16 24Z" fill="#4E60FF" fill-opacity="0.2"/>
            <path d="M16 20C18.2091 20 20 18.2091 20 16C20 13.7909 18.2091 12 16 12C13.7909 12 12 13.7909 12 16C12 18.2091 13.7909 20 16 20Z" fill="#4E60FF"/>
            <path d="M16 10V6M16 26V22M22 16H26M6 16H10" stroke="#4E60FF" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div>
          <h1 class="payment-title">Your Rental History</h1>
          <p class="payment-subtitle">Manage and view your active rentals</p>
        </div>
      </div>
      <v-btn
        color="#4E60FF"
        outlined
        @click="$router.go(-1)"
        class="back-btn"
      >
        <v-icon left>mdi-arrow-left</v-icon>
        Back
      </v-btn>
    </div>

    <v-divider class="divider"></v-divider>

    <!-- Main Content -->
    <div class="history-content">

      <div v-if="loading" class="loading-state">
        <v-progress-circular
          indeterminate
          color="#4E60FF"
          size="64"
        ></v-progress-circular>
        <p>Loading your rentals...</p>
      </div>

      <div v-else-if="payments.length === 0" class="empty-state">
        <div class="empty-icon">
          <svg width="160" height="160" viewBox="0 0 160 160" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="80" cy="80" r="80" fill="#F8F9FF"/>
            <path d="M80 40V60M80 120V140M120 80H140M60 80H40" stroke="#C7C8D2" stroke-width="2" stroke-linecap="round"/>
            <path d="M80 60C87.732 60 94 66.268 94 74C94 81.732 87.732 88 80 88C72.268 88 66 81.732 66 74C66 66.268 72.268 60 80 60Z" stroke="#C7C8D2" stroke-width="2"/>
            <path d="M80 100C87.732 100 94 106.268 94 114C94 121.732 87.732 128 80 128C72.268 128 66 121.732 66 114C66 106.268 72.268 100 80 100Z" stroke="#C7C8D2" stroke-width="2"/>
          </svg>
        </div>
        <h3>No active rentals found</h3>
        <p>Your active rentals will appear here</p>
        <v-btn
          color="#4E60FF"
          depressed
          @click="$router.push('/')"
          class="empty-btn"
        >
          Browse Rentals
        </v-btn>
      </div>

      <!-- Payment List -->
      <div v-else>
        <div class="payment-grid" >
          <div
            v-for="payment in payments"
            :key="payment.id"
            class="payment-card"
          >
            <div class="payment-card-header">
              <div class="payment-status">
                <div class="status-badge" :class="payment.status.toLowerCase()">
                  <v-icon small>{{ payment.status === 'ACTIVE' ? 'mdi-check-circle' : 'mdi-close-circle' }}</v-icon>
                  <span>{{ payment.status === 'ACTIVE' ? 'Active' : 'Inactive' }}</span>
                </div>
                <div class="payment-id">
                  <span>Booking #{{ payment.id }}</span>
                  <v-btn icon x-small @click="copyToClipboard(payment.id)" class="copy-btn">
                    <v-icon x-small>mdi-content-copy</v-icon>
                  </v-btn>
                </div>
              </div>
              <div class="payment-dates">
                <div class="date-range">
                  <v-icon small color="#83859C">mdi-calendar</v-icon>
                  <span>{{ formatDate(payment.viewFrom) }} - {{ formatDate(payment.viewTill) }}</span>
                </div>
                <!-- <div class="duration">
                  {{ calculateRentalDays(payment.viewFrom, payment.viewTill) }} days
                </div> -->
              </div>
            </div>

            <div class="payment-card-body">
              <div class="vehicle-info">
                <div class="vehicle-image">
                  <svg width="64" height="64" viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <rect width="64" height="64" rx="8" fill="#F5F6FA"/>
                    <path d="M48 40H16C14.8954 40 14 39.1046 14 38V26C14 24.8954 14.8954 24 16 24H48C49.1046 24 50 24.8954 50 26V38C50 39.1046 49.1046 40 48 40Z" fill="#4E60FF" fill-opacity="0.2" stroke="#4E60FF" stroke-width="2" stroke-linejoin="round"/>
                    <path d="M40 40V44C40 45.1046 39.1046 46 38 46H36C34.8954 46 34 45.1046 34 44V40" stroke="#4E60FF" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M24 40V44C24 45.1046 24.8954 46 26 46H28C29.1046 46 30 45.1046 30 44V40" stroke="#4E60FF" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M18 32H22" stroke="#4E60FF" stroke-width="2" stroke-linecap="round"/>
                    <path d="M42 32H46" stroke="#4E60FF" stroke-width="2" stroke-linecap="round"/>
                    <path d="M30 24L26 16" stroke="#4E60FF" stroke-width="2" stroke-linecap="round"/>
                    <path d="M34 24L38 16" stroke="#4E60FF" stroke-width="2" stroke-linecap="round"/>
                  </svg>
                </div>
                <div class="vehicle-details">
                  <h3>{{ payment.item.name }}</h3>
                  <div class="vehicle-meta">
                    <div class="meta-item">
                      <v-icon small color="#83859C">mdi-car</v-icon>
                      <span>{{ getCarModel(payment.item.attributes) }}</span>
                    </div>
                    <div class="meta-item">
                      <v-icon small color="#83859C">mdi-cash</v-icon>
                      <span>{{ formatCurrency(parseFloat(payment.item.pricePerDay)) }}/day</span>
                    </div>
                  </div>
                </div>
              </div>

              <div class="payment-actions">
                <v-btn
                  text
                  color="#4E60FF"
                  @click="viewDetails(payment)"
                  class="details-btn"
                >
                  <v-icon left small>mdi-eye</v-icon>
                  View Details
                </v-btn>

                <v-btn
                  v-if="payment.status === 'ACTIVE' && !isLateCancellation(payment)"
                  outlined
                  color="#FF5C60"
                  @click="initiateCancellation(payment)"
                  class="cancel-btn"
                >
                  <v-icon left small>mdi-close</v-icon>
                  Cancel Rental
                </v-btn>
              </div>
            </div>
          </div>
        </div>

        <!-- Pagination -->
        <div class="pagination-container" v-if="totalPages > 1">
          <span
            style="border-left: 0.7px #9e9e9e solid;"
            :class="['pagination-item', { 'disabled': currentPage === 0 }]"
            @click="currentPage > 0 && handlePageChange(currentPage - 1)"
          >
            ← Previous
          </span>
          <span
            v-for="page in visiblePages"
            :key="page"
            :class="['pagination-item', { 'active': currentPage === page }]"
            @click="handlePageChange(page)"
          >
            {{ page + 1 }}
          </span>
          <span
            :class="['pagination-item', { 'disabled': currentPage >= totalPages - 1 }]"
            @click="currentPage < totalPages - 1 && handlePageChange(currentPage + 1)"
          >
            Next →
          </span>
        </div>
      </div>
    </div>

    <!-- Cancellation Dialog -->
    <v-dialog v-model="showCancelDialog" max-width="500" persistent>
      <v-card class="cancel-dialog">
        <v-card-title class="dialog-title">
          <div class="dialog-icon error">
            <v-icon>mdi-alert-circle</v-icon>
          </div>
          Cancel Rental
        </v-card-title>

        <v-card-text class="dialog-text">
          <p>Are you sure you want to cancel your rental for <strong>{{ selectedPayment?.item.name }}</strong>?</p>

          <div class="rental-details">
            <div class="detail-item">
              <v-icon small color="#83859C">mdi-calendar</v-icon>
              <span>{{ formatDate(selectedPayment?.viewFrom) }} - {{ formatDate(selectedPayment?.viewTill) }}</span>
            </div>
            <div class="detail-item">
              <v-icon small color="#83859C">mdi-clock</v-icon>
              <span>{{ calculateRentalDays(selectedPayment?.viewFrom, selectedPayment?.viewTill) }} days</span>
            </div>
          </div>

          <v-textarea
            v-model="cancellationReason"
            label="Reason for cancellation (optional)"
            outlined
            class="mt-4"
            rows="3"
            placeholder="Please let us know why you're cancelling..."
          ></v-textarea>
        </v-card-text>

        <v-card-actions class="dialog-actions">
          <v-btn
            text
            @click="showCancelDialog = false"
            class="cancel-action"
          >
            Go Back
          </v-btn>

          <v-btn
            color="#FF5C60"
            depressed
            @click="confirmCancellation"
            class="confirm-cancel"
            :loading="cancelling"
          >
            Confirm Cancellation
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Details Dialog -->
    <v-dialog v-model="showDetailsDialog" max-width="600" persistent>
      <v-card class="details-dialog">
        <v-card-title class="dialog-title">
          <div class="dialog-icon">
            <v-icon>mdi-information</v-icon>
          </div>
          Rental Details
        </v-card-title>

        <v-card-text class="dialog-text">
          <div class="details-grid">
            <div class="detail-section">
              <h4>Vehicle Information</h4>
              <div class="detail-row">
                <span class="detail-label">Vehicle:</span>
                <span class="detail-value">{{ selectedPayment?.item.name }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">Model:</span>
                <span class="detail-value">{{ getCarModel(selectedPayment?.item.attributes) }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">Daily Rate:</span>
                <span class="detail-value">{{ formatCurrency(parseFloat(selectedPayment?.item.pricePerDay)) }}</span>
              </div>
              <div class="detail-row" v-for="(value, key) in parsedAttributes" :key="key">
                <span class="detail-label">{{ key }}:</span>
                <span class="detail-value">{{ value }}</span>
              </div>
            </div>

            <div class="detail-section">
              <h4>Rental Period</h4>
              <div class="detail-row">
                <span class="detail-label">Start Date:</span>
                <span class="detail-value">{{ formatDate(selectedPayment?.viewFrom) }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">End Date:</span>
                <span class="detail-value">{{ formatDate(selectedPayment?.viewTill) }}</span>
              </div>
              <!-- <div class="detail-row">
                <span class="detail-label">Duration:</span>
                <span class="detail-value">{{ calculateRentalDays(selectedPayment?.viewFrom, selectedPayment?.viewTill) }} days</span>
              </div> -->

            </div>

            <div class="detail-section">
              <h4>Booking Information</h4>
              <div class="detail-row">
                <span class="detail-label">Booking ID:</span>
                <span class="detail-value">#{{ selectedPayment?.id }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">Status:</span>
                <span class="detail-value status-badge" :class="selectedPayment.item?.status.toLowerCase()">
                  {{ selectedPayment.item?.status === 'ACTIVE' ? 'Active' : 'Inactive' }}
                </span>
              </div>
            </div>
          </div>
        </v-card-text>

        <v-card-actions class="dialog-actions">
          <v-btn
            color="#4E60FF"
            depressed
            @click="showDetailsDialog = false"
            block
          >
            Close
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import Header from '@/components/Header.vue';

import axios from 'axios'
import { useToast } from 'vue-toast-notification'
import 'vue-toast-notification/dist/theme-sugar.css'




export default {
  data() {
    return {
      loading: true,
      payments: [],
      showCancelDialog: false,
      showDetailsDialog: false,
      selectedPayment: null,
      cancellationReason: '',
      cancelling: false,
      currentPage: parseInt(localStorage.getItem('paymentHistoryPage')) || 0,
      itemsPerPage: 2,
      totalPages: 1,
      totalElements: 0,

    }
  },
  computed: {
    parsedAttributes() {
    try {
      return this.selectedPayment && this.selectedPayment.item.attributes
        ? JSON.parse(this.selectedPayment.item.attributes)
        : {};
    } catch (e) {
      return {};
    }
  },
    visiblePages() {
      if (this.totalPages <= 1) return [];

      const pages = [];
      const maxVisible = 5;
      let start = Math.max(this.currentPage - Math.floor(maxVisible / 2), 0);
      let end = Math.min(start + maxVisible - 1, this.totalPages - 1);

      if (end - start + 1 < maxVisible) {
        start = Math.max(end - maxVisible + 1, 0);
      }

      for (let i = start; i <= end; i++) {
        pages.push(i);
      }

      return pages;
    }
  },
  created() {
    const savedPage = localStorage.getItem('paymentHistoryPage')
    if(savedPage) {
      this.currentPage = parseInt(savedPage)
    }
    this.fetchPaymentHistory()
  },
  methods: {
    async fetchPaymentHistory() {
      try {
        this.loading = true
        const token = localStorage.getItem('authToken')
        const itemId = this.$route.query.itemId

        const response = await axios.post(
          `http://localhost:8080/api/items/history-payment-for-view?itemId=${itemId}&page=${this.currentPage}&size=${this.itemsPerPage}`,
          {},
          {
            headers: {
              'Authorization': `Bearer ${token}`
            }
          }
        )

        if (!response || !response.data) {
          throw new Error('No response from server')
        }


        if (response.data.success) {

          this.payments = response.data.body.content.map(payment => {
            return {
              ...payment,
              status: payment.status === 'COMPLETED' ? 'INACTIVE' : payment.status
            }
          })
          this.totalPages = response.data.body.page.totalPages
          this.totalElements = response.data.body.page.totalElements
        }
        else {
          throw new Error(response.data.message || 'Failed to load payment history')
        }
      } catch (error) {
        console.error('Error fetching payment history:', error)
        this.$toast.error('Failed to load rental history')
      } finally {
        this.loading = false
      }
    },
    formatCurrency(amount) {
      return `${amount?.toLocaleString() || '0'} ₸`
    },
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
      })
    },
    calculateRentalDays(startDate, endDate) {
      const start = new Date(startDate)
      const end = new Date(endDate)
      const diffTime = Math.abs(end - start) + 1
      return Math.ceil(diffTime / (1000 * 60 * 60 * 24))
    },
    getCarModel(attributes) {
      try {
        const attr = JSON.parse(attributes)
        return `${attr.brand} ${attr.model} (${attr.year})`
      } catch {
        return 'Vehicle details'
      }
    },
    copyToClipboard(text) {
      const toast = useToast()
      navigator.clipboard.writeText(text)
      toast.success('Booking ID copied to clipboard!', {
        position: 'top-right'
      })
    },
    viewDetails(payment) {
      this.selectedPayment = payment
      this.showDetailsDialog = true
    },
    initiateCancellation(payment) {
      this.selectedPayment = payment
      this.cancellationReason = ''
      this.showCancelDialog = true
    },
    isLateCancellation(payment) {

      const now = new Date()
      const startDate = new Date(payment.viewFrom)
      const timeDiff = startDate - now
      const daysDiff = timeDiff / (1000 * 60 * 60 * 24)

      
      return daysDiff < 2
    },
    async confirmCancellation() {
      const toast = useToast()

      try {
        this.cancelling = true
        const token = localStorage.getItem('authToken')

        const response = await axios.post(
          `http://localhost:8080/api/items/cancel-payment-for-view/${this.selectedPayment.id}`,
          {

            id: 0,
            from: "",
            days: 0,
            cartId: 0,
            price: 0,
            // id: this.selectedPayment.id,
            // from: this.selectedPayment.viewFrom,
            // days: this.calculateRentalDays(this.selectedPayment.viewFrom, this.selectedPayment.viewTill),
            // cartId: 0,
            // price: parseFloat(this.selectedPayment.item.pricePerDay),
            // cancellationReason: this.cancellationReason
          },
          {
            headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json'
            }
          }
        )

        if(!response || !response.data) {
          throw new Error('No response from server')
        }

        if (response.data.success) {
          toast.success('Rental cancelled successfully', {
            position: 'top-right'
          })
          this.showCancelDialog = false
          // Refresh the current page after cancellation
          // this.fetchPaymentHistory()
          window.location.reload()
        } else {
          toast.error(response.data.message || 'Cancellation failed', {
          position: 'top-right'
        })
        }
      } catch (error) {
        console.error('Cancellation error:', error)
        toast.error('Failed to process cancellation', {
          position: 'top-right'
        })
      } finally {
        this.cancelling = false
      }
    },
    handlePageChange(page) {
      this.currentPage = page
      localStorage.setItem('paymentHistoryPage',  page.toString())
      this.fetchPaymentHistory()
    }
  }
}
</script>

<style scoped>
.payment-history-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 32px 24px;
  margin-top: 60px;
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  background: #F8F9FF;
  border-radius: 12px;
}

.payment-title {
  font-size: 28px;
  font-weight: 700;
  color: #2B2B43;
  margin-bottom: 4px;
}

.payment-subtitle {
  font-size: 16px;
  color: #83859C;
  margin: 0;
}

.back-btn {
  border-radius: 12px;
  text-transform: none;
  letter-spacing: normal;
  font-weight: 600;
  height: 48px;
}

.divider {
  margin-bottom: 32px;
  border-color: #EDEEF2;
}

.history-content {
  min-height: 60vh;
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
  gap: 24px;
}

.loading-state p {
  color: #83859C;
  font-size: 16px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
}

.empty-icon {
  margin-bottom: 24px;
}

.empty-state h3 {
  font-size: 20px;
  color: #2B2B43;
  margin: 16px 0 8px;
}

.empty-state p {
  color: #83859C;
  margin-bottom: 24px;
}

.empty-btn {
  text-transform: none;
  letter-spacing: normal;
  font-weight: 600;
  padding: 0 24px;
  border-radius: 12px;
}

.payment-grid {

  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)) !important;
  gap: 16px;
  margin-bottom: 24px;
  /* max-width: 1200px; */
}

.payment-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: all 0.3s ease;
  border: 1px solid #EDEEF2;
}

.payment-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(78, 96, 255, 0.1);
}

.payment-card-header {
  padding: 16px;
  background-color: #F8F9FF;
  border-bottom: 1px solid #EDEEF2;
}

.payment-status {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.status-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 8px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.status-badge.active {
  background: rgba(78, 96, 255, 0.1);
  color: #4E60FF;
}

.status-badge.inactive {
  background: rgba(255, 92, 96, 0.1);
  color: #FF5C60;
}

.payment-id {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: #83859C;
}

.copy-btn {
  color: #C7C8D2;
}

.payment-dates {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.date-range {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #83859C;
}

.duration {
  font-size: 14px;
  font-weight: 600;
  color: #2B2B43;
}

.payment-card-body {
  padding: 16px;
}

.vehicle-info {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
}

.vehicle-image {
  width: 64px;
  height: 64px;
  background-color: #F5F6FA;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.vehicle-details {
  flex: 1;
}

.vehicle-details h3 {
  font-size: 18px;
  font-weight: 600;
  color: #2B2B43;
  margin: 0 0 8px 0;
}

.vehicle-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: #545563;
}

.payment-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.details-btn {
  text-transform: none;
  letter-spacing: normal;
  font-weight: 600;
}

.cancel-btn {
  text-transform: none;
  letter-spacing: normal;
  font-weight: 600;
  border-radius: 12px;
}

/* Dialog Styles */
.cancel-dialog, .details-dialog {
  border-radius: 16px !important;
  overflow: hidden;
}

.dialog-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 20px;
  font-weight: 600;
  padding: 24px;
  border-bottom: 1px solid #EDEEF2;
}

.dialog-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #F8F9FF;
}

.dialog-icon.error {
  background: #FFF5F5;
  color: #FF5C60;
}

.dialog-text {
  padding: 24px;
  font-size: 16px;
  color: #545563;
}

.dialog-text strong {
  color: #2B2B43;
}

.rental-details {
  background: #F8F9FF;
  border-radius: 8px;
  padding: 16px;
  margin: 16px 0;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  font-size: 14px;
  color: #545563;
}

.detail-item:last-child {
  margin-bottom: 0;
}

.dialog-actions {
  padding: 16px 24px;
  justify-content: flex-end;
}

.confirm-cancel {
  background: #FF5C60 !important;
  color: white !important;
  font-weight: 600;
  letter-spacing: normal;
  text-transform: none;
  border-radius: 12px;
}

.cancel-action {
  color: #83859C !important;
  text-transform: none;
  letter-spacing: normal;
}


.details-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
}

.detail-section {
  margin-bottom: 16px;
}

.detail-section h4 {
  font-size: 16px;
  font-weight: 600;
  color: #2B2B43;
  margin: 0 0 12px 0;
  padding-bottom: 8px;
  border-bottom: 1px solid #EDEEF2;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.detail-label {
  font-size: 14px;
  color: #83859C;
}

.detail-value {
  font-size: 14px;
  font-weight: 500;
  color: #2B2B43;
  text-align: right;
}

/* Pagination Styles */
.pagination-container {
  display: flex;
  justify-content: start;
  align-items: center;
  gap: 0;
  font-size: 14px;
  border-radius: 4px;
  padding: 4px;
  margin-top: 24px;
}

.pagination-item {
  cursor: pointer;
  padding: 4px 16px;
  color: #333;
  transition: background-color 0.2s, color 0.2s;
  border-right: 1px solid #e6e6e6;
}

.pagination-item:last-child {
  border-right: none;
}

.pagination-item:hover {
  background-color: #f0f0f0;
}

.pagination-item.active {
  color: #1976d2;
  font-weight: bold;
  background-color: #e6e6e6;
  border: none;
}

.pagination-item.disabled {
  color: #9e9e9e;
  cursor: not-allowed;
  pointer-events: none;
}

/* Responsive */
@media (max-width: 960px) {
  .history-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .back-btn {
    width: 100%;
  }
}

@media (max-width: 600px) {
  .payment-history-container {
    padding: 24px 16px;
  }

  .payment-title {
    font-size: 24px;
  }

  .payment-grid {
    grid-template-columns: 1fr;
  }

  .payment-actions {
    flex-direction: column;
    gap: 12px;
  }

  .details-btn, .cancel-btn {
    width: 100%;
  }
}
</style>
