<template>
  <v-card @click="navigateToCardPage()" class="custom-card">
    <div @click.stop>
      <swiper
      :modules="[Autoplay, Pagination]"
      :loop="true"
      :pagination="{ clickable: true }"

      class="card-carousel"
      @click.stop
    >
      <swiper-slide
        @click.stop
        v-for="(file, index) in card.files"
        :key="index"
      >
        <img :src="file.fileUrl" class="carousel-image" alt="Image" />
      </swiper-slide>
    </swiper>
    </div>

    <v-btn
      icon
      class="favorite-btn"
      @click.stop="toggleFavorite"
      :loading="favoriteLoading"
    >
      <v-icon :color="isFavorite ? 'red' : 'black'">
        {{ isFavorite ? 'mdi-heart' : 'mdi-heart-outline' }}
      </v-icon>
    </v-btn>

    <v-card-title class="card-title">
      {{ card.name }}
    </v-card-title>

    <v-card-subtitle class="card-subtitle">
      {{ card.description }}
    </v-card-subtitle>

    <v-card-text class="card-price">
      <span>KZT {{ card.pricePerDay }}</span>
    </v-card-text>
  </v-card>
</template>

<script setup>
import { ref } from 'vue';
import { Swiper, SwiperSlide } from 'swiper/vue';
import { Autoplay, Pagination } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/pagination';
import { useRouter } from 'vue-router';
import axios from 'axios';
import {useAuthStore} from '@/stores/auth'
import { onMounted } from 'vue';

const emit = defineEmits(['favorite-toggled']);


const props = defineProps({
  card: {
    type: Object,
    required: true,
    validator: (value) => {

      return value.id !== undefined;
    }
  },
  initialFavorite: {
    type: Boolean,
    default: false
  }
});

const router = useRouter();
const authStore = useAuthStore();
const isFavorite = ref(props.initialFavorite);
const favoriteLoading = ref(false);







const toggleFavorite = async () => {
  if (!authStore.isAuthenticated) {
    router.push('/login');
    return;
  }

  favoriteLoading.value = true;

  try {
    if (isFavorite.value) {
      // Remove from favorites
      await axios.delete(`http://localhost:8080/api/favourites/${props.card.id}`, {
        headers: {
          Authorization: `Bearer ${authStore.token}`
        }
      });
    } else {
      // Add to favorites
      await axios.post(
        `http://localhost:8080/api/favourites?itemId=${props.card.id}`,
        null,
        {
          headers: {
            Authorization: `Bearer ${authStore.token}`
          }
        }
      );
    }
    isFavorite.value = !isFavorite.value;
    emit('favorite-toggled', props.card.id, isFavorite.value);

  } catch (error) {
    console.error('Error toggling favorite:', error);
  } finally {
    favoriteLoading.value = false;
  }
};

const navigateToCardPage = () => {
  if (!props.card.id) {
    console.error('Card ID is required for navigation');
    return;
  }
  router.push({
    name: 'card-detail',
    params: { id: props.card.id }
  });
};
</script>

<style scoped>
.custom-card {
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 0 !important;

}

.card-carousel {
  width: 100%;
  height: 200px;
  cursor:grab;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
}

.favorite-btn {
  position: absolute;
  top: 12px;
  right: 12px;
  z-index: 10;
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}


::v-deep(.swiper-pagination-bullet) {
  width: 12px !important;
  height: 12px !important;
}

::v-deep(.swiper-pagination) {
  width: 100% !important; 
}


.favorite-btn:hover {
  background-color: rgba(255, 255, 255, 1);
  transform: scale(1.1);
}

.card-title {
  font-weight: bold;
  font-size: 18px;
  padding-bottom: 0 !important;
  white-space: normal;
  word-break: break-word;
}
.card-title:hover {
  color: rgb(49, 125, 129);
}

.card-subtitle {
  font-size: 14px;
  color: #323232;
  white-space: normal;
  word-break: break-word;
}

.card-price {
  font-weight: bold;
  font-size: 18px;
  color: black;
  text-align: right;
}
</style>


<!-- <template>
  <v-card @click="navigateToCardPage()" class="custom-card">

    <div
      ref="carouselContainer"
      class="scroll-container"
      @mousedown="startDrag"
      @mousemove="onDrag"
      @mouseup="endDrag"
      @mouseleave="endDrag"
      @touchstart="startDrag"
      @touchmove="onDrag"
      @touchend="endDrag"
      @click.stop
    >
      <div class="image-track">
        <img
          v-for="(file, index) in card.files"
          :key="index"
          :src="file.fileUrl"
          class="carousel-image"
          @click="navigateToCardPage()"
        />
      </div>
    </div>

    <v-btn icon class="favorite-btn" @click.stop="toggleFavorite">
      <v-icon :color="isFavorite ? 'red' : 'black'">
        {{ isFavorite ? 'mdi-heart' : 'mdi-heart-outline' }}
      </v-icon>
    </v-btn>

    <v-card-title class="card-title">
      {{ card.name }}
    </v-card-title>

    <v-card-subtitle class="card-subtitle">
      {{ card.description }}
    </v-card-subtitle>

    <v-card-text class="card-price">
      <span>KZT {{ card.pricePerDay }}</span>
    </v-card-text>
  </v-card>
</template>

<script setup>
import { ref } from 'vue';
import router from '@/router/index';

const props = defineProps({
  card: {
    type: Object,
    required: true,
  },
});

const isFavorite = ref(false);
const toggleFavorite = () => {
  isFavorite.value = !isFavorite.value;
};

const navigateToCardPage = () => {
  router.push({ name: 'card-detail' });
};

// Drag functionality for scrolling
const carouselContainer = ref(null);
let isDragging = false;
let startX = 0;
let scrollLeft = 0;

const startDrag = (e) => {
  isDragging = true;
  startX = e.type.includes('touch') ? e.touches[0].pageX : e.pageX;
  scrollLeft = carouselContainer.value.scrollLeft;
};

const onDrag = (e) => {
  if (!isDragging) return;
  e.preventDefault();
  const x = e.type.includes('touch') ? e.touches[0].pageX : e.pageX;
  const walk = (x - startX) * 1.5; // Adjust speed
  carouselContainer.value.scrollLeft = scrollLeft - walk;
};

const endDrag = () => {
  isDragging = false;
};
</script>

<style scoped>
.custom-card {
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 0 !important;
}

/* Scrollable image container */
.scroll-container {
  display: flex;
  overflow-x: auto;
  scroll-behavior: smooth;
  cursor: grab;
  width: 100%;
  white-space: nowrap;
  user-select: none;
  -webkit-overflow-scrolling: touch;
}

.image-track {
  display: flex;
  gap: 10px;
}

.carousel-image {
  height: 200px;
  border-radius: 12px;
  object-fit: cover;
  cursor: pointer;
  flex-shrink: 0;
  width: 300px; /* Adjust width as needed */
}

.favorite-btn {
  position: absolute;
  top: 12px;
  right: 12px;
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.favorite-btn:hover {
  background-color: rgba(255, 255, 255, 1);
  transform: scale(1.1);
}

.card-title {
  font-weight: bold;
  font-size: 18px;
  padding-bottom: 0 !important;
}

.card-subtitle {
  font-size: 14px;
  color: #323232;
}

.card-price {
  font-weight: bold;
  font-size: 18px;
  color: black;
  text-align: right;
}
</style> -->
