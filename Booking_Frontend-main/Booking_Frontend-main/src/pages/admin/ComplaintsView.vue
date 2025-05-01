<template>
  <v-container class="pa-6">
    <!-- Header with Stats -->
    <v-row class="mb-6">
      <v-col cols="12">
        <h1 class="text-h4 font-weight-bold text-orange-darken-2">
          <v-icon color="orange-darken-2" size="36" class="mr-3">mdi-alert-circle</v-icon>
          Customer Complaints
        </h1>
        <p class="text-subtitle-1 text-grey-darken-1 mt-2">
          Manage product issues and customer concerns
        </p>
      </v-col>

      <v-col cols="12" md="4">
        <v-card color="orange-lighten-5" flat>
          <v-card-text class="d-flex align-center">
            <v-avatar color="orange-darken-2" rounded class="mr-4">
              <v-icon dark>mdi-alert</v-icon>
            </v-avatar>
            <div>
              <div class="text-h5 font-weight-bold">{{ productComplaintsCount }}</div>
              <div class="text-subtitle-1">Product Issues</div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" md="4">
        <v-card color="blue-lighten-5" flat>
          <v-card-text class="d-flex align-center">
            <v-avatar color="blue-darken-2" rounded class="mr-4">
              <v-icon dark>mdi-message</v-icon>
            </v-avatar>
            <div>
              <div class="text-h5 font-weight-bold">{{ unreadMessagesCount }}</div>
              <div class="text-subtitle-1">Unread Messages</div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" md="4">
        <v-card color="green-lighten-5" flat>
          <v-card-text class="d-flex align-center">
            <v-avatar color="green-darken-2" rounded class="mr-4">
              <v-icon dark>mdi-check</v-icon>
            </v-avatar>
            <div>
              <div class="text-h5 font-weight-bold">{{ resolvedCount }}</div>
              <div class="text-subtitle-1">Resolved Issues</div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- Tabs -->
    <v-tabs v-model="tab" color="orange-darken-2" grow>
      <v-tab value="products">
        <v-icon left>mdi-package-variant</v-icon>
        Product Complaints
        <v-badge v-if="productComplaintsCount > 0" color="red" :content="productComplaintsCount" inline class="ml-2"></v-badge>
      </v-tab>
      <v-tab value="messages">
        <v-icon left>mdi-message-text</v-icon>
        Customer Messages
        <v-badge v-if="unreadMessagesCount > 0" color="red" :content="unreadMessagesCount" inline class="ml-2"></v-badge>
      </v-tab>
    </v-tabs>

    <v-divider></v-divider>

    <!-- Tab Content -->
    <v-window v-model="tab">
      <!-- Product Complaints Tab -->
      <v-window-item value="products">
        <v-card flat class="mt-4">
          <!-- Filter Bar -->
          <v-card-actions class="px-4 py-3">
            <v-text-field
              v-model="productSearch"
              prepend-inner-icon="mdi-magnify"
              placeholder="Search products..."
              variant="outlined"
              density="compact"
              hide-details
              single-line
              clearable
            ></v-text-field>

            <v-select
              v-model="productComplaintFilter"
              :items="productComplaintFilters"
              item-title="text"
              item-value="value"
              label="Filter"
              variant="outlined"
              density="compact"
              hide-details
              class="ml-2"
              style="max-width: 200px;"
            ></v-select>
          </v-card-actions>

          <v-divider></v-divider>

          <!-- Complaints List -->
          <v-list lines="two" class="pa-0">
            <v-list-item
              v-for="product in filteredProductComplaints"
              :key="product.id"
              @click="openProductComplaintDialog(product)"
              class="complaint-item"
            >
              <template v-slot:prepend>
                <v-avatar rounded="lg" size="60" class="mr-4">
                  <v-img :src="product.image" cover></v-img>
                  <v-badge
                    color="red"
                    :content="product.complaints.length"
                    overlap
                    bordered
                    location="top right"
                  ></v-badge>
                </v-avatar>
              </template>

              <v-list-item-title class="font-weight-medium d-flex align-center">
                {{ product.name }}
                <v-chip
                  v-if="product.urgent"
                  color="red"
                  size="small"
                  class="ml-2 text-white"
                >
                  Urgent
                </v-chip>
              </v-list-item-title>

              <v-list-item-subtitle class="d-flex flex-wrap mt-1">
                <v-chip
                  v-for="complaint in product.complaints.slice(0, 3)"
                  :key="complaint.id"
                  size="small"
                  class="mr-1 mb-1"
                  :color="getComplaintColor(complaint.type)"
                  :prepend-icon="getComplaintIcon(complaint.type)"
                >
                  {{ complaint.type }}
                </v-chip>
              </v-list-item-subtitle>

              <template v-slot:append>
                <v-icon>mdi-chevron-right</v-icon>
              </template>
            </v-list-item>
          </v-list>
        </v-card>
      </v-window-item>

      <!-- Customer Messages Tab -->
      <v-window-item value="messages">
        <v-card flat class="mt-4">
          <div class="d-flex" style="height: 600px;">
            <!-- Message List -->
            <v-card width="350" flat class="border-right">
              <v-card-actions class="px-4 py-3">
                <v-select
                  v-model="messageFilter"
                  :items="messageFilters"
                  item-title="text"
                  item-value="value"
                  label="Filter"
                  variant="outlined"
                  density="compact"
                  hide-details
                ></v-select>
              </v-card-actions>

              <v-divider></v-divider>

              <v-list lines="two" class="pa-0">
                <v-list-item
                  v-for="message in filteredMessages"
                  :key="message.id"
                  @click="selectMessage(message)"
                  :class="{ 'unread-message': !message.read }"
                >
                  <template v-slot:prepend>
                    <v-avatar :color="getUserColor(message.user)" size="40" class="mr-3">
                      <v-img v-if="message.user.avatar" :src="message.user.avatar"></v-img>
                      <span v-else class="text-white">{{ getUserInitials(message.user.name) }}</span>
                    </v-avatar>
                  </template>

                  <v-list-item-title class="font-weight-medium">
                    {{ message.user.name }}
                  </v-list-item-title>
                  <v-list-item-subtitle class="text-truncate">
                    {{ message.preview }}
                  </v-list-item-subtitle>

                  <v-list-item-subtitle class="text-caption text-grey">
                    {{ formatTime(message.time) }}
                  </v-list-item-subtitle>

                  <template v-slot:append>
                    <v-chip v-if="message.status === 'new'" color="orange" size="small" class="text-white">New</v-chip>
                    <v-chip v-else-if="message.status === 'pending'" color="blue" size="small" class="text-white">Pending</v-chip>
                  </template>
                </v-list-item>
              </v-list>
            </v-card>

            <!-- Message Detail -->
            <v-card flat class="flex-grow-1 d-flex flex-column">
              <template v-if="selectedMessage">

                <v-toolbar color="orange-lighten-5" density="compact">
                  <v-btn icon @click="selectedMessage = null">
                    <v-icon>mdi-arrow-left</v-icon>
                  </v-btn>

                  <v-toolbar-title class="d-flex align-center">
                    <v-avatar :color="getUserColor(selectedMessage.user)" size="36" class="mr-3">
                      <v-img v-if="selectedMessage.user.avatar" :src="selectedMessage.user.avatar"></v-img>
                      <span v-else class="text-white">{{ getUserInitials(selectedMessage.user.name) }}</span>
                    </v-avatar>
                    <div>
                      <div class="font-weight-medium">{{ selectedMessage.user.name }}</div>
                      <div class="text-caption text-grey">{{ selectedMessage.user.email }}</div>
                    </div>
                  </v-toolbar-title>

                  <v-spacer></v-spacer>

                  <v-btn icon>
                    <v-icon>mdi-phone</v-icon>
                  </v-btn>

                  <v-btn icon>
                    <v-icon>mdi-email</v-icon>
                  </v-btn>

                  <v-menu>
                    <template v-slot:activator="{ props }">
                      <v-btn icon v-bind="props">
                        <v-icon>mdi-dots-vertical</v-icon>
                      </v-btn>
                    </template>
                    <v-list>
                      <v-list-item @click="markAsResolved">
                        <v-list-item-title>Mark as Resolved</v-list-item-title>
                      </v-list-item>
                      <v-list-item @click="blockUser">
                        <v-list-item-title>Block User</v-list-item-title>
                      </v-list-item>
                    </v-list>
                  </v-menu>
                </v-toolbar>

                <v-divider></v-divider>

                <!-- Message Content -->
                <div class="message-content pa-4 flex-grow-1">
                  <div class="message-bubble message-them">
                    <div class="message-text">{{ selectedMessage.content }}</div>
                    <div class="message-time">{{ formatTime(selectedMessage.time) }}</div>
                  </div>

                  <template v-if="selectedMessage.replies.length > 0">
                    <div v-for="(reply, index) in selectedMessage.replies" :key="index" class="message-bubble message-me">
                      <div class="message-text">{{ reply.text }}</div>
                      <div class="message-time">{{ formatTime(reply.time) }}</div>
                    </div>
                  </template>
                </div>

                <!-- Reply Area -->
                <v-card-actions class="pa-4 border-top">
                  <v-select
                    v-model="selectedTemplate"
                    :items="replyTemplates"
                    label="Quick reply"
                    variant="outlined"
                    density="compact"
                    hide-details
                    class="mr-2"
                  ></v-select>

                  <v-textarea
                    v-model="replyText"
                    placeholder="Type your response..."
                    variant="outlined"
                    rows="1"
                    auto-grow
                    hide-details
                    class="flex-grow-1"
                    :counter="200"
                    maxlength="200"
                  ></v-textarea>

                  <v-btn
                    color="orange-darken-2"
                    variant="flat"
                    class="ml-2"
                    @click="sendReply"
                  >
                    Send
                  </v-btn>
                </v-card-actions>
              </template>

              <template v-else>
                <div class="empty-state">
                  <v-icon size="64" color="grey-lighten-1">mdi-message-text-outline</v-icon>
                  <div class="text-h6 mt-4">Select a message to view</div>
                  <div class="text-body-1 mt-2">Click on a message from the list to view details</div>
                </div>
              </template>
            </v-card>
          </div>
        </v-card>
      </v-window-item>
    </v-window>

    <!-- Product Complaint Dialog -->
    <v-dialog v-model="productComplaintDialog" max-width="800">
      <v-card v-if="selectedProductComplaint">
        <v-card-title class="d-flex align-center">
          <v-avatar rounded="lg" size="60" class="mr-3">
            <v-img :src="selectedProductComplaint.image" cover></v-img>
          </v-avatar>
          <div>
            <div class="text-h6">{{ selectedProductComplaint.name }}</div>
            <div class="text-caption text-grey">ID: {{ selectedProductComplaint.id }}</div>
          </div>
          <v-spacer></v-spacer>
          <v-btn icon @click="productComplaintDialog = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>

        <v-divider></v-divider>

        <v-card-text class="pa-0">
          <v-list lines="two" class="pa-0">
            <v-list-item
              v-for="complaint in selectedProductComplaint.complaints"
              :key="complaint.id"
            >
              <template v-slot:prepend>
                <v-avatar :color="getUserColor(complaint.user)" size="40" class="mr-3">
                  <v-img v-if="complaint.user.avatar" :src="complaint.user.avatar"></v-img>
                  <span v-else class="text-white">{{ getUserInitials(complaint.user.name) }}</span>
                </v-avatar>
              </template>

              <v-list-item-title class="font-weight-medium">
                {{ complaint.user.name }}
                <v-chip size="small" :color="getComplaintColor(complaint.type)" class="ml-2 text-white">
                  {{ complaint.type }}
                </v-chip>
              </v-list-item-title>
              <v-list-item-subtitle>
                {{ complaint.text }}
              </v-list-item-subtitle>
              <v-list-item-subtitle class="text-caption text-grey">
                {{ formatTime(complaint.time) }}
              </v-list-item-subtitle>
            </v-list-item>
          </v-list>
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions class="pa-4">
          <v-btn color="error" variant="outlined" @click="deleteProduct">
            <v-icon left>mdi-delete</v-icon>
            Delete Product
          </v-btn>
          <v-spacer></v-spacer>
          <v-btn color="grey" variant="text" @click="ignoreComplaints">
            <v-icon left>mdi-close-circle</v-icon>
            Ignore Complaints
          </v-btn>
          <v-btn color="orange-darken-2" variant="flat" @click="resolveComplaints">
            <v-icon left>mdi-check-circle</v-icon>
            Mark as Resolved
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';

