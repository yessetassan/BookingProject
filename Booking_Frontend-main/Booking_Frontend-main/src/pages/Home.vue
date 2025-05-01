<template>

  <Header/>

  <SearchBar @search="handleSearch"/>
  <div>
    <v-container>
      <v-row>
        <v-col cols="3" class="sidebar">
          <p class="font-weight-bold text-primary mb-2">{{ allCategoryName }}</p>
          <div v-for="category in allSubcategories" :key="category.id">
            <CategoryItem :category="category" @selectCategory="handleCategorySelect" />
          </div>


          <div v-if="selectedCategoryFilters.length > 0">
            <v-divider class="my-2"></v-divider>
            <div v-for="filter in selectedCategoryFilters" :key="filter.name">
              <v-card flat class="mb-3">
                <v-card-title class="text-subtitle-1 font-weight-bold pa-0 mb-2">{{ filter.name }}</v-card-title>
                <v-card-text class="pa-0">
                  <div v-if="filter.type === 'multi-select'">
                    <v-checkbox
                      v-for="option in filter.options.values"
                      :key="option"
                      :label="option"
                      :value="option"
                      v-model="selectedFilters[filter.name]"
                      density="compact"
                      hide-details
                      class="my-0 checkbox-small"
                      @change="handleFilterChange"
                    ></v-checkbox>
                  </div>
                  <div v-else-if="filter.type === 'range'">
                    <v-range-slider
                      v-model="selectedFilters[filter.name]"
                      :min="filter.options.min"
                      :max="filter.options.max"
                      :step="1"
                      thumb-label="always"
                      thumb-size="20"
                      track-color="primary"
                      color="primary"
                      hide-details
                      class="mt-2"
                      @change="handleFilterChange"
                    ></v-range-slider>
                    <v-row justify="space-between" class="mt-1">
                      <v-col cols="6" class="pa-0 pr-1">
                        <v-text-field
                          :width="100"
                          v-model="selectedFilters[filter.name][0]"
                          density="compact"
                          variant="outlined"
                          hide-details
                          single-line
                          class="text-center"
                          inputmode="numeric"
                          style="appearance: textfield; margin-left: 10px;"
                          @change="handleFilterChange"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="6" class="pa-0 pl-1">
                        <v-text-field
                          :width="100"
                          v-model="selectedFilters[filter.name][1]"
                          density="compact"
                          variant="outlined"
                          hide-details
                          single-line
                          class="text-center"
                          inputmode="numeric"
                          style="appearance: textfield"
                          @change="handleFilterChange"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                  </div>
                </v-card-text>
              </v-card>
            </div>
          </div>
        </v-col>


        <v-col cols="8" style="margin-left: 30px;">
          <v-row class="ma-0">
            <v-col v-for="card in cards" :key="card.id" cols="12" md="6" lg="4">
              <CardItem :card="card" :initial-favorite="card.isFavorite || favorites.some(item => item.item.id === card.id)"
              @favorite-toggled="handleFavoriteToggle"/>
            </v-col>
          </v-row>
          <div class="pagination-container mt-4" v-if="totalPages > 1">
            <span
              style="border-left: 0.7px #9e9e9e solid;"
              :class="['pagination-item', { 'disabled': currentPage === 0 }]"
              @click="currentPage > 0 && handlePageChange(currentPage - 1)"
            >
              ← Предыдущая
            </span>
            <span
              v-for="page in visiblePages"
              :key="page"
              :class="['pagination-item', { 'active': currentPage === page }]"
              @click="handlePageChange(page)"
            >
              {{ page + 1 }}
            </span>
            <span
              :class="['pagination-item', { 'disabled': currentPage >= totalPages - 1 }]"
              @click="currentPage < totalPages - 1 && handlePageChange(currentPage + 1)"
            >
              Следующая →
            </span>
          </div>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'


const authStore = useAuthStore();
import axios from "@/axios.js";
import CategoryItem from '../components/CategoryItem.vue';
import CardItem from '../components/CardItem.vue';
import SearchBar from '../components/SearchBar.vue';
import Header from '../components/Header.vue';
import { ref, computed, onMounted } from 'vue';
import { watch } from 'vue';


