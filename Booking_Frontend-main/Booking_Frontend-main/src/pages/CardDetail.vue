<template>
  <div>
    <Header/>
  <v-container class="product-page" v-if="product">
    <v-row>

      <v-col cols="12" md="7">
        <v-img
          :key="mainImage"
          :src="mainImage"
          height="400"
          contain
          class="main-product-image mb-4"
        ></v-img>

        <div class="thumbnail-container" v-if="product.files && product.files.length">
          <v-img
            v-for="(file, index) in product.files"
            :key="index"
            :src="file.fileUrl"
            height="80"
            contain
            class="thumbnail"
            @click="changeMainImage(file.fileUrl)"
            :class="{ 'thumbnail-active': mainImage === file.fileUrl }"
          />
        </div>
      </v-col>


      <v-col cols="12" md="5" class="mt-5">
        <h1 class="product-title">{{ product.name }}</h1>
        <div class="rating-container">
          <v-rating
            v-model="product.rate"
            readonly
            color="amber"
            density="compact"
            size="small"
          ></v-rating>
          <span class="review-count">{{ product.rate }} {{ $t('card_detail_reviews') }}</span>
        </div>

        <v-divider class="my-4"></v-divider>

        <div class="price-container">
          <span class="current-price">{{ product.pricePerDay }} KZT</span>
          <!-- <span class="old-price">55 000 тг</span>
          <v-chip color="red" text-color="white" small class="discount">-10%</v-chip> -->
        </div>

        <!-- <v-btn v-if="authStore.user?.role === 'ROLE_ADMIN'"
          color="primary"
          large
          block
          class="buy-btn mt-4"
        >
          Update only Admin
        </v-btn> -->

        <v-btn v-if="authStore.user?.role === 'ROLE_ADMIN'"
          variant="outlined"
          color="primary"
          large
          block
          class="mt-2"
          @click="deleteDialog = true"
        > Delete only Admin
        </v-btn>

        <v-btn
          color="primary"
          large
          block
          class="mt-2"
          @click="navigateToBookingNow(itemId)"
        > {{ $t('card_detail_booking_now') }}
        </v-btn>

        <!-- <v-btn
          variant="outlined"
          color="primary"
          large
          block
          class="mt-2"
          @click="toggleFavorite"
          :disabled="favoriteLoading.value"
        >
          <template v-if="favoriteAdded">Remove from favorites</template>
          <template v-else>{{ isFavorite ? 'Remove from favorites' : 'Add to favorites' }}</template>
        </v-btn> -->

      </v-col>
    </v-row>

    <div class="product-tabs">
      <v-tabs v-model="tab" grow color="primary">
        <v-tab value="sellers">{{ $t('card_detail_owner') }}</v-tab>
        <v-tab value="reviews">{{ $t('card_detail_reviews') }} {{ totalReviews }}</v-tab>
        <v-tab value="specs">{{ $t('card_detail_characteristics') }}</v-tab>
        <v-tab value="description">{{ $t('card_detail_description') }}</v-tab>
      </v-tabs>

      <v-divider></v-divider>

      <v-window v-model="tab">
        <v-window-item value="sellers">
  <div class="owner-container">
    <div class="owner-header">
      <h2 class="owner-title">{{ $t('card_detail_owner') }}</h2>
      <div class="owner-divider"></div>
    </div>

    <v-card class="owner-card" elevation="0">
      <div class="owner-content">
        <!-- Avatar Section -->
        <div class="owner-avatar-wrapper">
          <v-avatar color="#4E60FF" size="120" class="owner-avatar">
            <span class="avatar-initials">
              {{ product.user.firstname.charAt(0) }}{{ product.user.lastname.charAt(0) }}
            </span>
          </v-avatar>
          <div v-if="product.user.role.name === 'ROLE_ADMIN'" class="verified-badge">
            <v-icon small color="white">mdi-shield-check</v-icon>
            <span>Verified</span>
          </div>
        </div>

        <!-- Details Section -->
        <div class="owner-details">
          <h3 class="owner-name">{{ product.user.firstname }} {{ product.user.lastname }}</h3>

          <div class="owner-contact-info">
            <div class="contact-item">
              <v-icon color="#4E60FF" size="small">mdi-email-outline</v-icon>
              <span>{{ product.user.username }}</span>
            </div>
            <div class="contact-item">
              <v-icon color="#4E60FF" size="small">mdi-phone-outline</v-icon>
              <span>{{ product.user.phone }}</span>
            </div>
          </div>

          <div class="owner-meta">
            <v-chip class="member-chip" color="#F3F4FF" text-color="#4E60FF">
              <v-icon left size="small">mdi-calendar</v-icon>
              Member since {{ formatDateShort(product.user.createdAt) }}
            </v-chip>
          </div>
        </div>

        <!-- Action Section -->
        <div class="owner-actions">
          <v-btn
            color="#4E60FF"
            class="contact-button"
            rounded="lg"
            @click="contactOwner"
          >
            <v-icon left>mdi-email-outline</v-icon>
            {{ $t('card_detail_owner_contact') }}
          </v-btn>
        </div>
      </div>
    </v-card>
  </div>
