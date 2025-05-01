<template>
   <Header/>
  <v-app>

<!--
    <v-app-bar color="primary" density="compact" elevation="1" height="80">
      <template v-slot:prepend>
        <v-avatar color="white" class="mr-3 ml-5">
          <v-img src="/Users/ersultan/Desktop/Diploma Project/Booking_Frontend/src/assets/photo_5366421479140485508_y.jpg"></v-img>
        </v-avatar>
      </template>

      <v-app-bar-title class="text-white font-weight-bold">Admin Dashboard</v-app-bar-title>

      <v-spacer></v-spacer>

      <v-menu v-model="notificationMenu" :close-on-content-click="false" offset-y>
        <template v-slot:activator="{ props }">
          <v-badge
            :content="unreadNotifications.length"
            color="error"
            overlap
            offset-x="6"
            offset-y="6"
          >
            <v-btn icon v-bind="props">
              <v-icon color="white">mdi-bell</v-icon>
            </v-btn>
          </v-badge>
        </template>

        <v-card width="400">
          <v-toolbar color="primary" density="compact">
            <v-toolbar-title class="text-white">Messages</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn icon @click="markAllAsRead" variant="text" color="white">
              <v-icon>mdi-check-all</v-icon>
              <v-tooltip activator="parent">Mark all as read</v-tooltip>
            </v-btn>
          </v-toolbar>

          <v-list>
            <template v-if="notifications.length > 0">
              <v-list-item
                v-for="notification in notifications"
                :key="notification.id"
                :class="{ 'unread-notification': !notification.read }"
                @click="openChat(notification)"
              >
                <template v-slot:prepend>
                  <v-avatar :color="getUserColor(notification.sender)" size="40">
                    <v-img v-if="notification.avatar" :src="notification.avatar"></v-img>
                    <span v-else class="text-white">{{ getUserInitials(notification.sender) }}</span>
                  </v-avatar>
                </template>

                <v-list-item-title class="font-weight-medium">
                  {{ notification.sender }}
                </v-list-item-title>
                <v-list-item-subtitle>
                  {{ notification.message }}
                </v-list-item-subtitle>
                <v-list-item-subtitle class="text-caption text-grey">
                  {{ formatTime(notification.time) }}
                </v-list-item-subtitle>

                <template v-slot:append>
                  <v-icon v-if="!notification.read" color="primary">mdi-circle-small</v-icon>
                </template>
              </v-list-item>
            </template>

            <v-list-item v-else>
              <v-list-item-title>No messages</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-card>
      </v-menu>

      <v-menu>
        <template v-slot:activator="{ props }">
          <v-btn icon v-bind="props">
            <v-icon color="white">mdi-account-circle</v-icon>
          </v-btn>
        </template>
        <v-list>
          <v-list-item
            v-for="(item, index) in profileMenu"
            :key="index"
            :value="index"
          >
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </v-app-bar> -->

    <!-- Main Content -->
    <v-main class="main-content">
      <v-container fluid class="pa-6">

        <v-row>
          <v-col cols="12">
            <v-card class="welcome-card" elevation="4">
              <v-card-text class="d-flex align-center">
                <v-avatar color="primary" size="60" class="mr-4">
                  <v-icon color="white" size="30">mdi-account-tie</v-icon>
                </v-avatar>
                <div>
                  <h2 class="text-h5 font-weight-bold">Welcome back, Admin {{authStore.user?.section}}!</h2>
                  <p class="text-subtitle-1 text-grey-darken-1">Here's what's happening with your platform today</p>
                </div>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>

        <!-- Dashboard Cards -->

        <v-row class="mt-4">
          <v-col cols="12" sm="6" md="3">
            <v-card
              class="dashboard-card"
              elevation="2"
              @click="navigateTo('approved')"
            >
              <v-card-text>
                <div class="d-flex justify-space-between align-center">
                  <div>
                    <h3 class="text-h6 text-grey-darken-3">Approved</h3>
                    <h2 class="text-h3 font-weight-bold text-primary">
                      <template v-if="loading">...</template>
                      <template v-else>{{ counts.approved.toLocaleString() }}</template>
                    </h2>

                  </div>
                  <v-avatar color="primary-lighten-5" size="56">
                    <v-icon color="primary" size="30">mdi-checkbox-marked-circle</v-icon>
                  </v-avatar>
                </div>
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" sm="6" md="3">
            <v-card
              class="dashboard-card"
              elevation="2"
              @click="navigateTo('rejected')"
            >
              <v-card-text>
                <div class="d-flex justify-space-between align-center">
                  <div>
                    <h3 class="text-h6 text-grey-darken-3">Rejected</h3>
                    <h2 class="text-h3 font-weight-bold text-error">
                      <template v-if="loading">...</template>
                      <template v-else>{{ counts.rejected.toLocaleString() }}</template>
                    </h2>

                  </div>
                  <v-avatar color="red-lighten-5" size="56">
                    <v-icon color="error" size="30">mdi-close-circle</v-icon>
                  </v-avatar>
                </div>
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" sm="6" md="3">
            <v-card class="dashboard-card" elevation="2" @click="navigateTo('list')">
              <v-card-text>
                <div class="d-flex justify-space-between align-center">
                  <div>
                    <h3 class="text-h6 text-grey-darken-3">Checklist</h3>
                    <h2 class="text-h3 font-weight-bold text-indigo-darken-2">
                      <template v-if="loading">...</template>
                      <template v-else>{{ counts.checklist.toLocaleString() }}</template>
                    </h2>

                  </div>
                  <v-avatar color="indigo-lighten-5" size="56">
                    <v-icon color="indigo-darken-2" size="30">mdi-format-list-checks</v-icon>
                  </v-avatar>
                </div>
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" sm="6" md="3">
            <v-card class="dashboard-card" elevation="2" @click="navigateTo('complaints')">
              <v-card-text>
                <div class="d-flex justify-space-between align-center">
                  <div>
                    <h3 class="text-h6 text-grey-darken-3">Complaints</h3>
                    <h2 class="text-h3 font-weight-bold text-orange-darken-2">
                      <template v-if="loading">...</template>
                      <template v-else>{{ counts.complaints.toLocaleString() }}</template>
                    </h2>

                  </div>
                  <v-avatar color="orange-lighten-5" size="56">
                    <v-icon color="orange-darken-2" size="30">mdi-alert-circle</v-icon>
                  </v-avatar>
                </div>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>

      </v-container>
    </v-main>

    <!-- Chat Panel -->
    <v-navigation-drawer
      v-model="chatOpen"
      location="right"
      width="400"
      temporary
    >
      <v-toolbar color="primary" density="compact">
        <div class="d-flex align-center">
          <v-avatar :color="currentChatUser.color" size="40" class="mr-2">
            <v-img v-if="currentChatUser.avatar" :src="currentChatUser.avatar"></v-img>
            <span v-else class="text-white">{{ currentChatUser.initials }}</span>
          </v-avatar>
          <v-toolbar-title>{{ currentChatUser.name }}</v-toolbar-title>
        </div>
        <v-spacer></v-spacer>
        <v-btn icon @click="closeChat">
          <v-icon color="white">mdi-close</v-icon>
        </v-btn>
      </v-toolbar>

      <div class="chat-container pa-4">
        <template v-if="currentChatUser.messages.length > 0">
          <div
            v-for="(message, index) in currentChatUser.messages"
            :key="index"
            :class="['message', message.sender === 'me' ? 'me' : 'them']"
          >
            <div v-if="message.sender === 'them'" class="message-sender">
              <v-avatar :color="currentChatUser.color" size="24" class="mr-1">
                <v-img v-if="currentChatUser.avatar" :src="currentChatUser.avatar"></v-img>
                <span v-else class="text-white">{{ currentChatUser.initials }}</span>
              </v-avatar>
              <span class="text-caption">{{ currentChatUser.name }}</span>
            </div>
            <div class="message-content">{{ message.text }}</div>
            <div class="message-time">{{ message.time }}</div>
          </div>
        </template>
        <v-alert
          v-else
          type="info"
          variant="tonal"
          class="ma-4"
        >
          No messages yet. Start the conversation!
        </v-alert>
      </div>

      <v-divider></v-divider>

      <div class="pa-3">
        <v-text-field
          v-model="newMessage"
          placeholder="Type your message..."
          outlined
          dense
          hide-details
          append-inner-icon="mdi-send"
          @click:append-inner="sendMessage"
          @keyup.enter="sendMessage"
        ></v-text-field>
      </div>
    </v-navigation-drawer>
  </v-app>
