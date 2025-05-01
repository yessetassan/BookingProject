<template>
  <Header />

  <v-container class="login-container mt-15">
    <v-row justify="center">
      <v-col cols="12" md="6" lg="5">
        <v-card class="login-card">
          <v-card-title class="text-center text-h4 mb-2" style="color: #25409C;">{{$t('login')}}</v-card-title>
          <v-card-subtitle class="text-center text-body-1 mb-6">{{$t('login_shablon')}}</v-card-subtitle>

          <v-form @submit.prevent="signIn">
            <v-text-field
              v-model="email"
              label="Email"
              :placeholder="$t('login_email')"
              outlined
              dense
              class="mb-4"
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

            <!-- <v-row align="center" class="mb-6">
              <v-col cols="8">
                <v-checkbox
                  v-model="rememberMe"
                  hide-details
                >
                  <template #label>
                    <span class="text-caption">Remember for 30 days</span>
                  </template>
                </v-checkbox>
              </v-col>
              <v-col cols="4" class="text-right">
                <a class="text-caption">Forgot password?</a>
              </v-col>
            </v-row> -->

            <v-btn
              color="#25409C"
              block
              large
              type="submit"
              class="text-none font-weight-bold"
              height="40px"
              :loading="loading"
            >
            {{$t('login_sign_in')}}
            </v-btn>
          </v-form>

          <v-divider class="my-6"></v-divider>

          <!-- <v-btn
            outlined
            block
            style="color: #25409C;"
            class="text-none font-weight-bold"
            @click="signInWithGoogle"
            height="40px"
          >
            <img src="/src/assets/google-icon.png" :width="20" class="mr-2" alt="">
            Sign in with Google
          </v-btn> -->

          <div class="text-center mt-6">
            <span class="text-caption">Don't have an account?</span>
            <a @click="navigateToSignUpPage()" class="text-caption ml-1">Sign up</a>
          </div>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import Header from '@/components/Header.vue'
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const router = useRouter()
const auth = useAuthStore()

const email = ref('theyersultan.b@gmail.com')
const password = ref('123456')
const rememberMe = ref(false)
const loading = ref(false)
const showPassword = ref(false)

const emailRules = [
  v => !!v || 'Email is required',
  v => /.+@.+\..+/.test(v) || 'Email must be valid',
]

const passwordRules = [
  v => !!v || 'Password is required',
  v => (v && v.length >= 6) || 'Password must be at least 6 characters',
]

const signIn = async () => {
  loading.value = true
  try {
    const success = await auth.login(email.value, password.value)
    if (success) {
      router.push('/')
    } else {
      alert('Login failed. Please check your credentials.')
    }
  } finally {
    loading.value = false
  }
}

const signInWithGoogle = () => {
  console.log('Signing in with Google')
}

const navigateToSignUpPage = () => {
  router.push({ path: '/sign-up' })
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-card {
  padding: 24px;
  border-radius: 8px;
}

a {
  text-decoration: none;
  color: #25409C;
  font-weight: bold;
}

a:hover {
  text-decoration: underline;
  cursor: pointer;
}
</style>