const tab = ref('products');
const productSearch = ref('');
const productComplaintFilter = ref('all');
const messageFilter = ref('all');
const selectedMessage = ref(null);
const selectedTemplate = ref(null);
const replyText = ref('');
const productComplaintDialog = ref(false);
const selectedProductComplaint = ref(null);


const productComplaints = ref([
  {
    id: 1,
    name: 'Premium Camera Lens',
    image: 'https://example.com/lens.jpg',
    urgent: true,
    complaints: [
      {
        id: 101,
        user: { name: 'Alex Johnson', email: 'alex@example.com' },
        type: 'Incorrect Description',
        text: 'The lens description says it has image stabilization but it actually doesn\'t',
        time: new Date(Date.now() - 1000 * 60 * 60 * 2) // 2 hours ago
      },
      {
        id: 102,
        user: { name: 'Sarah Miller', email: 'sarah@example.com' },
        type: 'Damaged Product',
        text: 'Received with scratches on the glass',
        time: new Date(Date.now() - 1000 * 60 * 60 * 24) // 1 day ago
      }
    ]
  },
  {
    id: 2,
    name: 'Wireless Headphones',
    image: 'https://example.com/headphones.jpg',
    urgent: false,
    complaints: [
      {
        id: 201,
        user: { name: 'Michael Brown', email: 'michael@example.com' },
        type: 'Not Received',
        text: 'Ordered 2 weeks ago but still haven\'t received the item',
        time: new Date(Date.now() - 1000 * 60 * 60 * 24 * 7) // 1 week ago
      }
    ]
  }
]);