</template>

<script setup>
import Header from '@/components/Header.vue';
import { ref, computed, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import useDashboardCounts from '@/composables/useDashboardCounts';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();

const { counts, loading, stopAutoRefresh } = useDashboardCounts();


onUnmounted(() => {
  stopAutoRefresh();
});
const router = useRouter();
const notificationMenu = ref(false);
const chatOpen = ref(false);
const newMessage = ref('');


const notifications = ref([
  {
    id: 1,
    sender: 'Yersultan Bakbergen',
    avatar: 'https://randomuser.me/api/portraits/men/1.jpg',
    message: 'Request for product approval',
    time: new Date(Date.now() - 1000 * 60 * 5),
    read: false,
    status: 'active',
    messages: [
      { text: 'Hello, I need approval for my product listing', sender: 'them', time: '10:30 AM' },
      { text: 'Sure, what product is it?', sender: 'me', time: '10:32 AM' }
    ]
  },
  {
    id: 2,
    sender: 'Yesset Assan',
    avatar: 'https://randomuser.me/api/portraits/men/2.jpg',
    message: 'Complaint about late delivery',
    time: new Date(Date.now() - 1000 * 60 * 30), // 30 minutes ago
    read: false,
    status: 'active',
    messages: [
      { text: 'My order was delivered 2 days late', sender: 'them', time: '9:15 AM' }
    ]
  },
  {
    id: 3,
    sender: 'Bekzat Turganbek',
    avatar: 'https://randomuser.me/api/portraits/men/3.jpg',
    message: 'Question about payment options',
    time: new Date(Date.now() - 1000 * 60 * 60 * 2), // 2 hours ago
    read: true,
    status: 'resolved',
    messages: [
      { text: 'Do you accept PayPal payments?', sender: 'them', time: '8:45 AM' },
      { text: 'Yes, we accept PayPal and credit cards', sender: 'me', time: '8:50 AM' },
      { text: 'Thank you for your help!', sender: 'them', time: '8:52 AM' }
    ]
  }
]);

const currentChatUser = ref({
  id: null,
  name: '',
  color: 'primary',
  initials: '',
  avatar: null,
  messages: []
});


const unreadNotifications = computed(() => {
  return notifications.value.filter(n => !n.read);
});

const activeChatsCount = computed(() => {
  return notifications.value.filter(n => n.status === 'active').length;
});

const resolvedChatsCount = computed(() => {
  return notifications.value.filter(n => n.status === 'resolved').length;
});

const avgResponseTime = computed(() => {
  const activeChats = notifications.value.filter(n => n.status === 'active');
  if (activeChats.length === 0) return 0;

  const totalMinutes = activeChats.reduce((sum, chat) => {
    const lastMessage = chat.messages[chat.messages.length - 1];
    if (lastMessage.sender === 'them') {
      return sum + 5;
    }
    return sum;
  }, 0);

  return Math.round(totalMinutes / activeChats.length);
});

// Methods
function openChat(notification) {

  notification.read = true;
  notificationMenu.value = false;

  currentChatUser.value = {
    id: notification.id,
    name: notification.sender,
    color: getUserColor(notification.sender),
    initials: getUserInitials(notification.sender),
    avatar: notification.avatar,
    messages: notification.messages || []
  };


  chatOpen.value = true;
}

function closeChat() {
  chatOpen.value = false;
  currentChatUser.value = {
    id: null,
    name: '',
    color: 'primary',
    initials: '',
    avatar: null,
    messages: []
  };
}

function sendMessage() {
  if (newMessage.value.trim()) {
    const message = {
      text: newMessage.value,
      sender: 'me',
      time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
    };


    currentChatUser.value.messages.push(message);


    const notification = notifications.value.find(n => n.id === currentChatUser.value.id);
    if (notification) {
      notification.messages.push(message);
    }

    newMessage.value = '';

    if (notification && notification.status !== 'resolved') {
      setTimeout(() => {
        const reply = {
          text: 'Thanks for your message. We will get back to you soon.',
          sender: 'them',
          time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        };

        currentChatUser.value.messages.push(reply);
        notification.messages.push(reply);
      }, 1000);
    }
  }
}

function markAllAsRead() {
  notifications.value.forEach(n => n.read = true);
}

function getUserColor(name) {
  const colors = ['primary', 'secondary', 'success', 'error', 'warning', 'info'];
  const index = name.split('').reduce((acc, char) => acc + char.charCodeAt(0), 0) % colors.length;
  return colors[index];
}

function getUserInitials(name) {
  return name.split(' ').map(n => n[0]).join('').substring(0, 2);
}

function formatTime(date) {
  const now = new Date();
  const diff = now - date;

  if (diff < 1000 * 60) return 'Just now';
  if (diff < 1000 * 60 * 60) return `${Math.floor(diff / (1000 * 60))} minutes ago`;
  if (diff < 1000 * 60 * 60 * 24) return `${Math.floor(diff / (1000 * 60 * 60))} hours ago`;

  return date.toLocaleDateString() + ' ' + date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

const profileMenu = [
  { title: 'Profile' },
  { title: 'Settings' },
  { title: 'Logout' },
];

function navigateTo(type) {
  router.push({ name: `${type}-items` });
}
</script>

<style scoped>

.main-content {
  margin-top: 100px !important;
}
.welcome-card {
  background: linear-gradient(135deg, #ffffff 0%, #f5f7fa 100%);
  border-left: 4px solid var(--v-primary-base);
}

.dashboard-card {
  transition: all 0.3s ease;
  border-radius: 12px;
  overflow: hidden;
}

.dashboard-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0,0,0,0.1) !important;
}

.gradient-1 {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.gradient-2 {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.gradient-3 {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.gradient-4 {
  background: linear-gradient(135deg, #ff9a9e 0%, #fad0c4 100%);
}
.chat-container {
  height: calc(100vh - 180px);
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding-bottom: 20px;
}

.message {
  max-width: 80%;
  padding: 12px;
  border-radius: 12px;
  position: relative;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.message.me {
  background-color: #e3f2fd;
  margin-left: auto;
  border-bottom-right-radius: 0;
}

.message.them {
  background-color: #f5f5f5;
  margin-right: auto;
  border-bottom-left-radius: 0;
}

.message-sender {
  display: flex;
  align-items: center;
  margin-bottom: 4px;
  font-size: 0.75rem;
  color: #616161;
}

.message-content {
  font-size: 14px;
  word-wrap: break-word;
}

.message-time {
  font-size: 10px;
  color: #757575;
  text-align: right;
  margin-top: 4px;
}

.unread-notification {
  background-color: rgba(25, 118, 210, 0.05);
}
</style>