</v-window-item>

        <v-window-item value="reviews">
          <div class="tab-content pa-4">
            <h3 class="tab-title">{{ $t('card_detail_product_reviews') }}</h3>

            <!-- Add Comment Section -->
            <div class="comment-box mb-8">
              <!-- <h4 class="comment-title mb-4">Write your review</h4> -->
              <v-form @submit.prevent="submitComment">
                <v-rating
                  v-model="rating"
                  color="amber"
                  density="compact"
                  class="mb-4"
                ></v-rating>

                <v-textarea
                  v-model="newComment"
                  outlined
                  :label="$t('card_detail_input_placeholder')"
                  rows="3"
                  auto-grow
                  class="comment-textarea"
                  :rules="commentRules"
                ></v-textarea>

                <v-btn
                  color="primary"
                  type="submit"
                  :loading="commentLoading"
                  class="comment-submit-btn"
                >
                  <v-icon left>mdi-send</v-icon>
                  {{ $t('card_detail_post_review') }}
                </v-btn>
              </v-form>
            </div>
            <div class="review-filters">
              <v-chip
                v-for="filter in reviewFilters"
                :key="filter.value"
                :color="activeFilter === filter.value ? 'primary' : ''"
                class="mr-2"
                @click="changeFilter(filter.value)"
              >
                {{ filter.label }}
              </v-chip>
            </div>

            <div class="reviews-list">
              <div v-if="reviewsLoading" class="text-center py-4">
                <v-progress-circular indeterminate color="primary"></v-progress-circular>
              </div>

              <div
                v-for="review in filteredReviews"
                v-else
                :key="review.id"
                class="review-item"
              >
                <div class="review-header">
                  <v-avatar color="grey-lighten-2" size="40" class="mr-3">
                    <span class="avatar-text">{{ review.author.charAt(0) }}</span>
                  </v-avatar>
                  <div>
                    <div class="review-author">
                      {{ review.author }}
                    </div>
                    <div class="review-date">{{ review.date }}</div>
                  </div>
                  <v-rating
                    v-model="review.rating"
                    readonly
                    color="amber"
                    density="compact"
                    size="small"
                    class="ml-auto"
                  ></v-rating>
                </div>
                <div class="review-content">
                  {{ review.content }}
                </div>
                <div class="review-actions">
                  <v-btn
                    v-if="review.isCurrentUser"
                    variant="outlined"
                    color="primary"
                    size="small"
                    class="action-btn mr-2"
                    prepend-icon="mdi-pencil"
                    @click="openEditDialog(review)"
                  >
                    {{ $t('card_detail_edit_button') }}
                  </v-btn>

                  <v-btn
                    v-if="review.isCurrentUser"
                    variant="outlined"
                    color="error"
                    size="small"
                    class="action-btn"
                    prepend-icon="mdi-delete"
                    @click="deleteReview(review.id)"
                  >
                  {{ $t('card_detail_delete_button') }}
                  </v-btn>

                  <!-- <span v-if="!review.isCurrentUser" class="helpful-count">
                    {{ review.helpful }} people found this helpful
                  </span> -->

                </div>
              </div>

              <div v-if="!reviewsLoading && filteredReviews.length === 0" class="text-center py-4">
                {{ $t('card_detail_no_reviews_found') }}
              </div>
            </div>
          </div>
        </v-window-item>

        <v-window-item value="specs">
          <div class="tab-content pa-4">
            <h3 class="tab-title">{{ $t('card_detail_characteristics') }}</h3>
            <v-table density="compact">
              <tbody>
                <tr v-for="(spec, index) in parsedAttributes" :key="index">
                  <td class="spec-name">{{ spec.name }}</td>
                  <td class="spec-value">{{ spec.value }}</td>
                </tr>
              </tbody>
            </v-table>
          </div>
        </v-window-item>

        <v-window-item value="description">
          <div class="tab-content pa-4">
            <h3 class="tab-title">{{ $t('card_detail_product_description') }}</h3>
            <div class="product-description">
              <p>{{ product.description }}</p>
            </div>
          </div>

        </v-window-item>
      </v-window>
    </div>
  </v-container>


  <v-dialog v-model="showEditDialog" max-width="600">
  <v-card>
    <v-card-title class="text-h6">Edit Review</v-card-title>
    <v-card-text>
      <v-form @submit.prevent="updateReview">
        <v-rating
          v-model="editRating"
          color="amber"
          density="compact"
          class="mb-4"
        ></v-rating>

        <v-textarea
          v-model="editComment"
          outlined
          label="Edit your review"
          rows="3"
          auto-grow
          :rules="commentRules"
        ></v-textarea>
      </v-form>
    </v-card-text>
    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn
        color="grey"
        @click="showEditDialog = false"
      >
        Cancel
      </v-btn>
      <v-btn
        color="primary"
        @click="updateReview"
        :loading="editLoading"
      >
        Update
      </v-btn>
    </v-card-actions>
  </v-card>
