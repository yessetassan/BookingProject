<template>
  <Header />
  <div class="cards-container">

    <v-dialog v-model="addCardDialog" max-width="600" persistent>
      <v-card class="add-card-dialog">
        <v-card-title class="dialog-title">
          <v-icon left color="#4E60FF">mdi-credit-card-plus</v-icon>
          Add New Payment Card
        </v-card-title>

        <v-card-text class="dialog-content">
          <v-form ref="cardForm" v-model="valid" lazy-validation>
            <v-text-field
              v-model="newCard.cardHolderName"
              label="Cardholder Name"
              outlined
              placeholder="John Doe"
              prepend-inner-icon="mdi-account"
              :rules="[
                rules.required,
                rules.maxLength(100, 'Card holder name must be at most 100 characters')
              ]"
              counter="100"
              class="mb-4"
            ></v-text-field>

            <v-text-field
              v-model="newCard.cardNumber"
              label="Card Number"
              outlined
              placeholder="1234 5678 9012 3456"
              prepend-inner-icon="mdi-credit-card"
              :rules="[
                rules.required,
                v => /^\d{16}$/.test(v.replace(/\s/g, '')) || 'Card number must be 16 digits'
              ]"
              class="mb-4"
              @input="formatCardNumber"
            ></v-text-field>

            <v-row>
              <v-col cols="6">
                <v-text-field
                  v-model="newCard.expiryDate"
                  label="Expiry Date"
                  outlined
                  placeholder="MM/YYYY"
                  prepend-inner-icon="mdi-calendar"
                  :rules="[
                    rules.required,
                    v => /^(0[1-9]|1[0-2])\/?([0-9]{4})$/.test(v) || 'Expiry date must be in MM/YYYY format'
                  ]"
                  @input="formatExpiryDate"
                ></v-text-field>
              </v-col>
              <v-col cols="6">
                <v-text-field
                  v-model="newCard.cvv"
                  label="CVV"
                  outlined
                  placeholder="•••"
                  prepend-inner-icon="mdi-lock"
                  type="password"
                  :rules="[
                    rules.required,
                    v => /^\d{3,4}$/.test(v) || 'CVV must be 3 or 4 digits'
                  ]"
                  maxlength="4"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-form>

          <div class="card-preview">
            <div class="credit-card" :class="{ 'flipped': showBack }">
              <div class="card-front">
                <div class="card-logo">
                  <img :src="cardLogo" alt="Card Type">
                </div>
                <div class="card-number">
                  {{ newCard.cardNumber || '•••• •••• •••• ••••' }}
                </div>
                <div class="card-details">
                  <div class="card-holder">
                    <div class="label">Cardholder Name</div>
                    <div class="value">{{ newCard.cardHolderName || 'YOUR NAME' }}</div>
                  </div>
                  <div class="card-expiry">
                    <div class="label">Expires</div>
                    <div class="value">{{ newCard.expiryDate || 'MM/YYYY' }}</div>
                  </div>
                </div>
              </div>
              <div class="card-back">
                <div class="card-strip"></div>
                <div class="card-cvv">
                  <div class="label">CVV</div>
                  <div class="value">{{ newCard.cvv || '•••' }}</div>
                </div>
              </div>
            </div>
            <v-btn
              small
              text
              @click="showBack = !showBack"
              class="flip-btn"
            >
              <v-icon left small>{{ showBack ? 'mdi-rotate-left' : 'mdi-rotate-right' }}</v-icon>
              {{ showBack ? 'Show Front' : 'Show Back' }}
            </v-btn>
          </div>
        </v-card-text>

        <v-card-actions class="dialog-actions">
          <v-btn text @click="closeAddCardDialog" class="cancel-btn">
            Cancel
          </v-btn>
          <v-spacer></v-spacer>
          <v-btn
            color="#4E60FF"
            depressed
            @click="saveCard"
            :loading="saving"
            :disabled="!valid"
            class="save-btn"
          >
            <v-icon left>mdi-content-save</v-icon>
            Save Card
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Delete Confirmation Dialog -->
    <v-dialog v-model="deleteDialog" max-width="500" persistent>
      <v-card class="delete-dialog">
        <v-card-title class="dialog-title">
          <v-icon left color="#FF5C60">mdi-alert-circle</v-icon>
          Delete Card
        </v-card-title>

        <v-card-text class="dialog-text">
          <p>Are you sure you want to delete this card?</p>
          <div class="card-preview-small">
            <div class="card-brand">
              <img :src="getCardLogo(cardToDelete.cardNumber)" alt="Card Type">
            </div>
            <div class="card-info">
              <div class="card-number-masked">
                •••• •••• •••• {{ cardToDelete.cardNumber.slice(-4) }}
              </div>
              <div class="card-holder">
                {{ cardToDelete.cardHolderName }}
              </div>
            </div>
          </div>
        </v-card-text>

        <v-card-actions class="dialog-actions">
          <v-btn text @click="deleteDialog = false" class="cancel-btn">
            Cancel
          </v-btn>
          <v-spacer></v-spacer>
          <v-btn
            color="#FF5C60"
            depressed
            @click="confirmDelete"
            :loading="deleting"
            class="confirm-delete-btn"
          >
            <v-icon left>mdi-delete</v-icon>
            Delete Card
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Main Content -->
    <div class="cards-content">
      <div class="cards-header">
        <h1 class="title">
          <v-icon large color="#4E60FF">mdi-wallet</v-icon>
          My Payment Cards
        </h1>
        <v-btn
          color="#4E60FF"
          depressed
          @click="openAddCardDialog"
          class="add-card-btn"
        >
          <v-icon left>mdi-plus</v-icon>
          Add New Card
        </v-btn>
      </div>

      <v-divider class="divider"></v-divider>

      <div v-if="loading" class="loading-container">
        <v-progress-circular indeterminate color="#4E60FF" size="64"></v-progress-circular>
        <p>Loading your cards...</p>
      </div>

      <div v-else-if="cards.length === 0" class="empty-state">
        <div class="empty-icon">
          <v-icon size="80" color="#EDEEF2">mdi-credit-card-off</v-icon>
        </div>
        <h3 class="empty-title">No Payment Cards</h3>
        <p class="empty-subtitle">You haven't added any payment cards yet</p>
        <v-btn
          color="#4E60FF"
          depressed
          @click="openAddCardDialog"
          class="empty-action"
        >
          <v-icon left>mdi-plus</v-icon>
          Add Your First Card
        </v-btn>
      </div>

      <div v-else class="cards-grid">
  <div
    v-for="(card, index) in cards"
    :key="index"
    class="card-item"
    :class="{ 'primary-card': card.isPrimary }"
  >
    <div class="card-item-content">
      <div class="card-brand">
        <img :src="getCardLogo(card.cardNumber)" alt="Card Type" class="card-logo-img">
      </div>
      <div class="card-details">
        <div class="card-number-container">
          <div class="card-number">
            <span v-if="showFullNumbers[index]">
              {{ formatCardNumberDisplay(card.cardNumber) }}
            </span>
            <span v-else>
              •••• •••• •••• {{ card.cardNumber.slice(-4) }}
            </span>
          </div>
          <v-tooltip top>
            <template v-slot:activator="{ on = {}, attrs = {} }">
              <v-btn
                icon
                x-small
                @click.stop="toggleCardNumber(index)"
                class="show-number-btn"
                v-bind="attrs"
                v-on="on"
              >
                <v-icon x-small color="#4E60FF">
                  {{ showFullNumbers[index] ? 'mdi-eye-off' : 'mdi-eye' }}
                </v-icon>
              </v-btn>
            </template>
            <span>{{ showFullNumbers[index] ? 'Hide Card Number' : 'Show Card Number' }}</span>
          </v-tooltip>
        </div>
        <div class="card-holder">
          {{ card.cardHolderName }}
        </div>
        <div class="card-expiry">
          Expires {{ formatDisplayDate(card.expiryDate) }}
        </div>
      </div>
      <div class="card-actions">
        <v-tooltip top>
          <template v-slot:activator="{ on = {}, attrs = {} }">
            <v-btn
              icon
              small
              @click.stop="confirmDeleteCard(card)"
              class="delete-btn"
              v-bind="attrs"
              v-on="on"
            >
              <v-icon small color="#FF5C60">mdi-delete</v-icon>
            </v-btn>
          </template>
          <span>Delete Card</span>
        </v-tooltip>
      </div>
    </div>
    <div class="card-footer">
      <v-chip
        small
        color="#4E60FF"
        text-color="white"
        class="default-chip"
      >
        <v-icon small left>mdi-calendar</v-icon>
        Added {{ formatCreatedAt(card.createdAt) }}
      </v-chip>
      <v-spacer></v-spacer>
      <v-chip
        v-if="card.isPrimary"
        small
        color="#4CAF50"
        text-color="white"
        class="primary-chip"
      >
        <v-icon small left>mdi-star</v-icon>
        Primary
      </v-chip>
    </div>
    <div class="card-hover-effect"></div>
  </div>
