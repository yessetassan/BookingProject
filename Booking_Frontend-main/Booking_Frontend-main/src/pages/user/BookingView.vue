<template>
  <Header/>
  <div class="booking-container">
    <!-- Booking Form -->
    <div v-if="!bookingSuccess">
      <div class="booking-header">
        <div class="header-content">
          <v-icon large color="#4E60FF">mdi-calendar-check</v-icon>
          <div>
            <h1 class="booking-title">{{ $t('booking_now_complete_your_booking') }}</h1>
            <p class="booking-subtitle">{{ $t('booking_now_secure_booking_process') }}</p>
          </div>
        </div>
        <v-btn
          color="#4E60FF"
          outlined
          @click="viewBookingHistory(itemId)"
          class="history-btn"
        >
          <v-icon left>mdi-history</v-icon>
          {{ $t('booking_now_history') }}
        </v-btn>
      </div>

      <v-divider class="divider"></v-divider>

      <v-row>
        <!-- Left Column -->
        <v-col cols="12" md="7">
          <!-- Item Name (Always Visible)
          <v-card class="item-name-card" elevation="0">
            <v-card-title class="section-title">
              <v-icon left color="#4E60FF">mdi-package-variant</v-icon>
              {{ itemDetails.item.name }}
            </v-card-title>
            <v-card-text class="item-price-text">
              <span class="price">KZT {{ itemDetails.item.pricePerDay.toLocaleString() }}</span>
              <span class="per-day">per day</span>
            </v-card-text>
          </v-card> -->

          <!-- Date Selection -->
          <v-card class="date-card" elevation="0">
            <v-card-title class="section-title">
              <v-icon left color="#4E60FF">mdi-calendar-range</v-icon>
              {{ $t('booking_now_select_booking_dates') }}
            </v-card-title>
            <v-card-text>
              <VueDatePicker
                v-model="dateRange"
                range
                teleport="body"
                :enable-time-picker="false"
                :min-date="new Date()"
                :disabled-week-days="[]"
                :disabled-dates="disabledDates"
                :format="'yyyy-MM-dd'"
                :auto-apply="true"
                :placeholder="$t('booking_now_select_date_range')"
                class="custom-datepicker"
                :partial-range="false"
                :max-range="30"
              />
              <div v-if="dateRange" class="selected-dates">
                <div class="date-info">
                  <v-icon small color="#4E60FF">mdi-calendar-start</v-icon>
                  <span>{{ formatDate(dateRange[0]) }}</span>
                </div>
                <div class="date-info">
                  <v-icon small color="#4E60FF">mdi-calendar-end</v-icon>
                  <span>{{ formatDate(dateRange[1]) }}</span>
                </div>
                <div class="date-info">
                  <v-icon small color="#4E60FF">mdi-clock-outline</v-icon>
                  <span>{{ calculateDays }} {{ $t('booking_now_days') }}</span>
                </div>
              </div>
            </v-card-text>
          </v-card>

          <!-- Payment Method  -->
          <v-card class="payment-card" elevation="0">
            <v-card-title class="section-title">
              <v-icon left color="#4E60FF">mdi-credit-card</v-icon>
              {{ $t('booking_now_payment_method') }}
            </v-card-title>
            <v-card-text>
              <div v-if="cards.length > 0" class="cards-container">
                <div
                  v-for="card in cards"
                  :key="card.id"
                  :class="['card-option', { 'selected': selectedCard === card.id }]"
                  @click="selectedCard = card.id"
                >
                  <div class="card-brand">
                    <v-icon v-if="card.cardNumber.startsWith('4')" color="#1A1F71">mdi-credit-card</v-icon>
                    <v-icon v-else-if="card.cardNumber.startsWith('5')" color="#EB001B">mdi-credit-card</v-icon>
                    <v-icon v-else color="#4E60FF">mdi-credit-card</v-icon>
                  </div>
                  <div class="card-info">
                    <div class="card-number">•••• •••• •••• {{ card.cardNumber.slice(-4) }}</div>
                    <div class="card-details">
                      <span class="card-name">{{ card.cardHolderName }}</span>
                      <span class="card-expiry">Exp: {{ card.expiryDate }}</span>
                    </div>
                  </div>
                  <div class="card-check">
                    <v-icon v-if="selectedCard === card.id" color="#4E60FF">mdi-check-circle</v-icon>
                  </div>
                </div>
              </div>
              <div v-else class="no-cards">
                <div class="no-cards-icon">
                  <v-icon color="#4E60FF" large>mdi-credit-card-off</v-icon>
                </div>
                <p class="no-cards-text">No payment methods saved</p>
                <v-btn
                  color="#4E60FF"
                  outlined
                  @click="navigateToAddCard"
                  class="add-card-btn"
                >
                  <v-icon left>mdi-plus</v-icon>
                  Add Payment Method
                </v-btn>
              </div>
            </v-card-text>
          </v-card>
        </v-col>

        <!-- Right Column - Booking Summary -->
        <v-col cols="12" md="5">
          <v-card class="summary-card" elevation="0">
            <v-card-title class="summary-title">
              <v-icon left color="#4E60FF">mdi-receipt</v-icon>
              {{ $t('booking_now_booking_summary') }}
            </v-card-title>

            <v-card-text>
              <div class="booking-details">
                <div class="detail-item">
                  <v-icon small>mdi-package-variant</v-icon>
                  <span>{{ itemDetails.item.name }}</span>
                </div>
                <div v-if="dateRange" class="detail-item">
                  <v-icon small>mdi-calendar-range</v-icon>
                  <span>{{ formatDate(dateRange[0]) }} - {{ formatDate(dateRange[1]) }}</span>
                </div>
                <div v-if="dateRange" class="detail-item">
                  <v-icon small>mdi-clock-outline</v-icon>
                  <span>{{ calculateDays }} {{ $t('booking_now_days') }}</span>
                </div>
              </div>

              <v-divider class="divider-light"></v-divider>

              <div class="price-breakdown">
                <div class="price-row">
                  <span>{{ $t('booking_now_daily_rate') }}</span>
                  <span>KZT {{ itemDetails.item.pricePerDay.toLocaleString() }}</span>
                </div>
                <div v-if="dateRange" class="price-row">
                  <span>{{ $t('booking_now_booking_period') }}</span>
                  <span>{{ calculateDays }} {{ $t('booking_now_days') }}</span>
                </div>
                <div v-if="dateRange" class="price-row total">
                  <span>{{ $t('booking_now_total_amount') }}</span>
                  <span class="total-amount">KZT {{ calculateTotalPrice.toLocaleString() }}</span>
                </div>
              </div>

              <v-divider class="divider-light"></v-divider>

              <div class="terms-section">
                <v-checkbox
                  v-model="termsAccepted"
                  color="#4E60FF"
                  hide-details
                  class="terms-checkbox"
                >
                  <template v-slot:label>
                    <div class="terms-text">
                      I agree to the <a href="#" class="terms-link">Terms of Service</a> and rental agreement
                    </div>
                  </template>
                </v-checkbox>
              </div>

              <v-btn
                block
                x-large
                color="#4E60FF"
                class="book-now-btn"
                :disabled="!isFormValid"
                @click="processBooking"
                depressed
              >
                <v-icon left>mdi-calendar-check</v-icon>
                {{ $t('booking_now_confirm_booking') }}
              </v-btn>

              <div class="secure-badge">
                <v-icon small color="white">mdi-shield-check</v-icon>
                <span>256-bit SSL Encryption</span>
              </div>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </div>

    <!-- Success Confirmation -->
    <div v-else class="success-container">
      <div class="success-content">
        <div class="success-icon">
          <svg width="80" height="80" viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="40" cy="40" r="40" fill="#4E60FF" fill-opacity="0.1"/>
            <circle cx="40" cy="40" r="30" fill="#4E60FF" fill-opacity="0.2"/>
            <circle cx="40" cy="40" r="20" fill="#4E60FF"/>
            <path d="M30 40L38 48L50 34" stroke="white" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>

        <h1 class="success-title">Booking Pending!</h1>
        <p class="success-subtitle">Your rental is being processed</p>

        <div class="booking-details">
          <div class="detail-card">
            <div class="detail-row">
              <span class="detail-label">Item:</span>
              <span class="detail-value">{{ itemDetails.item.name }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">Amount Paid:</span>
              <span class="detail-value">KZT {{ calculateTotalPrice.toLocaleString() }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">Rental Period:</span>
              <span class="detail-value">{{ formatDate(dateRange[0]) }} - {{ formatDate(dateRange[1]) }}</span>
            </div>
          </div>
        </div>

        <div class="action-buttons">
          <v-btn
            color="#4E60FF"
            large
            outlined
            class="history-btn"
            @click="viewBookingHistory(itemId)"
          >
            <v-icon left>mdi-history</v-icon>
            View Booking History
          </v-btn>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import Header from '@/components/Header.vue';

import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';
import { useAuthStore } from '@/stores/auth';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();


const itemDetails = ref({
  item: {
    id: null,
    name: '',
    pricePerDay: 0
  },
  freeDayList: []
});
const dateRange = ref([]);
const cards = ref([]);
const selectedCard = ref(null);
const termsAccepted = ref(false);
const bookingSuccess = ref(false);
const isLoading = ref(false);

// Computed
const calculateDays = computed(() => {
  if (!dateRange.value || dateRange.value.length < 2) return 0;
  const diffTime = Math.abs(dateRange.value[1] - dateRange.value[0]);
  return Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
});

const calculateTotalPrice = computed(() => {
  return calculateDays.value * itemDetails.value.item.pricePerDay;
});

const disabledDates = computed(() => {
  const allDates = [];
  const currentDate = new Date();
  const endDate = new Date();
  endDate.setMonth(currentDate.getMonth() + 3);


  allDates.push(new Date(currentDate));

  if (itemDetails.value.freeDayList.length === 0) return allDates;

  while (currentDate <= endDate) {
    const formattedDate = formatDate(currentDate);
    const isFree = itemDetails.value.freeDayList.some(day => day.format === formattedDate && day.free);

    if (!isFree) {
      allDates.push(new Date(currentDate));
    }

    currentDate.setDate(currentDate.getDate() + 1);
  }

  return allDates;
});

const isFormValid = computed(() => {
  return (
    dateRange.value?.length === 2 &&
    selectedCard.value &&
    termsAccepted.value
  );
});

// Methods
const formatDate = (date) => {
  if (!date) return '';
  const d = new Date(date);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

const fetchItemDetails = async () => {
  try {
    isLoading.value = true;
    const itemId = route.query.itemId;
    const response = await axios.get(`http://localhost:8080/api/book/item/${itemId}`, {
      headers: {
        Authorization: `Bearer ${authStore.token}`
      }
    });

    if (response.data.success) {

      itemDetails.value = {
        item: {
          id: response.data.body.item.id,
          name: response.data.body.item.name,
          pricePerDay: response.data.body.item.pricePerDay
        },
        freeDayList: response.data.body.freeDayList
      };
    }
  } catch (error) {
    console.error('Error fetching item details:', error);
  } finally {
    isLoading.value = false;
  }
};

const fetchPaymentCards = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/cards', {
      headers: {
        Authorization: `Bearer ${authStore.token}`
      }
    });

    if (response.data.success) {
      cards.value = response.data.body;
      if (cards.value.length > 0) {
        selectedCard.value = cards.value[0].id;
      }
    }
  } catch (error) {
    console.error('Error fetching payment cards:', error);
  }
};