</v-dialog>

<!-- Delete Confirmation Dialog -->
<v-dialog v-model="deleteDialog" max-width="500" persistent>
  <v-card class="delete-modal">
    <v-card-title class="d-flex align-center">
      <v-icon color="error" size="28" class="mr-2">mdi-alert-circle-outline</v-icon>
      <span class="text-h6 font-weight-medium">Confirm Deletion</span>
    </v-card-title>

    <v-divider></v-divider>

    <v-card-text class="pt-4 pb-2">
      <div class="d-flex align-start">
        <v-icon color="warning" class="mr-3">mdi-alert</v-icon>
        <div>
          <p class="text-body-1 mb-1">You are about to delete <strong>"{{ product?.name }}"</strong>.</p>
          <p class="text-caption text-medium-emphasis">This action will permanently remove the product and cannot be undone.</p>
        </div>
      </div>
    </v-card-text>

    <v-card-actions class="px-6 pb-4">
      <v-spacer></v-spacer>
      <v-btn
        variant="outlined"
        color="grey-darken-1"
        @click="deleteDialog = false"
        :disabled="deleteLoading"
      >
        Cancel
      </v-btn>
      <v-btn
        color="error"
        variant="flat"
        @click="confirmDelete"
        :loading="deleteLoading"
        prepend-icon="mdi-delete"
        class="ml-3"
      >
        Delete Permanently
      </v-btn>
    </v-card-actions>
  </v-card>
</v-dialog>

<!-- Success Notification -->
<v-snackbar
  v-model="deleteSuccess"
  color="success"
  :timeout="3000"
  location="bottom right"
  multi-line
  elevation="24"