const messages = ref([
  {
    id: 1,
    user: { name: 'Michael Brown', email: 'michael@example.com', avatar: 'https://example.com/avatar1.jpg' },
    type: 'Fraud',
    preview: 'I believe this product listing is a scam...',
    content: 'I believe this product listing is a scam. The seller asked for payment outside your platform and then disappeared.',
    time: new Date(Date.now() - 1000 * 60 * 30), // 30 minutes ago
    status: 'new',
    read: false,
    replies: []
  },
  {
    id: 2,
    user: { name: 'Emma Wilson', email: 'emma@example.com' },
    type: 'Incorrect Info',
    preview: 'The product specifications are wrong...',
    content: 'The product page says the device has 128GB storage but it actually only has 64GB. This is false advertising!',
    time: new Date(Date.now() - 1000 * 60 * 60 * 3), // 3 hours ago
    status: 'pending',
    read: true,
    replies: [
      {
        text: 'We have forwarded your complaint to our product team for verification.',
        time: new Date(Date.now() - 1000 * 60 * 60 * 2) // 2 hours ago
      }
    ]
  }
]);

const productComplaintFilters = [
  { text: 'All Complaints', value: 'all' },
  { text: 'Fraud', value: 'fraud' },
  { text: 'Incorrect Info', value: 'incorrect' },
  { text: 'Damaged', value: 'damaged' },
  { text: 'Not Received', value: 'not_received' }
];