const processBooking = async () => {
  try {
    isLoading.value = true;
    const payload = {
      from: formatDate(dateRange.value[0]),
      to: formatDate(dateRange.value[1]),
      totalPrice: calculateTotalPrice.value,
      cartId: selectedCard.value
    };

    const response = await axios.post(
      `http://localhost:8080/api/book/item/${itemDetails.value.item.id}`,
      payload,
      {
        headers: {
          Authorization: `Bearer ${authStore.token}`
        }
      }
    );

    if (response.data.success) {
      bookingSuccess.value = true;
    }
  } catch (error) {
    console.error('Error processing booking:', error);
  } finally {
    isLoading.value = false;
  }
};

const itemId = route.query.itemId;
console.log(`ItemID: ${itemId}`)
const viewBookingHistory = (itemId) => {
  router.push({
    path: '/booking-history',
    query: { itemId }
  });
};

const navigateToAddCard = () => {
  router.push('/my-payment-cards');
};

onMounted(() => {
  if (!authStore.isAuthenticated) {
    router.push('/login');
    return;
  }

  fetchItemDetails();
  fetchPaymentCards();
});
</script>

<style scoped>

.booking-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  margin-top: 60px;
}


.booking-header {
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

.booking-title {
  font-size: 28px;
  font-weight: 700;
  color: #2B2B43;
  margin-bottom: 4px;
}

.booking-subtitle {
  font-size: 16px;
  color: #545563;
  font-weight: 400;
}

.history-btn {
  border-radius: 8px;
  text-transform: none;
  letter-spacing: normal;
  font-weight: 600;
  height: 48px;
}

.divider {
  border-color: rgba(78, 96, 255, 0.1);
  margin: 24px 0;
}


.item-name-card {
  border-radius: 16px;
  border: 1px solid #EDEEF2;
  margin-bottom: 24px;
  transition: all 0.3s ease;
  background: white;
}

.item-name-card:hover {
  box-shadow: 0 8px 24px rgba(78, 96, 255, 0.1);
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #2B2B43;
  padding: 20px;
  background-color: #F8F9FF;
  border-bottom: 1px solid #EDEEF2;
}

.item-price-text {
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.price {
  font-size: 20px;
  font-weight: 600;
  color: #4E60FF;
}

.per-day {
  font-size: 14px;
  color: #545563;
}


.selected-dates {
  display: flex;
  gap: 16px;
  margin-top: 16px;
  padding: 12px;
  background-color: #F8F9FF;
  border-radius: 8px;
}

.date-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #545563;
}


.summary-title {
  font-size: 18px;
  font-weight: 600;
  color: #2B2B43;
  padding: 20px;
  background-color: #F8F9FF;
  border-bottom: 1px solid #EDEEF2;
}

.booking-details {
  padding: 16px;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  font-size: 14px;
  color: #545563;
}

.divider-light {
  border-color: rgba(237, 238, 242, 0.8);
  margin: 16px 0;
}

.price-breakdown {
  padding: 0 16px;
}

.price-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
  font-size: 14px;
  color: #545563;
}

