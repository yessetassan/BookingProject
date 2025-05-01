<template>
  <Header/>
  <div class="booking-history-container">

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
          <h1 class="booking-title">{{ $t('booking_history_your_booking_history') }}</h1>
          <p class="booking-subtitle">{{ $t('booking_history_shablon') }}</p>
        </div>
      </div>
      <v-btn
        color="#4E60FF"
        outlined
        @click="$router.go(-1)"
        class="back-btn"
      >
        <v-icon left>mdi-arrow-left</v-icon>
        {{ $t('booking_history_back_button') }}
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
        <p>Loading your bookings...</p>
      </div>

      <div v-else-if="bookings.length === 0" class="empty-state">
        <div class="empty-icon">
          <svg width="160" height="160" viewBox="0 0 160 160" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="80" cy="80" r="80" fill="#F8F9FF"/>
            <path d="M80 40V60M80 120V140M120 80H140M60 80H40" stroke="#C7C8D2" stroke-width="2" stroke-linecap="round"/>
            <path d="M80 60C87.732 60 94 66.268 94 74C94 81.732 87.732 88 80 88C72.268 88 66 81.732 66 74C66 66.268 72.268 60 80 60Z" stroke="#C7C8D2" stroke-width="2"/>
            <path d="M80 100C87.732 100 94 106.268 94 114C94 121.732 87.732 128 80 128C72.268 128 66 121.732 66 114C66 106.268 72.268 100 80 100Z" stroke="#C7C8D2" stroke-width="2"/>
          </svg>
        </div>
        <h3>No bookings found</h3>
        <p>Your booking history will appear here</p>
        <v-btn
          color="#4E60FF"
          depressed
          @click="$router.push('/')"
          class="empty-btn"
        >
          Browse Rentals
        </v-btn>
      </div>

      <!-- Booking List -->
      <div v-else>
        <div class="booking-grid">
          <div
            v-for="booking in bookings"
            :key="booking.id"
            class="booking-card"
          >
            <div class="booking-card-header">
              <div class="booking-status">
                <div class="status-badge" :class="booking.status.toLowerCase()">
                  <v-icon small>{{ getStatusIcon(booking.status) }}</v-icon>
                  <span>{{ formatStatus(booking.status) }}</span>
                </div>
                <div class="booking-id">
                  <span>{{ $t('booking_history_booking') }} #{{ booking.id }}</span>
                  <v-btn icon x-small @click="copyToClipboard(booking.id)" class="copy-btn">
                    <v-icon x-small>mdi-content-copy</v-icon>
                  </v-btn>
                </div>
              </div>
              <div class="booking-dates">
                <div class="date-range">
                  <v-icon small color="#83859C">mdi-calendar</v-icon>
                  <span>{{ formatDate(booking.startDatetime) }} - {{ formatDate(booking.endDatetime) }}</span>
                </div>
                <div class="duration">
                  {{ calculateRentalDays(booking.startDatetime, booking.endDatetime) }} {{ $t('booking_now_days') }}
                </div>
              </div>
            </div>

            <div class="booking-card-body">
              <div class="item-info">
                <div class="item-image">
                  <v-img
                    :src="getItemImage(booking.item)"
                    height="64"
                    width="64"
                    cover
                    class="rounded-lg"
                  ></v-img>
                </div>
                <div class="item-details">
                  <h3>{{ booking.item.name }}</h3>
                  <div class="item-meta">
                    <div class="meta-item">
                      <v-icon small color="#83859C">mdi-cash</v-icon>
                      <span>{{ formatCurrency(booking.totalPrice) }} total</span>
                    </div>
                    <div class="meta-item">
                      <v-icon small color="#83859C">mdi-account</v-icon>
                      <span>{{ $t('card_detail_owner') }}: {{ booking.owner.firstname }} {{ booking.owner.lastname }}</span>
                    </div>
                  </div>
                </div>
              </div>

              <div class="booking-actions">
                <v-btn
                  text
                  color="#4E60FF"
                  @click="viewDetails(booking)"
                  class="details-btn"
                >
                  <v-icon left small>mdi-eye</v-icon>
                  {{ $t('booking_history_view_details') }}
                </v-btn>

                <v-btn
                  v-if="canCancel(booking)"
                  outlined
                  color="#FF5C60"
                  @click="initiateCancellation(booking)"
                  class="cancel-btn"
                >
                  <v-icon left small>mdi-close</v-icon>
                  Cancel Booking
                </v-btn>
              </div>
            </div>
          </div>
        </div>

        <!-- Pagination -->
        <div class="pagination-container" v-if="totalPages > 1">
          <span
            :class="['pagination-item', { 'disabled': currentPage === 0 }]"
            @click="currentPage > 0 && handlePageChange(currentPage - 1)"
          >
            ← {{ $t('booking_history_pagination_previous') }}
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
          {{ $t('booking_history_pagination_next') }} →
          </span>
        </div>
      </div>
    </div>

    <!-- Booking Details Dialog -->
    <v-dialog v-model="showDetailsDialog" max-width="600" persistent>
      <v-card class="details-dialog">
        <v-card-title class="dialog-title">
          <div class="dialog-icon">
            <v-icon>mdi-information</v-icon>
          </div>
          {{ $t('booking_history_details') }}
        </v-card-title>

        <v-card-text class="dialog-text">
          <div class="details-grid">
            <div class="detail-section">
              <h4>{{ $t('booking_history_item_information') }}</h4>
              <div class="detail-row">
                <span class="detail-label">Item:</span>
                <span class="detail-value">{{ selectedBooking?.item.name }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">Description:</span>
                <span class="detail-value">{{ selectedBooking?.item.description }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">Daily Rate:</span>
                <span class="detail-value">{{ formatCurrency(selectedBooking?.item.pricePerDay) }}</span>
              </div>
              <div v-for="(value, key) in parsedAttributes" :key="key" class="detail-row">
                <span class="detail-label">{{ formatAttributeKey(key) }}:</span>
                <span class="detail-value">{{ value }}</span>
              </div>
            </div>

            <div class="detail-section">
              <h4>{{ $t('booking_history_period') }}</h4>
              <div class="detail-row">
                <span class="detail-label">Start Date:</span>
                <span class="detail-value">{{ formatDate(selectedBooking?.startDatetime) }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">End Date:</span>
                <span class="detail-value">{{ formatDate(selectedBooking?.endDatetime) }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">Duration:</span>
                <span class="detail-value">{{ calculateRentalDays(selectedBooking?.startDatetime, selectedBooking?.endDatetime) }} days</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">Total Price:</span>
                <span class="detail-value">{{ formatCurrency(selectedBooking?.totalPrice) }}</span>
              </div>
            </div>

            <div class="detail-section">
              <h4>{{ $t('booking_history_owner_information') }}</h4>
              <div class="detail-row">
                <span class="detail-label">Name:</span>
                <span class="detail-value">{{ selectedBooking?.owner.firstname }} {{ selectedBooking?.owner.lastname }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">Phone:</span>
                <span class="detail-value">{{ selectedBooking?.owner.phone || 'Not provided' }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">Email:</span>
                <span class="detail-value">{{ selectedBooking?.owner.username }}</span>
              </div>
            </div>

            <div class="detail-section">
              <h4>{{ $t('booking_history_booking_status') }}</h4>
              <div class="detail-row">
                <span class="detail-label">Status:</span>
                <span class="detail-value status-badge" :class="selectedBooking?.status.toLowerCase()">
                  {{ formatStatus(selectedBooking?.status) }}
                </span>
              </div>
              <div class="detail-row">
                <span class="detail-label">Booked On:</span>
                <span class="detail-value">{{ formatDateTime(selectedBooking?.createdAt) }}</span>
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
          {{ $t('booking_history_close_button') }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Cancellation Dialog -->
    <v-dialog v-model="showCancelDialog" max-width="500" persistent>
      <v-card class="cancel-dialog">
        <v-card-title class="dialog-title">
          <div class="dialog-icon error">
            <v-icon>mdi-alert-circle</v-icon>
          </div>
          Cancel Booking
        </v-card-title>

        <v-card-text class="dialog-text">
          <p>Are you sure you want to cancel your booking for <strong>{{ selectedBooking?.item.name }}</strong>?</p>

          <div class="booking-details">
            <div class="detail-item">
              <v-icon small color="#83859C">mdi-calendar</v-icon>
              <span>{{ formatDate(selectedBooking?.startDatetime) }} - {{ formatDate(selectedBooking?.endDatetime) }}</span>
            </div>
            <div class="detail-item">
              <v-icon small color="#83859C">mdi-clock</v-icon>
              <span>{{ calculateRentalDays(selectedBooking?.startDatetime, selectedBooking?.endDatetime) }} days</span>
            </div>
            <div class="detail-item">
              <v-icon small color="#83859C">mdi-cash</v-icon>
              <span>{{ formatCurrency(selectedBooking?.totalPrice) }} total</span>
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
  </div>
</template>

<script setup>
import Header from '@/components/Header.vue';

import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()


const bookings = ref([])
const loading = ref(true)
const currentPage = ref(0)
const itemsPerPage = ref(2)
const totalPages = ref(1)
const totalElements = ref(0)
const showDetailsDialog = ref(false)
const selectedBooking = ref(null)
const showCancelDialog = ref(false)
const cancellationReason = ref('')
const cancelling = ref(false)


const visiblePages = computed(() => {
  if (totalPages.value <= 1) return []

  const pages = []
  const maxVisible = 5
  let start = Math.max(currentPage.value - Math.floor(maxVisible / 2), 0)
  let end = Math.min(start + maxVisible - 1, totalPages.value - 1)

  if (end - start + 1 < maxVisible) {
    start = Math.max(end - maxVisible + 1, 0)
  }

  for (let i = start; i <= end; i++) {
    pages.push(i)
  }

  return pages
})

const parsedAttributes = computed(() => {
  try {
    return JSON.parse(selectedBooking.value?.item.attributes || '{}')
  } catch {
    return {}
  }
})


const fetchBookingHistory = async () => {
  try {
    loading.value = true
    const itemId = route.query.itemId

    if (!itemId) {
      console.error('Item ID is required');
      bookings.value = [];
      return;
    }

    const url = `http://localhost:8080/api/book/item/history-for-user/${itemId}?page=${currentPage.value}&size=${itemsPerPage.value}` // for user


    // const url = itemId
    //   ? `http://localhost:8080/api/book/item/history-for-user/${itemId}?page=${currentPage.value}&size=${itemsPerPage.value}` // for user
    //   : `http://localhost:8080/api/book/item/history/${itemId}?page=${currentPage.value}&size=${itemsPerPage.value}` // for owner

    const response = await axios.get(url, {
      headers: {
        Authorization: `Bearer ${authStore.token}`
      }
    })

    if (response.data.success) {
      bookings.value = response.data.body.content
      totalPages.value = response.data.body.page.totalPages
      totalElements.value = response.data.body.page.totalElements

      if (bookings.value.length === 0) {
        if (currentPage.value > 0) {

          currentPage.value = 0;
          await fetchBookingHistory();
        }
        return;
      }

      if (currentPage.value >= totalPages.value) {
        currentPage.value = Math.max(0, totalPages.value - 1);
      }

    }
  } catch (error) {
    console.error('Error fetching booking history:', error)
  } finally {
    loading.value = false
  }
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' })
}

const formatDateTime = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const formatCurrency = (amount) => {
  if (!amount) return 'KZT 0'
  return `KZT ${parseFloat(amount).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`
}

const calculateRentalDays = (startDate, endDate) => {
  if (!startDate || !endDate) return 0
  const start = new Date(startDate)
  const end = new Date(endDate)
  const diffTime = Math.abs(end - start + 1)
  return Math.ceil(diffTime / (1000 * 60 * 60 * 24))
}

const getItemImage = (item) => {
  return item.files?.[0]?.fileUrl || 'https://via.placeholder.com/300'
}

const formatStatus = (status) => {
  const statusMap = {
    'PENDING': 'Pending',
    'CONFIRMED': 'Confirmed',
    'ACTIVE': 'Active',
    'COMPLETED': 'Completed',
    'CANCELLED': 'Cancelled',
    'REJECTED': 'Rejected'
  }
  return statusMap[status] || status
}

const getStatusIcon = (status) => {
  const iconMap = {
    'PENDING': 'mdi-clock',
    'CONFIRMED': 'mdi-check-circle',
    'ACTIVE': 'mdi-car',
    'COMPLETED': 'mdi-check-all',
    'CANCELLED': 'mdi-close-circle',
    'REJECTED': 'mdi-alert-circle'
  }
  return iconMap[status] || 'mdi-help-circle'
}

const formatAttributeKey = (key) => {
  return key.split(/(?=[A-Z])/).join(' ').toLowerCase()
    .replace(/^\w/, c => c.toUpperCase())
}

const canCancel = (booking) => {
  const now = new Date()
  const startDate = new Date(booking.startDatetime)
  return booking.status === 'PENDING' || (booking.status === 'ACTIVE' && now < startDate)
}

const viewDetails = (booking) => {
  selectedBooking.value = booking
  showDetailsDialog.value = true
}

const copyToClipboard = (text) => {
  navigator.clipboard.writeText(text.toString())

}

const initiateCancellation = (booking) => {
  selectedBooking.value = booking
  cancellationReason.value = ''
  showCancelDialog.value = true
}

const confirmCancellation = async () => {
  const itemId = route.query.itemId

  try {
    cancelling.value = true
    await axios.post(`http://localhost:8080/api/book/cancel/${selectedBooking.value.id}`, {
      reason: cancellationReason.value
    }, {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })





    fetchBookingHistory()
    showCancelDialog.value = false
  } catch (error) {
    console.error('Error cancelling booking:', error)
  } finally {
    cancelling.value = false
  }
}

const handlePageChange = (page) => {
  currentPage.value = page
  fetchBookingHistory()
}


onMounted(() => {
  if (!authStore.isAuthenticated) {
    router.push('/login')
    return
  }

  fetchBookingHistory()
})
</script>

<style scoped>
.booking-history-container {
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

.booking-title {
  font-size: 28px;
  font-weight: 700;
  color: #2B2B43;
  margin-bottom: 4px;
}

.booking-subtitle {
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

.booking-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.booking-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: all 0.3s ease;
  border: 1px solid #EDEEF2;
}

.booking-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(78, 96, 255, 0.1);
}

.booking-card-header {
  padding: 16px;
  background-color: #F8F9FF;
  border-bottom: 1px solid #EDEEF2;
}

.booking-status {
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

.status-badge.pending {
  background: rgba(255, 193, 7, 0.1);
  color: #FFA000;
}

.status-badge.confirmed {
  background: rgba(78, 96, 255, 0.1);
  color: #4E60FF;
}

.status-badge.active {
  background: rgba(76, 175, 80, 0.1);
  color: #4CAF50;
}

.status-badge.completed {
  background: rgba(158, 158, 158, 0.1);
  color: #9E9E9E;
}

.status-badge.cancelled, .status-badge.rejected {
  background: rgba(255, 92, 96, 0.1);
  color: #FF5C60;
}

.booking-id {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: #83859C;
}

.copy-btn {
  color: #C7C8D2;
}

.booking-dates {
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

.booking-card-body {
  padding: 16px;
}

.item-info {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
}

.item-image {
  width: 64px;
  height: 64px;
  background-color: #F5F6FA;
  border-radius: 8px;
  overflow: hidden;
}

.item-details {
  flex: 1;
}

.item-details h3 {
  font-size: 18px;
  font-weight: 600;
  color: #2B2B43;
  margin: 0 0 8px 0;
}

.item-meta {
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

.booking-actions {
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

.booking-details {
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

/* Details Dialog Styles */
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
  .booking-history-container {
    padding: 24px 16px;
  }

  .booking-title {
    font-size: 24px;
  }

  .booking-grid {
    grid-template-columns: 1fr;
  }

  .booking-actions {
    flex-direction: column;
    gap: 12px;
  }

  .details-btn, .cancel-btn {
    width: 100%;
  }
}
</style>