const messageFilters = [
  { text: 'All Messages', value: 'all' },
  { text: 'New', value: 'new' },
  { text: 'Pending', value: 'pending' },
  { text: 'Resolved', value: 'resolved' },
  { text: 'Fraud', value: 'fraud' },
  { text: 'Incorrect Info', value: 'incorrect' },
  { text: 'Other', value: 'other' }
];

const replyTemplates = [
  'Your complaint has been received. We will check the product.',
  'We have forwarded your complaint to the support service.',
  'Contact the support service: +7 700 123 4567',
  'Thank you for reporting this issue. We take all complaints seriously.'
];


const filteredProductComplaints = computed(() => {
  let filtered = productComplaints.value;


  if (productSearch.value) {
    const search = productSearch.value.toLowerCase();
    filtered = filtered.filter(product =>
      product.name.toLowerCase().includes(search) ||
      product.complaints.some(c =>
        c.user.name.toLowerCase().includes(search) ||
        c.text.toLowerCase().includes(search)
      )
    );
  }


  if (productComplaintFilter.value !== 'all') {
    filtered = filtered.filter(product =>
      product.complaints.some(c => c.type.toLowerCase().includes(productComplaintFilter.value))
    );
  }

  return filtered;
});

const filteredMessages = computed(() => {
  let filtered = messages.value;


  if (messageFilter.value !== 'all') {
    filtered = filtered.filter(message => message.status === messageFilter.value);
  }

  return filtered;
});