>
  <div class="d-flex align-center">
    <v-icon color="white" class="mr-3">mdi-check-circle</v-icon>
    <div>
      <div class="text-subtitle-1 font-weight-medium">Success!</div>
      <div class="text-caption">"{{ product?.name }}" has been deleted successfully.</div>
    </div>
  </div>

  <template v-slot:actions>
    <v-btn
      variant="text"
      color="white"
      @click="deleteSuccess = false"
      icon="mdi-close"
    ></v-btn>
  </template>
</v-snackbar>

  <!-- <v-dialog v-model="showFavoriteDialog" width="400">
    <v-card>
      <v-card-title class="text-h6">Success</v-card-title>
      <v-card-text class="py-4">Product has been added to your favorites 💖</v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn color="primary" @click="showFavoriteDialog = false">OK</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog> -->

  </div>


</template>

<script setup>
import { ref, computed } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useRoute, useRouter } from 'vue-router';
import axios from "@/axios.js";
import { onMounted } from 'vue';
import { tr } from 'vuetify/locale';
import { watch } from 'vue';

const route = useRoute();
const router = useRouter();
const product = ref(null);
const loading = ref(true)



const itemId = route.params.id;


const tab = ref('description');
const mainImage = ref('');
const rating = ref(0);
const activeFilter = ref('all');
const isChangingImage = ref(false);

const newComment = ref('')
const commentLoading = ref(false)

// const showFavoriteDialog = ref(false);
const favoriteAdded = ref(false);
const isFavorite = ref(false);
const favoriteLoading = ref(false);
const authStore = useAuthStore();

const reviews = ref([]);
const reviewsLoading = ref(false);
const totalReviews = ref(0);


const editingReviewId = ref(null);
const editComment = ref('');
const editRating = ref(0);
const editLoading = ref(false);
const showEditDialog = ref(false);

const deleteDialog = ref(false);
const deleteLoading = ref(false);
const deleteSuccess = ref(false);



const contactOwner = () => {

  alert(`Contacting owner ${product.value.user.firstname} at ${product.value.user.phone}`);
};

const viewOwnerProfile = () => {

  alert(`Viewing profile of ${product.value.user.firstname}`);
};

const formatDateShort = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', { year: 'numeric', month: 'short' });
};

const confirmDelete = async () => {
  deleteLoading.value = true;

  try {
    const response = await axios.delete(
      `/api/items/delete/${route.params.id}`,
      {
        headers: {
          Authorization: `Bearer ${authStore.token}`
        }
      }
    );

    if (response.data.success) {
      deleteSuccess.value = true;

      setTimeout(() => {
        router.push('/');
      }, 2000);
    } else {
      alert('Failed to delete product: ' + (response.data.message || 'Unknown error'));
    }
  } catch (error) {
    console.error('Error deleting product:', error);
    alert('Failed to delete product. Please try again later.');
  } finally {
    deleteLoading.value = false;
    deleteDialog.value = false;
  }
};

const commentRules = [
  v => !!v || 'Comment is required',
  v => (v && v.length >= 10) || 'Comment must be at least 10 characters',
  v => (v && v.length <= 500) || 'Comment must be less than 500 characters'
]

const props = defineProps({
  id: {
    type: [String, Number],
    required: true
  }
});


const updateReview = async () => {
  if (!editComment.value || !editRating.value) {
    alert('Please provide both a rating and comment');
    return;
  }

  editLoading.value = true;

  try {
    const response = await axios.post(
      '/api/reviews/update',
      {
        id: editingReviewId.value,
        rating: editRating.value,
        itemId: route.params.id,
        comment: editComment.value
      },
      {
        headers: {
          Authorization: `Bearer ${authStore.token}`
        }
      }
    );

    if (response.data.success) {

      const index = reviews.value.findIndex(r => r.id === editingReviewId.value);
      if (index !== -1) {
        reviews.value[index] = {
          ...reviews.value[index],
          rating: editRating.value,
          content: editComment.value,
          type: getReviewType(editRating.value)
        };
      }


      if (product.value) {
        product.value.rate = calculateAverageRating();
      }

      showEditDialog.value = false;
      // alert('Review updated successfully!');
    }
  } catch (error) {
    console.error('Error updating review:', error);
    alert('Failed to update review. Please try again later.');
  } finally {
    editLoading.value = false;
  }
};