.price-row.total {
  margin-top: 16px;
  font-size: 16px;
  font-weight: 600;
  color: #2B2B43;
}

.total-amount {
  color: #4E60FF;
  font-size: 18px;
}


.payment-card {
  border-radius: 16px;
  border: 1px solid #EDEEF2;
  margin-bottom: 24px;
  transition: all 0.3s ease;
  background: white;
}

.payment-card:hover {
  box-shadow: 0 8px 24px rgba(78, 96, 255, 0.1);
}

.cards-container {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 16px;
}

.card-option {
  display: flex;
  align-items: center;
  padding: 16px;
  border-radius: 12px;
  border: 1px solid #EDEEF2;
  transition: all 0.3s ease;
  cursor: pointer;
  background: white;
}

.card-option:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(78, 96, 255, 0.1);
}

.card-option.selected {
  border: 2px solid #4E60FF;
  background-color: #F8F9FF;
}

.card-brand {
  margin-right: 16px;
}

.card-info {
  flex-grow: 1;
}

.card-number {
  font-weight: 500;
  margin-bottom: 4px;
}

.card-details {
  display: flex;
  gap: 16px;
  font-size: 14px;
  color: #545563;
}

.card-name {
  font-weight: 500;
}

.card-check {
  margin-left: auto;
}