const categories = ref([]);
const selectedCategoryId = ref(null);



const fetchCategories = async () => {
  try {

    const response = await axios.get('/open-api/categories');
    console.log('API Response:', response.data);

    if (response.data.success) {
      categories.value = response.data.body;
    }
  } catch (error) {
    console.error('Error fetching categories:', error);
  }
};


const allCategoryName = computed(() => {
  return categories.value.name;
});


const allSubcategories = computed(() => {
  return categories.value.subcategories || [];
});





onMounted(() => {
  const savedPage = localStorage.getItem('currentPage');
  if (savedPage !== null) {
    currentPage.value = parseInt(savedPage);
  }
  fetchCategories();
  fetchCards(currentPage.value);
});

const cards = ref([]);
const totalPages = ref(1);
const currentPage = ref(0);
const itemsPerPage = 9;

const favorites = ref([]);




const fetchCards = async (page = currentPage.value, categoryId = null, searchQuery = null) => {
  try {
    const payload = {
      page: page,
      size: itemsPerPage,
      ...(searchQuery ? { name: searchQuery } : {}),
      categoryId: categoryId,
      filterAttributes: Object.keys(selectedFilters.value)
        .filter((filterName) => selectedFilters.value[filterName]?.length)
        .map((filterName) => ({
          name: filterName.toLowerCase(),
          type: "multi-select",
          options: JSON.stringify({ values: selectedFilters.value[filterName] })
        })),
    };

    const response = await axios.post("/open-api/items/search", payload);

    if (response.data.success) {
      cards.value = response.data.body.content.map((item, index) => ({
        id: item.id || index + 1,
        ...item
      }));

      if (authStore.isAuthenticated) {
        await fetchFavorites();
      }


      totalPages.value = response.data.body.page?.totalPages ||
                         Math.ceil(response.data.body.page?.totalElements / itemsPerPage) ||
                         1;

      if (currentPage.value >= totalPages.value) {
        currentPage.value = totalPages.value - 1;
      }
    }
  } catch (error) {
    console.error("Error fetching cards:", error);
  }
};

const fetchFavorites = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/favourites', {
      headers: {
        Authorization: `Bearer ${authStore.token}`
      }
    });

    if (Array.isArray(response.data.body)) {
      favorites.value = response.data.body;

      cards.value = cards.value.map(card => ({
        ...card,
        isFavorite: response.data.body.some(fav => fav.item.id === card.id)
      }));
    }
  } catch (error) {
    console.error('Error fetching favorites:', error);
  }
};

const handleFavoriteToggle = (cardId, newFavoriteStatus) => {
  const cardIndex = cards.value.findIndex(c => c.id === cardId);
  if (cardIndex !== -1) {
    cards.value[cardIndex].isFavorite = newFavoriteStatus;
  }


  if (newFavoriteStatus) {

    favorites.value.push({ item: { id: cardId } });
  } else {

    favorites.value = favorites.value.filter(fav => fav.item.id !== cardId);
  }
};

watch(() => authStore.isAuthenticated, (isAuthenticated) => {
  if (isAuthenticated) {
    fetchFavorites();
  } else {
    favorites.value = [];
  }
}, { immediate: true });



// const fetchCards = async (page = 0, categoryId = null, searchQuery = null) => {
//   try {
//     const payload = {
//       page: page,
//       size: itemsPerPage,
//       ...(searchQuery ? { name: searchQuery } : {}),
//       categoryId: categoryId,
//       filterAttributes: selectedFilters.value["brand"]?.length
//         ? [
//             {
//               name: "brand",
//               type: "multi-select",
//               options: JSON.stringify({ values: selectedFilters.value["brand"] }),
//             },
//           ]
//         : [], // Если "brand" пустой, отправляем пустой массив (можно вообще не отправлять)
//     };

//     console.log("Payload:", payload);

//     const response = await axios.post("http://localhost:8080/open-api/items/search", payload);
//     console.log("Cards API Response:", response.data);