const openEditDialog = (review) => {
  editingReviewId.value = review.id;
  editComment.value = review.content;
  editRating.value = review.rating;
  showEditDialog.value = true;
};


const checkFavoriteStatus = async () => {
  if (!authStore.isAuthenticated) return;

  try {
    const response = await axios.get('/api/favourites', {
      headers: {
        Authorization: `Bearer ${authStore.token}`
      }
    });

    if (response.data.success && Array.isArray(response.data.body)) {

      isFavorite.value = response.data.body.some(
        favorite => favorite.item?.id?.toString() === route.params.id?.toString()
      );
      favoriteAdded.value = isFavorite.value;
    }
  } catch (error) {
    console.error('Error checking favorites:', error);
  }
};

const parsedAttributes = computed(() => {
  if (!product.value || !product.value.attributes) return [];

  try {
    const rawAttrs = product.value.attributes;

    const attrs = typeof rawAttrs === 'string' ? JSON.parse(rawAttrs) : rawAttrs;

    return Object.entries(attrs).map(([key, value]) => ({
      name: key,
      value
    }));
  } catch (e) {
    console.error('Failed to parse attributes:', e);
    return [];
  }
});


// const parsedAttributes = computed(() => {
//   if (!product.value || !product.value.attributes) return [];

//   try {
//     const attrs = JSON.parse(product.value.attributes);
//     return Object.entries(attrs).map(([key, value]) => ({
//       name: key,
//       value
//     }));
//   } catch (e) {
//     console.error('Failed to parse attributes:', e);
//     return [];
//   }
// });


const toggleFavorite = async () => {
  if (!authStore.isAuthenticated) {
    router.push('/login');
    return;
  }

  favoriteLoading.value = true;

  try {
    if (isFavorite.value) {
      await axios.delete(`/api/favourites/${route.params.id}`, {
        headers: {
          Authorization: `Bearer ${authStore.token}`
        }
      });
    } else {
      await axios.post(
        `/api/favourites?itemId=${route.params.id}`,
        null,
        {
          headers: {
            Authorization: `Bearer ${authStore.token}`
          }
        }
      );
    }

    isFavorite.value = !isFavorite.value;
    favoriteAdded.value = isFavorite.value;

    if (isFavorite.value) {
      // showFavoriteDialog.value = true;

      setTimeout(() => {
        favoriteAdded.value = false;
      }, 3000);
    }
  } catch (error) {
    console.error('Error toggling favorite:', error);
  } finally {
    favoriteLoading.value = false;
  }
};


const fetchReviews = async (sentiment = 'all') => {
  reviewsLoading.value = true;
  try {
    let url = `/open-api/reviews?itemId=${route.params.id}`;

    if (sentiment !== 'all') {
      url += `&sentiment=${sentiment}`;
    }

    const response = await axios.get(url);

    if (response.data.success) {
      reviews.value = response.data.body.content.map(review => {

        const currentUserId = authStore.user?.id;

        const reviewAuthorId = review.user?.id;


        const isCurrentUser = currentUserId && reviewAuthorId &&
                             currentUserId.toString() === reviewAuthorId.toString();

        return {
          id: review.id,
          author: isCurrentUser ? 'You' : review.user?.firstname || 'Anonymous',
          userId: review.user?.id,
          date: formatDate(review.createdAt),
          rating: review.rating,
          content: review.comment,
          helpful: 0,
          type: getReviewType(review.rating),
          isCurrentUser
        };
      });

      totalReviews.value = response.data.body.page.totalElements;
    }
  } catch (error) {
    console.error('Error fetching reviews:', error);
  } finally {
    reviewsLoading.value = false;
  }
};