</div>
    </div>
  </div>
</template>

<script setup>
import Header from '@/components/Header.vue';

import { ref, computed, onMounted } from 'vue'
import axios from '@/axios.js'
import { useToast } from 'vue-toast-notification'
import 'vue-toast-notification/dist/theme-sugar.css'

const toast = useToast()

// State
const loading = ref(false)
const saving = ref(false)
const deleting = ref(false)
const valid = ref(true)
const addCardDialog = ref(false)
const deleteDialog = ref(false)
const showBack = ref(false)
const cardToDelete = ref(null)
const cards = ref([])
const cardForm = ref(null)

const showFullNumbers = ref([])


const newCard = ref({
  cardHolderName: '',
  cardNumber: '',
  expiryDate: '',
  cvv: ''
})

const initializeShowNumbers = () => {
  showFullNumbers.value = new Array(cards.value.length).fill(false)
}


watch(cards, () => {
  initializeShowNumbers()
}, { deep: true })

const toggleCardNumber = (index) => {
  showFullNumbers.value = [...showFullNumbers.value]
  showFullNumbers.value[index] = !showFullNumbers.value[index]
}

const formatCardNumberDisplay = (cardNumber) => {
  if (!cardNumber) return ''
  const cleaned = cardNumber.replace(/\s/g, '')
  return cleaned.replace(/(\d{4})(?=\d)/g, '$1 ')
}