//     if (response.data.success) {
//       cards.value = response.data.body.content;
//       totalPages.value = response.data.body.totalPages;
//     }
//   } catch (error) {
//     console.error("Error fetching cards:", error);
//   }
// };


const handleSearch = (query) => {
  fetchCards(0, selectedCategoryId.value, query);
};


const handleCategorySelect = (categoryId) => {
  selectedCategoryId.value = categoryId;
  fetchCards(0, categoryId);
  fetchFilters(categoryId);
};


const handlePageChange = (page) => {
  currentPage.value = page;
  fetchCards(page, selectedCategoryId.value);
};

const handleFilterChange = () => {
  fetchCards(0, selectedCategoryId.value);
};


const selectedCategoryFilters = ref([]);
const selectedFilters = ref({});

const fetchFilters = async (categoryId) => {
  try {
    const response = await axios.get(`/open-api/filters/category/${categoryId}`);
    console.log('Filters API Response:', response.data);

    if (response.data.success) {
      const parsedFilters = response.data.body.map(filter => ({
        ...filter,
        options: JSON.parse(filter.options),
      }));
      selectedCategoryFilters.value = parsedFilters;
      initializeFilters(parsedFilters);
    }
  } catch (error) {
    console.error('Error fetching filters:', error);
  }
};


const initializeFilters = (filters) => {
  selectedFilters.value = {};
  filters.forEach(filter => {
    if (filter.type === 'multi-select') {
      selectedFilters.value[filter.name] = [];
    } else if (filter.type === 'range') {
      selectedFilters.value[filter.name] = [filter.options.min, filter.options.max];
    }
  });
};

const visiblePages = computed(() => {
  if (totalPages.value <= 1) return [];

  const pages = [];
  const maxVisible = 5;
  let start = Math.max(currentPage.value - Math.floor(maxVisible / 2), 0);
  let end = Math.min(start + maxVisible - 1, totalPages.value - 1);

  if (end - start + 1 < maxVisible) {
    start = Math.max(end - maxVisible + 1, 0);
  }

  for (let i = start; i <= end; i++) {
    pages.push(i);
  }

  return pages;
});

// watch([currentPage, totalPages], ([newPage, newTotal]) => {
//   console.log(`Page changed to ${newPage + 1} of ${newTotal}, showing ${cards.value.length} items`);
// });
watch(currentPage, (newPage) => {
  localStorage.setItem('currentPage', newPage);
});
</script>

<style scoped>
.sidebar {
  padding: 16px;
  width: 100%;
}

.v-card {
  padding: 15px;
}


.sidebar .v-card {
  box-shadow: none !important;
}


.v-card-text {
  padding: 0 !important;
}

.v-checkbox {
  margin-top: -10px !important;
}


.v-card-title {
  font-size: 14px !important;
}

.v-checkbox >>> .v-label {
  font-size: 14px !important;
}

.v-text-field >>> input {
    font-size: 0.8em;

}

.v-text-field {
  margin-bottom: 15px;
}


.checkbox-small >>> .v-input__control {
  min-height: 24px !important;
}

.checkbox-small >>> .v-selection-control {
  padding: 0 !important;
}

.checkbox-small >>> .v-icon {
  font-size: 16px !important;
}


.pagination-container {
  display: flex;
  justify-content: start;
  align-items: center;
  gap: 0;
  font-size: 14px;

  border-radius: 4px;
  padding: 4px;
}

.pagination-item {
  cursor: pointer;
  padding: 4px 16px;
  color: #333;
  transition: background-color 0.2s, color 0.2s;
  border-right: 1px solid #e6e6e6;
}


.pagination-item:last-child {
  border-right: none;
}

.pagination-item:hover {
  background-color: #f0f0f0;
}

.pagination-item.active {
  color: #1976d2;
  font-weight: bold;
  background-color: #e6e6e6;
  border: none;
}

.pagination-item.disabled {
  color: #9e9e9e;
  cursor: not-allowed;
  pointer-events: none;
}
</style>