const deleteReview = async (reviewId) => {
  if (!confirm('Are you sure you want to delete this review?')) return;

  try {
    const response = await axios.delete(
      `/api/reviews/${reviewId}`,
      {
        headers: {
          Authorization: `Bearer ${authStore.token}`
        }
      }
    );

    if (response.data.success) {
      reviews.value = reviews.value.filter(r => r.id !== reviewId);
      totalReviews.value -= 1;


      if (product.value) {
        product.value.rate = calculateAverageRating();
      }


    }
  } catch (error) {
    console.error('Error deleting review:', error);
    alert('Failed to delete review. Please try again later.');
  }
};

const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleDateString();
};


const getReviewType = (rating) => {
  if (rating >= 4) return 'positive';
  if (rating <= 2) return 'negative';
  return 'neutral';
};


const fetchProductDetails = async () => {
  try {
    const response = await axios.get(`/open-api/items/${route.params.id}`);
    if (response.data.success) {
      product.value = response.data.body;
      if (product.value.files && product.value.files.length) {
        mainImage.value = product.value.files[0].fileUrl;
      }
    }
  } catch (error) {
    console.error('Error fetching product details:', error);
  } finally {
    loading.value = false;
  }
};



onMounted(async () => {
  console.log('Current user:', authStore.user);
  await fetchProductDetails();
  await checkFavoriteStatus();
  await fetchReviews();
});

const submitComment = async () => {
  if (!authStore.isAuthenticated) {
    router.push('/login');
    return;
  }

  if (!newComment.value || !rating.value) {
    alert('Please provide both a rating and comment');
    return;
  }

  commentLoading.value = true;

  try {
    const response = await axios.post(
      '/api/reviews',
      {
        rating: rating.value,
        itemId: route.params.id,
        comment: newComment.value
      },
      {
        headers: {
          Authorization: `Bearer ${authStore.token}`
        }
      }
    );

    if (response.data.success) {

      const newReview = {
        id: response.data.body.id,
        author: 'You',
        userId: authStore.user.id,
        date: new Date().toLocaleDateString(),
        rating: rating.value,
        content: newComment.value,
        helpful: 0,
        type: rating.value >= 4 ? 'positive' : rating.value <= 2 ? 'negative' : 'neutral',
        isCurrentUser: true
      };

      reviews.value.unshift(newReview);
      newComment.value = '';
      rating.value = 0;


      if (product.value) {
        product.value.rate = calculateAverageRating();
      }

      // alert('Thank you for your review!');
    } else {
      alert('Failed to submit review: ' + (response.data.message || 'Unknown error'));
    }
  } catch (error) {
    console.error('Error submitting review:', error);
    alert('Failed to submit review. Please try again later.');
  } finally {
    commentLoading.value = false;
  }
};


const calculateAverageRating = () => {
  if (reviews.value.length === 0) return 0;
  const sum = reviews.value.reduce((acc, review) => acc + review.rating, 0);
  return sum / reviews.value.length;
};

const productImages = ref([
  'https://booking-image-upload-bucket.s3.eu-north-1.amazonaws.com/sedans/Honda+Accord+2021+%D0%B7%D0%B0%D0%B4%D0%BD%D0%B8%D0%B8%CC%86.jpg',
  'https://booking-image-upload-bucket.s3.eu-north-1.amazonaws.com/sedans/Honda+Accord+2021+%D0%B7%D0%B0%D0%B4%D0%BD%D0%B8%D0%B8%CC%86.jpg',
  'https://booking-image-upload-bucket.s3.eu-north-1.amazonaws.com/sedans/2021-Honda-Accord-Interior-1.jpg',
  'https://booking-image-upload-bucket.s3.eu-north-1.amazonaws.com/sedans/2021-Honda-Accord-Interior-1.jpg',
  'https://booking-image-upload-bucket.s3.eu-north-1.amazonaws.com/sedans/2021-Honda-Accord-Interior-1.jpg',
]);

const sellers = ref([
  { id: 1, name: 'TechMarket', rating: 4.8, reviews: 245 },

]);

import { useI18n } from 'vue-i18n';

const { t } = useI18n();