const cardImages = {
  visa: 'https://cdn-icons-png.flaticon.com/128/3037/3037247.png',
  mastercard: 'https://cdn-icons-png.flaticon.com/128/3037/3037247.png',
  amex: 'https://cdn-icons-png.flaticon.com/128/3037/3037247.png',
  discover: 'https://cdn-icons-png.flaticon.com/128/3037/3037247.png',
  generic: 'https://cdn-icons-png.flaticon.com/128/3037/3037247.png'
}


const rules = {
  required: value => !!value || 'Required',
  maxLength: (length, message) => v => (v && v.length <= length) || message
}


const cardLogo = computed(() => {
  const firstDigit = newCard.value.cardNumber.charAt(0)
  if (!firstDigit) return cardImages.generic

  switch(firstDigit) {
    case '4': return cardImages.visa
    case '5': return cardImages.mastercard
    case '3': return cardImages.amex
    case '6': return cardImages.discover
    default: return cardImages.generic
  }
})


const getAuthHeader = () => {
  const token = localStorage.getItem('authToken')
  return {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  }
}

const fetchCards = async () => {
  loading.value = true
  try {
    const response = await axios.get(
      '/api/cards',
      getAuthHeader()
    )

    if (response.data.success && response.data.body) {
      cards.value = response.data.body.map(card => ({
        ...card,
        cardNumber: card.cardNumber.replace(/\s/g, ''),
        expiryDate: card.expiryDate
      }))
    } else {
      throw new Error('Invalid response format')
    }
  } catch (error) {
    console.error('Error fetching cards:', error)
    handleApiError(error, 'Failed to load payment cards')
  } finally {
    loading.value = false
  }
}

const getCardLogo = (cardNumber) => {
  if (!cardNumber) return cardImages.generic

  const firstDigit = cardNumber.charAt(0)
  switch(firstDigit) {
    case '4': return cardImages.visa
    case '5': return cardImages.mastercard
    case '3': return cardImages.amex
    case '6': return cardImages.discover
    default: return cardImages.generic
  }
}

const handleApiError = (error, defaultMessage) => {
  if (error.response) {
    if (error.response.status === 401) {
      toast.error('Session expired. Please login again.', {
        position: 'top-right'
      })
      return
    }
    if (error.response.data?.message) {
      toast.error(error.response.data.message)
      return
    }
  }
  toast.error(defaultMessage || 'An error occurred')
}