.no-cards {
  text-align: center;
  padding: 40px 20px;
}

.no-cards-icon {
  margin-bottom: 16px;
}

.no-cards-text {
  margin-bottom: 24px;
  color: #545563;
}

.add-card-btn {
  margin-top: 16px;
}

/* Terms Section */
.terms-section {
  padding: 16px;
  margin: 16px 0;
  background-color: #F8F9FF;
  border-radius: 8px;
}

.terms-text {
  font-size: 14px;
  color: #545563;
}

.terms-link {
  color: #4E60FF;
  text-decoration: none;
  font-weight: 500;
}

.terms-link:hover {
  text-decoration: underline;
}


.book-now-btn {
  border-radius: 12px;
  height: 56px;
  font-size: 16px;
  font-weight: 600;
  text-transform: none;
  letter-spacing: normal;
  margin: 16px 0;
  box-shadow: 0 4px 16px rgba(78, 96, 255, 0.2);
  transition: all 0.3s ease;
}

.book-now-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(78, 96, 255, 0.3);
}

.book-now-btn:disabled {
  background-color: #EDEEF2 !important;
  color: #A2A5B5 !important;
  box-shadow: none;
}

.secure-badge {
  background-color: #4E60FF;
  color: white;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
  justify-content: center;
  margin-top: 16px;
}