const reviewFilters = computed(() => [
  { value: 'all', label: t('card_detail_all_reviews') },
  { value: 'positive', label: t('card_detail_positive') },
  { value: 'negative', label: t('card_detail_negative') }
]);






const specifications = ref([
  { name: 'Engine', value: '2.0L i-VTEC, 160 hp' },
  { name: 'Transmission', value: 'CVT (Continuously Variable Transmission) or 6-speed manual' },
  { name: 'Drive', value: 'Front-wheel drive' },
  { name: '0-100 km/h Acceleration', value: '8.5 seconds' },
  { name: 'Fuel Consumption', value: '6.5 L/100 km' },
  { name: 'Trunk Capacity', value: '450 liters' },
  { name: 'Fuel Tank', value: '50 liters' },
  { name: 'Weight', value: '1,400 kg' }
]);



const filteredReviews = computed(() => {
  if (activeFilter.value === 'all') return reviews.value;
  return reviews.value.filter(review => {
    if (activeFilter.value === 'positive') return review.rating >= 4;
    if (activeFilter.value === 'negative') return review.rating <= 2;
    return review.rating === 3;
  });
});


const changeFilter = (filter) => {
  activeFilter.value = filter;
  fetchReviews(filter === 'all' ? 'all' : filter);
};

const changeMainImage = (image) => {
  isChangingImage.value = true;
  setTimeout(() => {
    mainImage.value = image;
    isChangingImage.value = false;
  }, 250);
};


watch(
  () => authStore.isAuthenticated,
  (newVal) => {
    if (newVal) fetchReviews();
  }
);


const navigateToBookingNow = (itemId) => {
  router.push({
    path: '/booking-now',
    query: { itemId }
  });
};


</script>

<style scoped>

.delete-modal {
  border-left: 4px solid rgb(var(--v-theme-error));
}

.delete-modal .v-card-title {
  background-color: rgba(var(--v-theme-error), 0.08);
  padding: 16px 24px;
}

.delete-modal .v-card-text {
  padding: 20px 24px;
}

.v-snackbar__wrapper {
  min-width: 300px;
}
.product-page {
  max-width: 1200px;
  margin-top: 100px;
  background-color: #F5F5F5;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.product-title {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 8px;
}

.rating-container {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.review-count {
  color: #757575;
  font-size: 14px;
}

.price-container {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 16px 0;
}

.current-price {
  font-size: 28px;
  font-weight: 700;
  color: #1976d2;
}

.old-price {
  font-size: 18px;
  color: #757575;
  text-decoration: line-through;
}

.discount {
  font-weight: 700;
}

.main-product-image {
  border-radius: 8px;
  background-color: #f5f5f5;
}

.thumbnail-container {
  display: flex;
  gap: 8px;
  margin-top: 8px;
}

.thumbnail {
  border-radius: 4px;
  cursor: pointer;
  border: 1px solid #e0e0e0;
  transition: border-color 0.2s;
}

.thumbnail-active {
  border-color: #1976d2;
}

.thumbnail:hover {
  border-color: #1976d2;
}

.product-tabs {
  margin-top: 40px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.tab-content {
  background-color: white;
}

.tab-title {
  margin-bottom: 20px;
  font-size: 20px;
  font-weight: 600;
}

.seller-card {
  transition: all 0.2s;
}

.seller-card:hover {
  border-color: #1976d2;
}

.seller-name {
  font-weight: 600;
}

.seller-rating {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #757575;
}

.review-filters {
  margin-bottom: 20px;
}

.review-item {
  padding: 16px 0;
  border-bottom: 1px solid #e0e0e0;
}

.review-header {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}


.comment-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.comment-textarea {
  background: white;
  border-radius: 8px;
}

.comment-textarea :deep(.v-input__slot) {
  min-height: 120px;
}

.comment-submit-btn {
  text-transform: none;
  letter-spacing: normal;
  font-weight: 500;
  border-radius: 8px;
  padding: 0 24px;
  height: 42px;
}

.avatar-text {
  font-weight: 600;
}

.review-author {
  font-weight: 600;
}

.review-date {
  font-size: 12px;
  color: #757575;
}

.review-content {
  margin: 8px 0 12px 52px;
  line-height: 1.5;
}

.review-actions {
  display: flex;
  align-items: center;
  margin-left: 50px;
}

.helpful-count {
  font-size: 12px;
  color: #757575;
  margin-right: 12px;
}

.spec-name {
  font-weight: 600;
  color: #616161;
  width: 40%;
}

.spec-value {
  font-weight: 500;
}

.product-description {
  line-height: 1.6;
}

.product-description p {
  margin-bottom: 12px;
}

.buy-btn {
  font-weight: 600;
  letter-spacing: normal;
}


.owner-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 16px;
}

.owner-header {
  margin-bottom: 32px;
  text-align: center;
}

.owner-title {
  font-size: 1.75rem;
  font-weight: 700;
  color: #2B2B43;
  margin-bottom: 12px;
  position: relative;
  display: inline-block;
}

.owner-title:after {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 3px;
  background: linear-gradient(90deg, #4E60FF 0%, #8B54FF 100%);
  border-radius: 3px;
}

.owner-card {
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(78, 96, 255, 0.1) !important;
  overflow: hidden;
  border: 1px solid rgba(237, 238, 242, 0.8);
  background: white;
  transition: all 0.3s ease;
}

.owner-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 35px rgba(78, 96, 255, 0.15) !important;
}

.owner-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 32px;
}