const openAddCardDialog = () => {
  addCardDialog.value = true
  showBack.value = false
  newCard.value = {
    cardHolderName: '',
    cardNumber: '',
    expiryDate: '',
    cvv: ''
  }
}

const closeAddCardDialog = () => {
  addCardDialog.value = false
}

const formatCardNumber = () => {
  let value = newCard.value.cardNumber.replace(/\D/g, '')
  value = value.replace(/(\d{4})(?=\d)/g, '$1 ')
  if (value.length > 19) {
    value = value.substring(0, 19)
  }
  newCard.value.cardNumber = value
}

const formatExpiryDate = () => {
  let value = newCard.value.expiryDate.replace(/\D/g, '')
  if (value.length > 2) {
    value = value.substring(0, 2) + '/' + value.substring(2, 6)
  }
  if (value.length > 7) {
    value = value.substring(0, 7)
  }
  newCard.value.expiryDate = value
}

const formatDisplayDate = (dateString) => {
  if (!dateString) return ''

  if (dateString.includes('/')) {
    return dateString
  }

  const date = new Date(dateString)
  if (isNaN(date.getTime())) {
    return dateString
  }

  const month = date.getMonth() + 1
  const year = date.getFullYear().toString()
  return `${month.toString().padStart(2, '0')}/${year}`
}

const formatCreatedAt = (dateString) => {
  if (!dateString) return ''

  const date = new Date(dateString)
  if (isNaN(date.getTime())) return ''

  return date.toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
    year: 'numeric'
  })
}

const saveCard = async () => {
  const { valid } = await cardForm.value.validate()
  if (!valid) return

  saving.value = true
  try {
    const cardData = {
      cardHolderName: newCard.value.cardHolderName,
      cardNumber: newCard.value.cardNumber.replace(/\s/g, ''),
      expiryDate: newCard.value.expiryDate,
      cvv: newCard.value.cvv
    }

    const response = await axios.post(
      '/api/cards/create',
      cardData,
      getAuthHeader()
    )

    if (response.data.success) {
      toast.success('Card added successfully', {
        position: 'top-right'
      })
      addCardDialog.value = false
      await fetchCards()
    } else {
      throw new Error(response.data.message || 'Failed to add card')
    }
  } catch (error) {
    console.error('Error saving card:', error)
    handleApiError(error, 'Failed to add card')
  } finally {
    saving.value = false
  }
}

const confirmDeleteCard = (card) => {
  cardToDelete.value = card
  deleteDialog.value = true
}

const confirmDelete = async () => {
  if (!cardToDelete.value) return

  deleting.value = true
  try {
    await axios.delete(
      `/api/cards/${cardToDelete.value.id}`,
      getAuthHeader()
    )

    cards.value = cards.value.filter(card => card.id !== cardToDelete.value.id)
    toast.success('Card deleted successfully', {
      position: 'top-right'
    })
    deleteDialog.value = false
  } catch (error) {
    console.error('Error deleting card:', error)
    handleApiError(error, 'Failed to delete card')
  } finally {
    deleting.value = false
  }
}


onMounted(() => {
  fetchCards()
})
</script>

<style scoped>

.cards-container {

  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  margin-top: 60px;
}


.cards-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.title {
  font-size: 28px;
  font-weight: 700;
  color: #2B2B43;
  display: flex;
  align-items: center;
  gap: 16px;
}

.add-card-btn {
  border-radius: 8px;
  height: 48px;
  font-weight: 600;
  text-transform: none;
  letter-spacing: normal;
  box-shadow: 0 4px 16px rgba(78, 96, 255, 0.15);
  transition: all 0.3s ease;
}

.add-card-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(78, 96, 255, 0.25);
}

.divider {
  border-color: rgba(78, 96, 255, 0.1);
  margin-bottom: 32px;
}


.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: #F8F9FF;
  border-radius: 16px;
  animation: fadeIn 0.6s ease-out;
}

.empty-icon {
  margin-bottom: 24px;
  opacity: 0.6;
}

.empty-title {
  font-size: 20px;
  font-weight: 600;
  color: #2B2B43;
  margin-bottom: 8px;
}

.empty-subtitle {
  font-size: 16px;
  color: #545563;
  margin-bottom: 24px;
}

.empty-action {
  border-radius: 8px;
  height: 48px;
  font-weight: 600;
  text-transform: none;
  letter-spacing: normal;
}


