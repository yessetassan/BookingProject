import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import axios from "@/axios.js";

import router from '@/router'


export const useAuthStore = defineStore('auth', () => {
  const user = ref(JSON.parse(localStorage.getItem('authUser'))) || null
  const token = ref(localStorage.getItem('authToken') || null)
  const isLoading = ref(false)

  const isAuthenticated = computed(() => !!token.value)
  const userId = computed(() => user.value?.id || null)
  const userInitials = computed(() => {
    if (!user.value) return ''
    return `${user.value.firstName?.[0] || ''}${user.value.lastName?.[0] || ''}`.toUpperCase()
  })

  async function login(email, password) {
    try {
      isLoading.value = true
      const response = await axios.post('/open-api/auth/sign-in', {
        email,
        password
      })

      if (response.data.success) {
        token.value = response.data.body.token
        localStorage.setItem('authToken', token.value)


        
        user.value = {
          id: response.data.body.userId,
          email: email,
          firstName: response.data.body.firstName || email.split('@')[0],
          lastName: response.data.body.lastName || '',
          role: response.data.body.role || 'ROLE_USER',
          section: response.data.body.section
        }

        localStorage.setItem('authUser', JSON.stringify(user.value))


        return true
      }
      return false
    } catch (error) {
      console.error('Login failed:', error)
      return false
    } finally {
        isLoading.value = false
     }
  }

  async function register(userData) {
    try {
      isLoading.value = true
      const response = await axios.post('/open-api/auth/sign-up', userData)
      return response.data
    } catch (error) {
      console.error('Registration failed:', error)
      throw error
    } finally {
      isLoading.value = false
    }
  }

  function logout() {
    user.value = null
    token.value = null
    localStorage.removeItem('authToken')
    localStorage.removeItem('authUser')
    router.push('/login')
  }

  return {
    user,
    token,
    isAuthenticated,
    userId,
    userInitials,
    isLoading,
    login,
    register,
    logout
  }
})
