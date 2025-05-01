<template>
  <Header/>
  <div class="payment-container">
    <!-- Payment Form -->
    <div v-if="!paymentSuccess">
      <div class="payment-header">
        <div class="header-content">
          <v-icon large color="#4E60FF">mdi-lock-check</v-icon>
          <div>
            <h1 class="payment-title">Complete Your Rental</h1>
            <p class="payment-subtitle">Secure payment processing</p>
          </div>
        </div>
        <v-btn
          color="#4E60FF"
          outlined
          @click="viewPaymentHistory"
          class="history-btn"
        >
          <v-icon left>mdi-history</v-icon>
          Payment History
        </v-btn>
      </div>

      <v-divider class="divider"></v-divider>

      <v-row>
        <!-- Left Column -->
        <v-col cols="12" md="7">
          <!-- Date Picker -->
          <v-card class="date-card" elevation="0">
            <v-card-title class="section-title">
              <v-icon left color="#4E60FF">mdi-calendar</v-icon>
              Select Rental Date
            </v-card-title>
            <v-card-text class="date-picker-container">
              <VueDatePicker
              teleport="body"
                v-model="selectedDate"
                :format="'yyyy-MM-dd'"
                :enable-time-picker="false"
                :min-date="new Date()"
                :max-date="maxDate"
                :auto-apply="true"
                placeholder="Select date"
                class="custom-datepicker"
              />
            </v-card-text>
          </v-card>

          <!-- Rental Period Selection -->
          <v-card class="rental-card" elevation="0">
            <v-card-title class="section-title">
              <v-icon left color="#4E60FF">mdi-calendar-range</v-icon>
              Select Rental Period
            </v-card-title>
            <v-card-text>
              <div class="rental-options">
                <div
                  v-for="period in rentalPeriods"
                  :key="period.days"
                  :class="['period-option', { 'selected': selectedPeriod === period.days }]"
                  @click="selectPeriod(period)"
                >
                  <div class="period-content">
                    <div class="period-days">
                      <span class="days">{{ period.label }}</span>
                    </div>
                    <div class="period-price">
                      <span class="price">{{ formatCurrency(period.price) }}</span>
                      <span v-if="period.recommended" class="recommended-badge">RECOMMENDED</span>
                    </div>
                  </div>
                </div>
              </div>
            </v-card-text>
          </v-card>

          <!-- Card Selection -->
          <v-card class="card-selection" elevation="0">
            <v-card-title class="section-title">
              <v-icon left color="#4E60FF">mdi-credit-card</v-icon>
              Payment Method
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

        <!-- Right Column - Order Summary -->
        <v-col cols="12" md="5">
          <v-card class="summary-card" elevation="0">
            <v-card-title class="summary-title">
              <v-icon left color="#4E60FF">mdi-receipt</v-icon>
              Order Summary
            </v-card-title>

            <v-card-text>
              <div class="booking-details">
                <!-- <div class="detail-item">
                  <v-icon small>mdi-identifier</v-icon>
                  <span>Booking #{{ bookingId }}</span>
                </div> -->
                <div class="detail-item">
                  <v-icon small>mdi-calendar</v-icon>
                  <span>{{ getPeriodLabel(selectedPeriod) }}</span>
                </div>
                <div v-if="selectedDate" class="detail-item">
                  <v-icon small>mdi-calendar</v-icon>
                  <span>Starts: {{ formatDate(selectedDate) }}</span>
                </div>
              </div>

              <v-divider class="divider-light"></v-divider>

              <div class="price-breakdown">
                <div class="price-row">
                  <span>Rental Period</span>
                  <span>{{ getPeriodLabel(selectedPeriod) }}</span>
                </div>
                <div class="price-row">
                  <span>Rental Price</span>
                  <span>{{ formatCurrency(subtotal) }}</span>
                </div>
                <!-- <div class="price-row">
                  <span>Service Fee</span>
                  <span>{{ formatCurrency(serviceFee) }}</span>
                </div> -->
                <div class="price-row total">
                  <span>Total Amount</span>
                  <span class="total-amount">{{ formatCurrency(totalAmount) }}</span>
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
                      I agree to the <a href="#" class="terms-link">Terms of Service</a> and authorize payment
                    </div>
                  </template>
                </v-checkbox>
              </div>

              <v-btn
                block
                x-large
                color="#4E60FF"
                class="pay-now-btn"
                :disabled="!isFormValid"
                @click="processPayment"
                depressed
              >
                <v-icon left>mdi-lock</v-icon>
                Confirm Payment
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

        <h1 class="success-title">Payment Successful!</h1>
        <p class="success-subtitle">Your rental has been confirmed</p>

        <div class="payment-details">
          <div class="detail-card">
            <div class="detail-row">
              <!-- <span class="detail-label">Payment ID:</span>
              <span class="detail-value">{{ paymentId }}</span>
              <v-btn icon small @click="copyPaymentId" class="copy-btn">
                <v-icon small>mdi-content-copy</v-icon>
              </v-btn> -->
            </div>
            <div class="detail-row">
              <span class="detail-label">Amount Paid:</span>
              <span class="detail-value">{{ formatCurrency(totalAmount) }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">Rental Period:</span>
              <span class="detail-value">{{ getPeriodLabel(selectedPeriod) }}</span>
            </div>
          </div>
        </div>

        <div class="action-buttons">
          <v-btn
            color="#4E60FF"
            large
            outlined
            class="history-btn"
            @click="viewPaymentHistory"
          >
            <v-icon left>mdi-history</v-icon>
            View Payment History
          </v-btn>
        </div>
      </div>
    </div>
  </div>

  <v-dialog v-model="showWarningModal" max-width="500" persistent>
    <v-card class="warning-dialog">
      <v-card-title class="warning-title">
        <div class="warning-icon">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 2C6.48 2 2 6.48 2 12C2 17.52 6.48 22 12 22C17.52 22 22 17.52 22 12C22 6.48 17.52 2 12 2ZM12 20C7.59 20 4 16.41 4 12C4 7.59 7.59 4 12 4C16.41 4 20 7.59 20 12C20 16.41 16.41 20 12 20Z" fill="#FFA000"/>
            <path d="M12 7C11.45 7 11 7.45 11 8V12C11 12.55 11.45 13 12 13C12.55 13 13 12.55 13 12V8C13 7.45 12.55 7 12 7ZM11 15C11 14.45 11.45 14 12 14C12.55 14 13 14.45 13 15C13 15.55 12.55 16 12 16C11.45 16 11 15.55 11 15Z" fill="#FFA000"/>
          </svg>
        </div>
        <span>Booking Conflict</span>
      </v-card-title>

      <v-card-text class="warning-content">
        <p>{{ warningMessage }}</p>

        <div class="conflict-period">
          <v-icon small color="#FFA000">mdi-calendar-alert</v-icon>
          <span>{{ conflictPeriod }}</span>
        </div>
      </v-card-text>

      <v-card-actions class="warning-actions">
        <v-btn
          color="#4E60FF"
          depressed
          @click="showWarningModal = false"
          class="understand-btn"
        >
          I Understand
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import Header from '@/components/Header.vue';
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';
import axios from 'axios';
import { useToast } from 'vue-toast-notification'
import 'vue-toast-notification/dist/theme-sugar.css'

export default {
  components: { VueDatePicker },
  data() {
    return {
      paymentSuccess: false,
      paymentId: '',
      selectedDate: null,
      selectedPeriod: 30, //month 30
      selectedCard: null,
      cards: [],
      // bookingId: Math.floor(Math.random() * 900000) + 100000,
      termsAccepted: false,
      rentalPeriods: [
        { days: 7, price: 1000, label: '7 days' },
        { days: 14, price: 1500, label: '14 days' },
        { days: 30, price: 2500, label: '1 month', recommended: true }
      ],
      maxDate: new Date(new Date().setFullYear(new Date().getFullYear() + 1)),
      itemId: null,

      showWarningModal: false,
      warningMessage: '',
      conflictPeriod: ''
    }
  },
  computed: {
    subtotal() {
      const period = this.rentalPeriods.find(p => p.days === this.selectedPeriod);
      return period ? period.price : 0;
    },
    serviceFee() {
      return this.subtotal * 0.05; // 5% service fee
    },
    totalAmount() {
      return this.subtotal;
    },
    isFormValid() {
      return this.termsAccepted &&
             this.selectedPeriod &&
             this.selectedDate &&
             (this.cards.length > 0 ? this.selectedCard !== null : false);
    }
  },
  created() {
    this.fetchCards();
    this.itemId = this.$route.query.itemId;
  },
  methods: {
    formatCurrency(amount) {
      return `${amount.toLocaleString()} ₸`;
    },
    formatDate(date) {
      if (!date) return '';
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    getPeriodLabel(days) {
      const period = this.rentalPeriods.find(p => p.days === days);
      return period ? period.label : `${days} days`;
    },
    selectPeriod(period) {
      this.selectedPeriod = period.days;
    },
    async fetchCards() {
      try {
        const token = localStorage.getItem('authToken');
        const response = await axios.get('http://localhost:8080/api/cards', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (response.data.success) {
          this.cards = response.data.body;
          if (this.cards.length > 0) {
            this.selectedCard = this.cards[0].id;
          }
        }
      } catch (error) {
        console.error('Error fetching cards:', error);
        this.$toast.error('Failed to load payment methods');
      }
    },
    async processPayment() {
      if (!this.isFormValid) return;

      try {
        const token = localStorage.getItem('authToken');

        if (!this.itemId) {
          this.$toast.error('Item ID is missing');
          return;
        }

        const payload = {
          id: parseInt(this.itemId),
          from: this.formatDate(this.selectedDate),
          days: this.selectedPeriod,
          cartId: this.selectedCard,
          price: this.totalAmount
        };

        const response = await axios.post(
          'http://localhost:8080/api/items/payment-for-view',
          payload,
          {
            headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json'
            }
          }
        );

        if (response.data.success) {
          // toast.error('Rental paid successfully!', {
          //   position: 'top-right'
          // })
          this.paymentId = response.data.paymentId || 'PAY-' + Math.random().toString(36).substr(2, 10).toUpperCase();
          this.paymentSuccess = true;
        } else {
          if(response.data.status ==='INCOMPLETE') {

            const currentLanguage = localStorage.getItem('currentLanguage') || 'en';

            const reasonByLang = {
              kz: response.data.reason,
              ru: response.data.reasonRu,
              en: response.data.reasonEn
            };

            this.warningMessage = reasonByLang[currentLanguage] || response.data.reasonEn;
            this.conflictPeriod = this.extractConflictPeriod(this.warningMessage);
            this.showWarningModal = true
          } else {
            this.$toast.error(response.data.message || 'Payment failed');
          }

        }
      } catch (error) {
        console.error('Payment error:', error);
        this.$toast.error('Payment processing failed');
      }
    },

    extractConflictPeriod(text) {
      const regex = /(\d{4}-\d{2}-\d{2} - \d{4}-\d{2}-\d{2})/;
      const match = text.match(regex);
      return match ? match[0] : '';
    },
    copyPaymentId() {
      navigator.clipboard.writeText(this.paymentId);
      this.$toast.success('Payment ID copied to clipboard!');
    },
    viewPaymentHistory() {
      this.$router.push({
        path: '/payment-history',
        query: { itemId: this.itemId }
      });
    },
    navigateToAddCard() {
      this.$router.push('/my-payment-cards');
    }
  }
}
</script>

<style scoped>


.warning-dialog {
  border-radius: 16px !important;
  overflow: hidden;
}

.warning-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 20px;
  font-weight: 600;
  color: #D32F2F;
  padding: 24px;
  background-color: #FFF5F5;
  border-bottom: 1px solid #FFCDD2;
}

.warning-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #FFF5F5;
}