/* Success Container */
.success-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px;
  text-align: center;
  animation: fadeIn 0.6s ease-out;
}

.success-content {
  background: white;
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 8px 32px rgba(78, 96, 255, 0.1);
}

.success-icon {
  margin-bottom: 24px;
}

.success-title {
  font-size: 32px;
  font-weight: 700;
  color: #2B2B43;
  margin-bottom: 8px;
}

.success-subtitle {
  font-size: 18px;
  color: #545563;
  margin-bottom: 32px;
}

/* Booking Details */
.booking-details {
  margin: 32px 0;
}

.detail-card {
  background: #F8F9FF;
  border-radius: 12px;
  padding: 24px;
  max-width: 500px;
  margin: 0 auto;
  text-align: left;
}

.detail-row {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.detail-row:last-child {
  margin-bottom: 0;
}

.detail-label {
  font-weight: 600;
  color: #2B2B43;
  min-width: 120px;
}

.detail-value {
  flex-grow: 1;
  color: #545563;
}

.action-buttons {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}

.history-btn {
  border: 2px solid #4E60FF;
  color: #4E60FF;
  font-weight: 600;
  letter-spacing: normal;
  text-transform: none;
  padding: 0 24px;
  height: 48px;
  transition: all 0.3s ease;
}

.history-btn:hover {
  background: rgba(78, 96, 255, 0.08) !important;
  transform: translateY(-2px);
}


@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}


@media (max-width: 960px) {
  .booking-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .history-btn {
    width: 100%;
    margin-top: 16px;
  }

  .success-container {
    padding: 20px;
  }

  .success-content {
    padding: 24px;
  }

  .success-title {
    font-size: 24px;
  }

  .success-subtitle {
    font-size: 16px;
  }

  .detail-card {
    padding: 16px;
  }

  .detail-row {
    flex-wrap: wrap;
  }

  .detail-label {
    min-width: 100%;
    margin-bottom: 4px;
  }
}

@media (max-width: 600px) {
  .booking-container {
    padding: 20px 10px;
  }

  .booking-title {
    font-size: 24px;
  }

  .booking-subtitle {
    font-size: 14px;
  }

  .selected-dates {
    flex-direction: column;
    gap: 8px;
  }
}
</style>
