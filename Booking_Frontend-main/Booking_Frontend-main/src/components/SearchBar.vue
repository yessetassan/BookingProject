<template>
  <!-- Search Bar Container -->
  <div class="search-bar-container">
    <div class="search-bar-content">

      <div class="search-input-group">
        <v-text-field
          v-model="searchQuery"
          :label="mobileView ? '' : $t('search_search_items')"
          prepend-inner-icon="mdi-magnify"
          variant="outlined"
          single-line
          hide-details
          density="compact"
          class="search-bar"
          @keyup.enter="performSearch"
        ></v-text-field>

        <v-btn
          color="primary"
          @click="performSearch"
          class="search-button"
          :icon="mobileView"
        >
          <v-icon>mdi-magnify</v-icon>
          <span v-if="!mobileView">{{ $t('search_find') }}</span>
        </v-btn>
      </div>

      <!-- My City Section - Hidden on mobile -->
      <div v-if="!mobileView" class="my-city">
        <span class="my-city-label">{{ $t('search_my_city') }}:</span>
        <span class="text-primary my-city-name">{{ $t('search_almaty') }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';

const searchQuery = ref('');
const mobileView = ref(false);

const checkScreenSize = () => {
  mobileView.value = window.innerWidth < 768;
};

onMounted(() => {
  checkScreenSize();
  window.addEventListener('resize', checkScreenSize);
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', checkScreenSize);
});

const emit = defineEmits(['search']);

const performSearch = () => {
  if (searchQuery.value.trim()) {
    emit('search', searchQuery.value);
  }
};
</script>

<style scoped>
.search-bar-container {
  width: 100%;
  padding: 12px 16px;
  background: #f8f8f8;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
  margin-top: 100px;

}

.search-bar-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  gap: 16px;
}

.search-input-group {
  flex: 1;
  display: flex;
  gap: 8px;
}

.search-bar {
  flex: 1;
  min-width: 120px;
}

.search-button {
  height: 40px;
  white-space: nowrap;
}

.my-city {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  font-size: 14px;
  color: #333;
  min-width: 120px;
}

.my-city-label {
  font-weight: bold;
  white-space: nowrap;
}


@media (max-width: 767px) {
  .search-bar-container {
    padding: 8px 12px;
  }

  .search-bar-content {
    gap: 8px;
  }

  .search-input-group {
    width: 100%;
  }

  .search-button {
    min-width: 40px;
    width: 40px;
  }
}


@media (min-width: 768px) and (max-width: 1023px) {
  .search-bar-content {
    gap: 12px;
  }

  .my-city {
    min-width: 100px;
  }
}
</style>