.cards-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(700px, 1fr));
  gap: 24px;
  animation: fadeIn 0.6s ease-out;
}

.card-item {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(78, 96, 255, 0.08);
  transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
  border: 1px solid rgba(237, 238, 242, 0.8);
  position: relative;
  z-index: 1;
}

.card-item:hover {
  transform: translateY(-8px);
  box-shadow: 0 15px 35px rgba(78, 96, 255, 0.15);
  border-color: rgba(78, 96, 255, 0.3);
}

.card-item.primary-card {
  border: 2px solid rgba(76, 175, 80, 0.3);
  box-shadow: 0 10px 30px rgba(76, 175, 80, 0.1);
}

.card-item.primary-card:hover {
  box-shadow: 0 15px 35px rgba(76, 175, 80, 0.15);
}

.card-item-content {
  padding: 24px;
  display: flex;
  gap: 16px;
  align-items: center;
  position: relative;
  z-index: 2;
  background: white;
}

.card-brand {
  width: 64px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: white;
  border-radius: 6px;
  padding: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.card-logo-img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  filter: drop-shadow(0 2px 4px rgba(0,0,0,0.1));
  transition: transform 0.3s ease;
}

.card-item:hover .card-logo-img {
  transform: scale(1.1);
}

.card-details {
  flex-grow: 1;
  min-width: 0;
}

.card-number-container {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px;
}

.card-number {
  font-size: 16px;
  font-weight: 600;
  color: #2B2B43;
  margin-bottom: 6px;
  letter-spacing: 0.5px;
  font-family: 'Courier New', monospace;
  position: relative;
  display: inline-block;
}

.card-number::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 2px;
  background: #4E60FF;
  transition: width 0.3s ease;
}

.card-item:hover .card-number::after {
  width: 100%;
}

.card-holder {
  font-size: 14px;
  color: #545563;
  margin-bottom: 4px;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  font-weight: 500;
  margin-right: 5px;
}

.card-expiry {
  font-size: 13px;
  color: #A2A5B5;
  font-weight: 500;
}

.card-actions {
  margin-left: auto;
  display: flex;
  align-items: center;
}

.show-number-btn {
  transition: all 0.3s ease;
  background: transparent !important;
  border-radius: 50%;
  width: 24px;
  height: 24px;
  margin-left: 4px;
}

.show-number-btn:hover {
  transform: scale(1.15);
  background: rgba(78, 96, 255, 0.1) !important;
}
.delete-btn {
  transition: all 0.3s ease;
  background: rgba(255, 92, 96, 0.1) !important;
  border-radius: 50%;
  width: 32px;
  height: 32px;
}

.delete-btn:hover {
  transform: scale(1.1);
  background: rgba(255, 92, 96, 0.2) !important;
}

.card-footer {
  padding: 14px 24px;
  background: linear-gradient(to right, #F8F9FF, #FFFFFF);
  display: flex;
  align-items: center;
  position: relative;
  z-index: 2;
  border-top: 1px solid rgba(237, 238, 242, 0.8);
}

.default-chip {
  font-weight: 500;
  font-size: 12px;
  box-shadow: 0 2px 4px rgba(78, 96, 255, 0.1);
}

.primary-chip {
  font-weight: 500;
  font-size: 12px;
  box-shadow: 0 2px 4px rgba(76, 175, 80, 0.1);
}

.card-hover-effect {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(78, 96, 255, 0.03) 0%, rgba(139, 84, 255, 0.03) 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
  z-index: 1;
}

.card-item:hover .card-hover-effect {
  opacity: 1;
}

/* Add a subtle shine effect on hover */
.card-item::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(
    to bottom right,
    rgba(255, 255, 255, 0) 0%,
    rgba(255, 255, 255, 0) 45%,
    rgba(255, 255, 255, 0.5) 50%,
    rgba(255, 255, 255, 0) 55%,
    rgba(255, 255, 255, 0) 100%
  );
  transform: rotate(30deg);
  opacity: 0;
  transition: opacity 0.5s ease;
  pointer-events: none;
}

.card-item:hover::before {
  opacity: 0.6;
  animation: shine 1.5s ease forwards;
}

@keyframes shine {
  0% {
    transform: rotate(30deg) translate(-30%, -30%);
  }
  100% {
    transform: rotate(30deg) translate(30%, 30%);
  }
}

