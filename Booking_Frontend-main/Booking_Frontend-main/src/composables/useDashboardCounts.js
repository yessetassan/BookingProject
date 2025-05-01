import { ref, onMounted, onUnmounted } from 'vue';
import axios from "@/axios.js";


export default function useDashboardCounts() {
  const counts = ref({
    approved: 0,
    rejected: 0,
    checklist: 0,
    complaints: 0
  });

  const loading = ref(true);
  let refreshInterval;

  const fetchCounts = async () => {
    try {
      const token = localStorage.getItem('authToken');
      const headers = { 'Authorization': `Bearer ${token}` };


      const [approvedRes, rejectedRes, checklistRes] = await Promise.all([
        axios.post('/api/items/approved', {
          page: 0,
          size: 1,  // Changed to 1 since we only need the count
          name: "",
          categoryId: null,
          filterAttributes: []
        }, { headers }),
        axios.post('/api/items/rejected', {
          page: 0,
          size: 1,
          name: "",
          categoryId: null,
          filterAttributes: []
        }, { headers }),
        axios.post('/api/items/checkIn', {
          page: 0,
          size: 1,
          name: "",
          categoryId: null,
          filterAttributes: []
        }, { headers })
      ]);

      counts.value = {
        approved: approvedRes.data.body?.page?.totalElements || 0,
        rejected: rejectedRes.data.body?.page?.totalElements || 0,
        checklist: checklistRes.data.body?.page?.totalElements || 0,
        complaints: 0 // Пока без API
      };

    } catch (error) {
      console.error('Error fetching dashboard counts:', error);
    } finally {
      loading.value = false;
    }
  };

  const startAutoRefresh = (interval = 30000) => {
    stopAutoRefresh(); 
    refreshInterval = setInterval(fetchCounts, interval);
  };

  const stopAutoRefresh = () => {
    if (refreshInterval) {
      clearInterval(refreshInterval);
      refreshInterval = null;
    }
  };

  onMounted(() => {
    fetchCounts();
    startAutoRefresh();
  });

  onUnmounted(() => {
    stopAutoRefresh();
  });

  return {
    counts,
    loading,
    refreshCounts: fetchCounts,
    startAutoRefresh,
    stopAutoRefresh
  };
}
