<template>
  <v-app-bar
    class="px-4 px-sm-10"
    :color="$vuetify.theme.current.dark ? 'grey-darken-4' : 'primary'"
    :elevation="4"
    :height="60"
  >
    <!-- Logo/Brand Section -->
    <router-link to="/" class="d-flex align-center text-decoration-none">
      <v-avatar size="50" class="mr-3" rounded="lg">
        <v-img style="border-radius: 25px;"
          src="/src/assets/photo_5366421479140485508_y.jpg"
          alt="Booking Platform Logo"
        />
      </v-avatar>
      <h3 class="text-white font-weight-bold text-h5 text-sm-h7">
        Nova Rent Booking
      </h3>
    </router-link>

    <v-spacer></v-spacer>


    <Language/>
    <!-- Desktop Navigation -->
    <div class="d-none d-md-flex align-center">
      <template v-if="auth.isAuthenticated">
        <v-btn
          v-if="auth.user?.role === 'ROLE_USER'"
          to="/add-item"
          class="mx-1"
          color="white"
          variant="text"
          prepend-icon="mdi-plus-circle"
        >
        {{ $t('header_add_products') }}
        </v-btn>

        <v-btn
          v-if="auth.user?.role === 'ROLE_USER'"
          to="/favorite-view"
          class="mx-1"
          color="white"
          variant="text"
          prepend-icon="mdi-heart"
        >
        {{ $t('header_favorites') }}
        </v-btn>

        <v-menu>
          <template v-slot:activator="{ props }">
            <v-btn
              color="white"
              variant="text"
              v-bind="props"
              class="text-none ml-2"
              prepend-icon="mdi-account-circle"
            >
              {{ auth.user?.firstName || auth.user?.email }}
              <v-icon>mdi-chevron-down</v-icon>
            </v-btn>
          </template>
          <v-list min-width="200">
            <v-list-item
              to="/my-items"
              v-if="auth.user?.role === 'ROLE_USER'"
              prepend-icon="mdi-format-list-bulleted"
            >
              <v-list-item-title>{{ $t('header_my_items') }}</v-list-item-title>
            </v-list-item>
            <v-list-item
              to="/my-payment-cards"
              v-if="auth.user?.role === 'ROLE_USER'"
              prepend-icon="mdi-format-list-bulleted"
            >
              <v-list-item-title>{{ $t('header_my_payment_cards') }}</v-list-item-title>
            </v-list-item>
            <v-divider></v-divider>
            <v-list-item
              @click="auth.logout"
              prepend-icon="mdi-logout"
              color="error"
            >
              <v-list-item-title>{{ $t('header_logout') }}</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </template>

      <template v-else>
        <v-btn
          @click="navigateToSignUpPage()"
          class="mx-1"
          color="white"
          variant="outlined"
          prepend-icon="mdi-account-plus"
        >
        {{ $t('header_sign_up') }}
        </v-btn>
        <v-btn
          @click="navigateToSignInPage()"
          class="mx-1"
          color="white"
          variant="flat"
          prepend-icon="mdi-login"
        >
        {{ $t('header_login') }}
        </v-btn>
      </template>
    </div>

    <!-- Mobile Navigation -->
    <div class="d-flex d-md-none">
      <v-menu location="bottom">
        <template v-slot:activator="{ props }">
          <v-btn
            icon
            v-bind="props"
            color="white"
            size="large"
          >
            <v-icon>mdi-menu</v-icon>
          </v-btn>
        </template>
        <v-list min-width="200">
          <template v-if="auth.isAuthenticated">

            <v-list-item
              v-if="auth.user?.role === 'ROLE_USER'"
              to="/add-item"
              prepend-icon="mdi-plus-circle"
            >
              <v-list-item-title>{{ $t('header_add_products') }}</v-list-item-title>
            </v-list-item>
            <v-list-item
              v-if="auth.user?.role === 'ROLE_USER'"
              to="/favorite-view"
              prepend-icon="mdi-heart"
            >
              <v-list-item-title>{{ $t('header_favorites') }}</v-list-item-title>
            </v-list-item>
            <v-list-item
              v-if="auth.user?.role === 'ROLE_USER'"
              to="/my-items"
              prepend-icon="mdi-format-list-bulleted"
            >
              <v-list-item-title>{{ $t('header_my_items') }}</v-list-item-title>
            </v-list-item>
            <v-divider></v-divider>
            <v-list-item
              prepend-icon="mdi-account-circle"
            >
              <v-list-item-title>{{ auth.user?.firstName || auth.user?.email }}</v-list-item-title>
            </v-list-item>
            <v-list-item
              @click="auth.logout"
              prepend-icon="mdi-logout"
              color="error"
            >
              <v-list-item-title>{{ $t('header_logout') }}</v-list-item-title>
            </v-list-item>
          </template>
          <template v-else>
            <v-list-item
              @click="navigateToSignUpPage()"
              prepend-icon="mdi-account-plus"
            >
              <v-list-item-title>{{ $t('header_sign_up') }}</v-list-item-title>
            </v-list-item>
            <v-list-item
              @click="navigateToSignInPage()"
              prepend-icon="mdi-login"
            >
              <v-list-item-title>{{ $t('header_login') }}</v-list-item-title>
            </v-list-item>
          </template>
        </v-list>
      </v-menu>
    </div>
  </v-app-bar>
</template>

<script setup>
import Language from '../components/Language.vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const router = useRouter()
const auth = useAuthStore()

const navigateToSignInPage = () => {
  router.push({ path: '/login' })
}

const navigateToSignUpPage = () => {
  router.push({ path: '/sign-up' })
}
</script>

<style scoped>
.v-app-bar {
  transition: all 0.3s ease;
  background: linear-gradient(135deg, var(--v-primary-base), var(--v-primary-darken-2));
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.12), 0 7px 10px -5px rgba(0, 0, 0, 0.2) !important;
}

.v-btn {
  letter-spacing: 0.5px;
  font-weight: 500;
  transition: all 0.2s ease;
}

.v-btn:hover {
  transform: translateY(-1px);
}

.v-list-item {
  border-radius: 8px;
  margin: 4px 8px;
}

.v-list-item:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

@media (max-width: 600px) {
  .v-app-bar {
    height: 70px !important;
    padding-left: 12px !important;
    padding-right: 12px !important;
  }

  .v-avatar {
    width: 44px !important;
    height: 44px !important;
  }

  h3 {
    font-size: 1.25rem !important;
  }
}
</style>
