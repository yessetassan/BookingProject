<template>
  <Header/>
 <v-app>



   <!-- Main Content -->
   <v-main class="main-content">
     <v-container fluid class="pa-6">


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
                   <h3 class="text-h6 text-grey-darken-3">{{ $t('my_items_approved') }}</h3>
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
                   <h3 class="text-h6 text-grey-darken-3">{{ $t('my_items_rejected') }}</h3>
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
                    <h3 class="text-h6 text-grey-darken-3">{{ $t('my_items_checklist') }}</h3>
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


       </v-row>

     </v-container>
   </v-main>
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







function getUserColor(name) {
 const colors = ['primary', 'secondary', 'success', 'error', 'warning', 'info'];
 const index = name.split('').reduce((acc, char) => acc + char.charCodeAt(0), 0) % colors.length;
 return colors[index];
}

function getUserInitials(name) {
 return name.split(' ').map(n => n[0]).join('').substring(0, 2);
}



function navigateTo(type) {
 router.push({ name: `${type}-my-items` });
}
</script>

<style scoped>

.main-content {
 margin-top: 100px !important;
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



@keyframes fadeIn {
 from { opacity: 0; transform: translateY(10px); }
 to { opacity: 1; transform: translateY(0); }
}


</style>