const productComplaintsCount = computed(() => productComplaints.value.length);
const unreadMessagesCount = computed(() => messages.value.filter(m => !m.read).length);
const resolvedCount = computed(() => messages.value.filter(m => m.status === 'resolved').length);


function openProductComplaintDialog(product) {
  selectedProductComplaint.value = product;
  productComplaintDialog.value = true;
}

function selectMessage(message) {
  selectedMessage.value = message;
  message.read = true;


  if (message.status === 'new') {
    message.status = 'pending';
  }
}

function sendReply() {
  if (!selectedMessage.value) return;

  const reply = {
    text: selectedTemplate.value || replyText.value,
    time: new Date()
  };

  selectedMessage.value.replies.push(reply);
  selectedMessage.value.status = 'resolved';


  selectedTemplate.value = null;
  replyText.value = '';
}

function deleteProduct() {

  productComplaints.value = productComplaints.value.filter(
    p => p.id !== selectedProductComplaint.value.id
  );
  productComplaintDialog.value = false;
}

function ignoreComplaints() {
  productComplaintDialog.value = false;
}

function resolveComplaints() {
  productComplaintDialog.value = false;
}

function markAsResolved() {
  if (selectedMessage.value) {
    selectedMessage.value.status = 'resolved';
  }
}

function blockUser() {
  console.log('Blocking user:', selectedMessage.value.user.name);
}

function getUserColor(user) {
  const colors = ['primary', 'secondary', 'success', 'error', 'warning', 'info'];
  const index = user.name.split('').reduce((acc, char) => acc + char.charCodeAt(0), 0) % colors.length;
  return colors[index];
}

function getUserInitials(name) {
  return name.split(' ').map(n => n[0]).join('').substring(0, 2);
}

function getComplaintColor(type) {
  const colors = {
    'fraud': 'red',
    'incorrect': 'orange',
    'damaged': 'deep-orange',
    'not_received': 'amber',
    'default': 'grey'
  };

  const key = type.toLowerCase().replace(/\s+/g, '_');
  return colors[key] || colors.default;
}

function getComplaintIcon(type) {
  const icons = {
    'fraud': 'mdi-alert-octagon',
    'incorrect': 'mdi-information-outline',
    'damaged': 'mdi-package-variant-remove',
    'not_received': 'mdi-truck-delivery-outline',
    'default': 'mdi-message-alert'
  };

  const key = type.toLowerCase().replace(/\s+/g, '_');
  return icons[key] || icons.default;
}

function formatTime(date) {
  const now = new Date();
  const diff = now - date;

  if (diff < 1000 * 60) return 'Just now';
  if (diff < 1000 * 60 * 60) return `${Math.floor(diff / (1000 * 60))}m ago`;
  if (diff < 1000 * 60 * 60 * 24) return `${Math.floor(diff / (1000 * 60 * 60))}h ago`;
  return date.toLocaleDateString();
}

onMounted(() => {

});
</script>

<style scoped>
.border-right {
  border-right: 1px solid rgba(0, 0, 0, 0.12);
}

.complaint-item {
  transition: all 0.3s ease;
  border-left: 3px solid transparent;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}

.complaint-item:hover {
  background-color: rgba(251, 140, 0, 0.05);
  border-left-color: #fb8c00;
}

.unread-message {
  background-color: rgba(251, 192, 45, 0.1);
  border-left: 3px solid #fbc02d;
}

.message-content {
  overflow-y: auto;
  background-color: #f9f9f9;
}

.message-bubble {
  max-width: 70%;
  padding: 12px 16px;
  margin-bottom: 8px;
  position: relative;
}

.message-bubble.message-me {
  background-color: #fb8c00;
  color: white;
  border-radius: 18px 18px 4px 18px;
  margin-left: auto;
}

.message-bubble.message-them {
  background-color: white;
  border: 1px solid #e0e0e0;
  border-radius: 18px 18px 18px 4px;
  margin-right: auto;
}

.message-text {
  margin-bottom: 4px;
}

.message-time {
  font-size: 0.75rem;
  opacity: 0.8;
  text-align: right;
}

.border-top {
  border-top: 1px solid rgba(0, 0, 0, 0.12);
}

.empty-state {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #9e9e9e;
}

.status-badge {
  position: absolute;
  top: -8px;
  right: -8px;
  border: 2px solid white;
}
</style>
