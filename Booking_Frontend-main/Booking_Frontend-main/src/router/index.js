import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
const routes = [
  {
    path: '/',
    name: 'home-view',
    beforeEnter: (to, from, next) => {
      const auth = useAuthStore()
      if (auth.isAuthenticated && auth.user?.role === 'ROLE_ADMIN') {
        next('/admin-main-view')
      } else {
        next()
      }
    },
    component: () => import('@/pages/Home.vue'),
  },
  {
    path: '/login',
    name: 'login-view',
    component: () => import('@/pages/SignInView.vue')
  },
  {
    path: '/sign-up',
    name: 'sign-up-view',
    component: () => import('@/pages/SignUpView.vue')
  },
  {
    path: '/card-detail/:id',
    name: 'card-detail',
    component: () => import('@/pages/CardDetail.vue'),
    props: true
  },
  {
    path: '/favorite-view',
    name: 'favorite-view',
    component: () => import('@/pages/FavoriteView.vue'),
    meta: { requiresAuth: true }
  },

  {
    path: '/add-item',
    name: 'add-item',
    component: () => import('@/pages/AddCardView.vue'),
    meta: {requiresAuth: true, allowedRoles: ['ROLE_USER']}
  },

  {
    path: '/admin-main-view',
    name: 'admin-main-view',
    component: () => import('@/pages/admin/MainView.vue'),
    meta: { requiresAuth: true, allowedRoles: ['ROLE_ADMIN'] }

  },

  {
    path: '/approved-items',
    name: 'approved-items',
    component: () => import('@/pages/admin/ApprovedItems.vue'),
    meta: {requiresAuth: true, allowedRoles: ['ROLE_ADMIN']}

  },
  {
    path: '/rejected-items',
    name: 'rejected-items',
    component: () => import('@/pages/admin/RejectedItems.vue'),
    meta: {requiresAuth: true, allowedRoles: ['ROLE_ADMIN']}

  },

  {
    path: '/list-items',
    name: 'list-items',
    component: () => import('@/pages/admin/ListItemsCheckView.vue')
  },
  {
    path: '/check-items/:id',
    name: 'check-items',
    component: () => import('@/pages/admin/CheckItemview.vue'),
    // meta: {requiresAuth: true, allowedRoles: ['ROLE_ADMIN']}
  },
  {
    path: '/my-check-items/:id',
    name: 'my-check-items',
    component: () => import('@/pages/CheckItemWithID.vue'),

    // meta: {requiresAuth: true, allowedRoles: ['ROLE_ADMIN']}
  },
  {
    path: '/complaints-items',
    name: 'complaints-items',
    component: () => import('@/pages/admin/ComplaintsView.vue'),
    meta: {requiresAuth: true, allowedRoles: ['ROLE_ADMIN']}

  },

  {
    path: '/my-items',
    name: 'my-items',
    component: () => import('@/pages/user/UserItemsView.vue'),
    meta: {requiresAuth: true, allowedRoles: ['ROLE_USER']}
  },
  {
    path: '/approved-my-items',
    name: 'approved-my-items',
    component: () => import('@/pages/user/ApprovedItems.vue'),
    meta: {requiresAuth: true, allowedRoles: ['ROLE_USER']}
  },
  {
    path: '/rejected-my-items',
    name: 'rejected-my-items',
    component: () => import('@/pages/user/RejectedItems.vue'),
    meta: {requiresAuth: true, allowedRoles: ['ROLE_USER']}
  },
  {
    path: '/list-my-items',
    name: 'list-my-items',
    component: () => import('@/pages/user/ListItemsCheckView.vue'),
    meta: {requiresAuth: true, allowedRoles: ['ROLE_USER']}

  },
  {
    path: '/payment',
    name: 'payment',
    component: () => import('@/pages/user/PaymentView.vue')
  },
  {
    path: '/my-payment-cards',
    name: 'my-payment-cards',
    component: () => import('@/pages/user/MyPaymentCards.vue')
  },
  {
    path: '/payment-history',
    name: 'payment-history',
    component: () => import('@/pages/user/PaymentHistory.vue')
  },
  {
    path: '/booking-now',
    name: 'booking-now',
    component: () => import('@/pages/user/BookingView.vue')
  },
  {
    path: '/booking-history',
    name: 'booking-history',
    component: () => import('@/pages/user/BookingHistoryView.vue')
  },
  {
    path: '/owner-booking-history',
    name: 'owner-booking-history',
    component: () => import('@/pages/user/OwnerBookingHistory.vue')
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

router.beforeEach((to, from, next) => {
  const auth = useAuthStore()

  const requiresAuth = to.meta.requiresAuth
  const allowedRoles = to.meta.allowedRoles

  if(requiresAuth && !auth.isAuthenticated) {
    next({ path: '/login'})
  } else if (allowedRoles && !allowedRoles.includes(auth.user?.role)) {
    next({path: '/'})
  } else {
    next()
  }
})

export default router