/* Responsive adjustments */
@media (max-width: 960px) {
  .cards-grid {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  }
}

@media (max-width: 600px) {
  .cards-grid {
    grid-template-columns: 1fr;
  }

  .card-item-content {
    padding: 20px;
  }

  .card-footer {
    padding: 12px 20px;
  }
}

/* Dialog Styles */
.add-card-dialog, .delete-dialog {
  border-radius: 16px !important;
  overflow: hidden;
}

.dialog-title {
  font-size: 20px;
  font-weight: 600;
  padding: 24px;
  border-bottom: 1px solid #EDEEF2;
  display: flex;
  align-items: center;
}

.dialog-content {
  padding: 24px;
}

.dialog-actions {
  padding: 16px 24px;
  border-top: 1px solid #EDEEF2;
}

.cancel-btn {
  color: #545563 !important;
  font-weight: 500;
  text-transform: none;
  letter-spacing: normal;
}

.save-btn, .confirm-delete-btn {
  font-weight: 600;
  text-transform: none;
  letter-spacing: normal;
  height: 48px;
  border-radius: 8px;
}


.card-preview {
  margin-top: 32px;
  perspective: 1000px;
}

.credit-card {
  width: 100%;
  max-width: 350px;
  height: 220px;
  margin: 0 auto;
  position: relative;
  transform-style: preserve-3d;
  transition: transform 0.6s;
  cursor: pointer;
}

.credit-card.flipped {
  transform: rotateY(180deg);
}

.card-front, .card-back {
  position: absolute;
  width: 100%;
  height: 100%;
  backface-visibility: hidden;
  border-radius: 16px;
  padding: 24px;
  box-sizing: border-box;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.card-front {
  background: linear-gradient(135deg, #4E60FF 0%, #8B54FF 100%);
  color: white;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.card-back {
  background: linear-gradient(135deg, #8B54FF 0%, #4E60FF 100%);
  transform: rotateY(180deg);
}

.card-logo {
  text-align: right;
}

.card-logo img {
  height: 40px;
  filter: brightness(0) invert(1);
}

.card-number {
  font-size: 20px;
  letter-spacing: 2px;
  text-align: center;
  margin: 20px 0;
  font-family: 'Courier New', monospace;
  font-weight: 500;
  text-shadow: 0 1px 2px rgba(0,0,0,0.2);
}

.card-details {
  display: flex;
  justify-content: space-between;
}

.card-holder, .card-expiry {
  font-size: 14px;
}

.label {
  font-size: 10px;
  opacity: 0.8;
  margin-bottom: 4px;
  text-transform: uppercase;
}

.value {
  font-size: 16px;
  font-weight: 500;
}

.card-strip {
  height: 40px;
  background: #2B2B43;
  margin: 0 -24px;
  margin-top: 24px;
}

.card-cvv {
  margin-top: 20px;
  background: white;
  color: #2B2B43;
  padding: 8px;
  border-radius: 4px;
  text-align: right;
  font-family: 'Courier New', monospace;
  font-weight: 600;
}

.flip-btn {
  margin-top: 16px;
  text-transform: none;
  letter-spacing: normal;
}


.card-preview-small {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-top: 20px;
  padding: 16px;
  background: #F8F9FF;
  border-radius: 8px;
}

.card-brand {
  width: 48px;
  height: 32px;
}

.card-brand img {
  max-width: 100%;
  max-height: 100%;
}

.card-info {
  flex-grow: 1;
}

.card-number-masked {
  font-weight: 600;
  color: #2B2B43;
  margin-bottom: 4px;
  font-family: 'Courier New', monospace;
}

.card-holder {
  font-size: 14px;
  color: #545563;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
  animation: fadeIn 0.6s ease-out;
}

.loading-container p {
  margin-top: 16px;
  color: #545563;
}


@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}


@media (max-width: 960px) {
  .cards-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .add-card-btn {
    width: 100%;
  }
}

@media (max-width: 600px) {
  .cards-container {
    padding: 20px 10px;
  }

  .title {
    font-size: 24px;
  }

  .cards-grid {
    grid-template-columns: 1fr;
  }

  .credit-card {
    height: 200px;
  }

  .dialog-content {
    padding: 16px;
  }
}
</style>
