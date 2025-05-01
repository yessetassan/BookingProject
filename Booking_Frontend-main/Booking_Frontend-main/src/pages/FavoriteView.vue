<template>
  <Header />
  <v-container class="favorites-container">

    <div class="favorites-hero">
      <h1 class="text-h3 font-weight-bold mb-2">{{ $t('favorite_your_favorites') }}</h1>
      <p class="text-body-1 text-grey-darken-1">{{ $t('favorite_shablon') }}</p>
    </div>


    <v-row v-if="loading" class="loading-row">
      <v-col cols="12" class="text-center py-12">
        <v-progress-circular
          indeterminate
          color="primary"
          size="64"
          width="6"
        ></v-progress-circular>
        <p class="text-h6 mt-4">Loading your favorites...</p>
      </v-col>
    </v-row>


    <div v-else>
      <transition-group name="fade-list" tag="div" class="favorites-grid">
        <div
          v-for="favorite in favorites"
          :key="favorite.id"
          class="favorite-item"
        >
          <CardItem
            :card="formatFavoriteItem(favorite)"
            class="favorite-card"
          />
          <v-btn
            icon
            class="remove-favorite-btn"
            @click.stop="removeFavorite(favorite)"
            variant="text"
            color="error"
          >
            <v-icon>mdi-heart-remove</v-icon>
            <v-tooltip activator="parent" location="bottom">{{ $t('favorite_remove_from') }}</v-tooltip>
          </v-btn>
        </div>
      </transition-group>


      <v-card
        v-if="favorites.length === 0"
        class="empty-state mx-auto"
        max-width="500"
        elevation="0"
      >
        <div class="empty-state-content">
          <v-icon size="96" color="pink-lighten-3">mdi-heart-broken</v-icon>
          <h2 class="text-h4 mt-4 font-weight-medium">{{ $t('favorite_no_favorite') }}</h2>
          <p class="text-body-1 mt-2 text-grey-darken-1">
            {{ $t('favorite_not_shablon') }}
          </p>
          <v-btn
            color="primary"
            variant="outlined"
            class="mt-6"
            to="/"
          >
          {{ $t('favorite_browse_items') }}
          </v-btn>
        </div>
      </v-card>
    </div>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from "@/axios.js";
import { useAuthStore } from '@/stores/auth';
import Header from '@/components/Header.vue';
import CardItem from '@/components/CardItem.vue';

const authStore = useAuthStore();
const favorites = ref([]);
const loading = ref(true);
const removingId = ref(null);

const formatFavoriteItem = (favorite) => {
  return {
    id: favorite.item.id,
    name: favorite.item.name,
    description: favorite.item.description,
    pricePerDay: favorite.item.pricePerDay,
    files: favorite.files,
  };
};

const fetchFavorites = async () => {
  try {
    loading.value = true;
    const response = await axios.get('/api/favourites', {
      headers: {
        Authorization: `Bearer ${authStore.token}`
      }
    });

    if (response.data.success) {
      favorites.value = response.data.body;
    }
  } catch (error) {
    console.error('Error fetching favorites:', error);
  } finally {
    loading.value = false;
  }
};

const removeFavorite = async (favorite) => {
  try {
    removingId.value = favorite.id;
    await axios.delete(`/api/favourites/${favorite.item.id}`, {
      headers: {
        Authorization: `Bearer ${authStore.token}`
      }
    });
    favorites.value = favorites.value.filter(f => f.id !== favorite.id);
  } catch (error) {
    console.error('Error removing favorite:', error);
  } finally {
    removingId.value = null;
  }
};

onMounted(() => {
  if (authStore.isAuthenticated) {
    fetchFavorites();
  } else {
    window.location.href = '/login';
  }
});
</script>

<style scoped>
.favorites-container {
  max-width: 1400px;
  padding-top: 32px;
  padding-bottom: 64px;
}

.favorites-hero {
  text-align: center;
  margin-bottom: 48px;
  padding: 24px 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

.loading-row {
  min-height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.favorites-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
  padding: 8px;
}

.favorite-item {
  position: relative;
  transition: all 0.3s ease;
}

.favorite-item:hover {
  transform: translateY(-4px);
}

.favorite-card {
  transition: all 0.3s ease;
  height: 100%;
}

.favorite-card:hover {
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1) !important;
}

.remove-favorite-btn {
  position: absolute;
  top: 8px;
  right: 8px;
  z-index: 2;
  background: rgba(255, 255, 255, 0.9) !important;
  backdrop-filter: blur(2px);
}

.empty-state {
  margin-top: 64px;
  text-align: center;
  padding: 48px 24px;
  border-radius: 16px;
  background: rgba(255, 245, 245, 0.5);
}

.empty-state-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* Animation classes */
.fade-list-move,
.fade-list-enter-active,
.fade-list-leave-active {
  transition: all 0.5s ease;
}

.fade-list-enter-from,
.fade-list-leave-to {
  opacity: 0;
  transform: translateY(30px);
}

.fade-list-leave-active {
  position: absolute;
}

@media (max-width: 400px) {
  .favorites-hero {
    margin-bottom: 24px;
  }

  .favorites-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }
}
</style>