.warning-content {
  padding: 24px;
  font-size: 16px;
  color: #5F6368;
  line-height: 1.6;
}

.conflict-period {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 16px;
  padding: 12px;
  background-color: #FFF9E6;
  border-radius: 8px;
  font-weight: 500;
  color: #5D4037;
}

.warning-actions {
  padding: 16px 24px;
  justify-content: flex-end;
  background-color: #FAFAFA;
  border-top: 1px solid #EEEEEE;
}

.understand-btn {
  border-radius: 8px;
  font-weight: 600;
  letter-spacing: normal;
  text-transform: none;
  padding: 0 24px;
  height: 42px;
}


.v-dialog {
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.payment-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  margin-top: 60px;
}


.payment-header {
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

.payment-title {
  font-size: 28px;
  font-weight: 700;
  color: #2B2B43;
  margin-bottom: 4px;
}

.payment-subtitle {
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


.date-card,
.rental-card,
.card-selection,
.summary-card {
  border-radius: 16px;
  border: 1px solid #EDEEF2;
  margin-bottom: 24px;
  /* overflow: auto; */
  transition: all 0.3s ease;
  background: white;
}

/* .date-card {
  height: 65%;
} */

.date-card:hover,
.rental-card:hover,
.card-selection:hover,
.summary-card:hover {
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

/* Date Picker */
/* .date-picker-container {
  position: relative;
  z-index: 10;
  padding: 16px;
}

.custom-datepicker {
  width: 100%;
} */

/* Rental Options */
.rental-options {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 16px;
  padding: 8px;
}

.period-option {
  border-radius: 12px;
  border: 1px solid #EDEEF2;
  transition: all 0.3s ease;
  cursor: pointer;
  padding: 16px;
  background: white;
  position: relative;
  overflow: hidden;
}

.period-option:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(78, 96, 255, 0.1);
}

.period-option.selected {
  border: 2px solid #4E60FF;
  background-color: #F8F9FF;
  box-shadow: 0 4px 16px rgba(78, 96, 255, 0.15);
}

.period-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.period-days {
  margin-bottom: 8px;
}

.days {
  font-size: 16px;
  font-weight: 600;
  color: #2B2B43;
}

.period-price {
  margin-top: auto;
}

.price {
  font-size: 18px;
  font-weight: 600;
  color: #4E60FF;
  display: block;
}

.recommended-badge {
  display: inline-block;
  background-color: #4E60FF;
  color: white;
  font-size: 10px;
  font-weight: 600;
  padding: 2px 8px;
  border-radius: 10px;
  margin-top: 4px;
  text-transform: uppercase;
}


.cards-container {
  display: flex;
  flex-direction: column;
  gap: 12px;
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

/* Order Summary */
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

.pay-now-btn {
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

.pay-now-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(78, 96, 255, 0.3);
}

.pay-now-btn:disabled {
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

/* Payment Details */
.payment-details {
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

.copy-btn {
  margin-left: 8px;
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

/* Animations */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

/* Responsive */
@media (max-width: 960px) {
  .payment-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .history-btn {
    width: 100%;
    margin-top: 16px;
  }

  .rental-options {
    grid-template-columns: 1fr 1fr;
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
  .rental-options {
    grid-template-columns: 1fr;
  }

  .payment-container {
    padding: 20px 10px;
  }

  .payment-title {
    font-size: 24px;
  }

  .payment-subtitle {
    font-size: 14px;
  }
}
</style>
