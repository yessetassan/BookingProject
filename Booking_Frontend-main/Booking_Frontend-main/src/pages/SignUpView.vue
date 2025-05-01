<template>
  <Header />

  <v-container class="register-container mt-15">
    <v-row justify="center">
      <v-col cols="12" md="6" lg="5">
        <v-card class="register-card" elevation="5">
          <v-card-title class="text-center text-h4 mb-2" style="color: #25409C;">{{$t('signup')}}</v-card-title>
          <v-card-subtitle class="text-center mb-6">{{$t('signup_shablon')}}</v-card-subtitle>

          <v-form @submit.prevent="signUp" ref="form">
            <v-text-field
              v-model="email"
              :label="$t('login_email')"
              placeholder="example@email.com"
              outlined
              dense
              class="mb-4 custom-input"
              :rules="emailRules"
              required
            />

            <v-text-field
              v-model="password"
              :label="$t('login_password')"
              placeholder="●●●●●●●●"
              outlined
              dense
              :type="showPassword ? 'text' : 'password'"
              class="mb-4 custom-input"
              :rules="passwordRules"
              required
              :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
              @click:append-inner="showPassword = !showPassword"
            />

            <v-text-field
              v-model="firstName"
              :label="$t('signup_firstname')"
              placeholder="John"
              outlined
              dense
              class="mb-4 custom-input"
              :rules="nameRules"
              required
            />

            <v-text-field
              v-model="lastName"
              :label="$t('signup_lastname')"
              placeholder="Doe"
              outlined
              dense
              class="mb-4 custom-input"
              :rules="nameRules"
              required
            />

            <v-btn
              color="#25409C"
              block
              large
              type="submit"
              class="text-none font-weight-bold"
              style="height: 45px; font-size: 20px;"
              :loading="loading"
            >
              {{$t('signup_signup')}}
            </v-btn>
          </v-form>

          <div class="mt-3 text-center">
            <span class="text-caption">{{$t('signup_account')}} <router-link to="/login" style="color: #25409C;">{{ $t('login_sign_in') }}</router-link></span>
            <v-divider class="my-4"></v-divider>
            <!-- <span class="text-caption mt-2">Or continue with</span> <br>
            <img
              src="/src/assets/google-icon.png"
              class="cursor-pointer mt-2"
              :width="30"
              alt="Google"
              @click="signInWithGoogle"
            > -->
          </div>
        </v-card>
      </v-col>
    </v-row>

    <!-- Success Dialog -->
    <v-dialog v-model="successDialog" max-width="450" persistent>
      <v-card class="success-dialog">
        <v-card-text class="text-center pa-6">
          <v-icon color="success" size="80" class="mb-4">mdi-check-circle-outline</v-icon>
          <h2 class="text-h5 mb-2" style="color: #4CAF50;">Welcome, {{ firstName }}!</h2>
          <p class="mb-4">Your account has been successfully created.</p>
          <v-progress-linear
            color="primary"
            indeterminate
            height="6"
            class="mb-4"
          ></v-progress-linear>
          <p class="text-caption">You're being logged in automatically...</p>
        </v-card-text>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script setup>
import Header from '@/components/Header.vue'
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const router = useRouter()
const auth = useAuthStore()

const firstName = ref('')
const lastName = ref('')
const email = ref('')
const password = ref('')
const loading = ref(false)
const successDialog = ref(false)
const form = ref(null)
const showPassword = ref(false)

const emailRules = [
  v => !!v || 'Email is required',
  v => /.+@.+\..+/.test(v) || 'Email must be valid',
]

const passwordRules = [
  v => !!v || 'Password is required',
  v => (v && v.length >= 6) || 'Password must be at least 6 characters',
]

const nameRules = [
  v => !!v || 'Name is required',
  v => (v && v.length >= 2) || 'Name must be at least 2 characters',
]

const signUp = async () => {
  const { valid } = await form.value.validate()

  if (!valid) return

  loading.value = true

  try {
    
    await auth.register({
      email: email.value,
      password: password.value,
      firstName: firstName.value,
      lastName: lastName.value
    })

   
    const success = await auth.login(email.value, password.value)

    if (success) {
      successDialog.value = true
      setTimeout(() => {
        router.push('/')
      }, 3000)
    }
  } catch (error) {
    alert(error.response?.data?.message || 'Registration failed. Please try again.')
  } finally {
    loading.value = false
  }
}

const signInWithGoogle = () => {
  console.log('Signing in with Google')
}
</script>

<style scoped>
.register-container {
  min-height: 80vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

.register-card {
  padding: 32px;
  border-radius: 12px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.custom-input {
  border-radius: 8px;
}

.v-card-title {
  font-weight: 700;
  letter-spacing: 0.5px;
}

.v-card-subtitle {
  color: #666;
}

.success-dialog {
  border-radius: 12px;
  overflow: hidden;
}
</style>