.owner-avatar-wrapper {
  position: relative;
  margin-bottom: 24px;
}

.owner-avatar {
  box-shadow: 0 10px 20px rgba(78, 96, 255, 0.2);
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.owner-avatar:hover {
  transform: scale(1.05);
  box-shadow: 0 15px 30px rgba(78, 96, 255, 0.3);
}

.avatar-initials {
  font-size: 2.5rem;
  font-weight: 600;
  letter-spacing: 1px;
}

.verified-badge {
  position: absolute;
  bottom: -6px;
  left: 50%;
  transform: translateX(-50%);
  background: #4E60FF;
  color: white;
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 6px;
  box-shadow: 0 5px 15px rgba(78, 96, 255, 0.25);
  z-index: 2;
}

.owner-details {
  text-align: center;
  margin-bottom: 24px;
}

.owner-name {
  font-size: 1.5rem;
  font-weight: 700;
  color: #2B2B43;
  margin-bottom: 16px;
  position: relative;
}

.owner-name:after {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 40px;
  height: 2px;
  background: #4E60FF;
  border-radius: 2px;
}

.owner-contact-info {
  display: flex;
  flex-direction: column;
  gap: 14px;
  margin-bottom: 20px;
}

.contact-item {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  color: #545563;
  font-size: 1rem;
  font-weight: 500;
}

.owner-meta {
  display: flex;
  justify-content: center;
}

.member-chip {
  font-size: 0.875rem;
  font-weight: 600;
  padding: 6px 12px;
}

.owner-actions {
  width: 100%;
  max-width: 240px;
}

.contact-button {
  text-transform: none;
  letter-spacing: normal;
  font-weight: 600;
  height: 48px;
  width: 100%;
  font-size: 1rem;
  box-shadow: 0 8px 20px rgba(78, 96, 255, 0.2);
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.contact-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 25px rgba(78, 96, 255, 0.3);
}

@media (min-width: 960px) {
  .owner-content {
    flex-direction: row;
    align-items: center;
    padding: 40px;
  }

  .owner-avatar-wrapper {
    margin-bottom: 0;
    margin-right: 40px;
  }

  .owner-details {
    text-align: left;
    flex-grow: 1;
    margin-bottom: 0;
  }

  .owner-contact-info {
    align-items: flex-start;
    justify-content: flex-start;
  }

  .owner-actions {
    margin-left: 40px;
    max-width: none;
    width: auto;
  }

  .contact-button {
    width: auto;
    padding: 0 32px;
  }
}
</style>
