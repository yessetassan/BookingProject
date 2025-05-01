<template>

  <Header/>
  <v-container class="pa-6 mt-15">
    <!-- Page Header -->
    <v-row class="mb-6">
      <v-col cols="12">
        <h1 class="text-h4 font-weight-bold text-primary">Approved Inventory</h1>
        <p class="text-subtitle-1 text-grey-darken-1 mt-2">
          Browse our premium selection of verified items
        </p>
      </v-col>
    </v-row>

        <!-- Search Bar -->
    <v-row class="mb-6">
      <v-col cols="12">
        <v-card flat class="search-card" elevation="1">
          <v-text-field
            v-model="searchQuery"
            variant="outlined"
            placeholder="Search rejected items..."
            prepend-inner-icon="mdi-magnify"
            hide-details
            single-line
            clearable
            @click:clear="clearSearch"
            @keyup.enter="fetchApprovedItems"
            class="search-field"
          >
            <template v-slot:append>
              <v-btn
                color="error"
                variant="flat"
                @click="fetchApprovedItems"
                :disabled="!searchQuery"
                class="search-button"
              >
                Search
              </v-btn>
            </template>
          </v-text-field>
        </v-card>
      </v-col>
    </v-row>

    <!-- Search Results -->
    <v-alert
      v-if="isSearching"
      type="info"
      variant="tonal"
      class="mb-4"
      rounded="lg"
    >
      <template v-slot:prepend>
        <v-icon color="info">mdi-information</v-icon>
      </template>
      Showing results for: <strong>"{{ searchQuery }}"</strong>
      <v-btn
        variant="text"
        color="error"
        @click="clearSearch"
        class="ml-2"
        size="small"
      >
        Clear search
      </v-btn>
    </v-alert>

    <v-row>
      <!-- Categories Column -->
      <v-col cols="12" md="3" class="pr-md-4">
        <!-- Categories Card -->
        <v-card flat class="mb-6" elevation="0">
          <v-card-title class="text-h5 font-weight-medium px-0 mb-4">
            <v-icon color="primary" class="mr-2">mdi-shape-outline</v-icon>
            Categories
          </v-card-title>
          <v-card-text class="px-0">
            <div class="category-container">
              <div v-for="category in allSubcategories" :key="category.id">
                <CategoryItem :category="category" @selectCategory="selectCategory" />
              </div>
            </div>
          </v-card-text>
        </v-card>

        <!-- Filters Card -->
        <v-card flat elevation="0" v-if="selectedCategory && activeFilters.length > 0">
          <v-card-title class="text-h5 font-weight-medium px-0 mb-4">
            <v-icon color="primary" class="mr-2">mdi-filter</v-icon>
            Refine Results
          </v-card-title>
          <v-card-text class="px-0">
            <div v-for="filter in activeFilters" :key="filter.name" class="mb-6">
              <div class="text-subtitle-1 font-weight-medium mb-3 text-grey-darken-2">
                {{ filter.name }}
              </div>

              <!-- Multi-select Checkbox Filter -->
              <div v-if="filter.type === 'multi-select'">
                <v-checkbox
                  v-for="option in filter.options"
                  :key="option.value"
                  :label="option.value"
                  :value="option.value"
                  v-model="selectedFilters[filter.name]"
                  density="compact"
                  hide-details
                  class="my-0 checkbox-small"
                  color="primary"
                  @change="fetchApprovedItems"
                ></v-checkbox>
              </div>

              <!-- Range Filter -->
              <div v-else-if="filter.type === 'range'">
                <v-range-slider
                  v-model="selectedFilters[filter.name]"
                  :min="filter.min"
                  :max="filter.max"
                  :step="filter.step || 1000"
                  thumb-label="always"
                  thumb-size="24"
                  track-color="primary-lighten-1"
                  color="primary"
                  hide-details
                  class="mt-2"
                  @change="fetchApprovedItems"
                ></v-range-slider>
                <v-row justify="space-between" class="mt-1">
                  <v-col cols="6" class="pa-0 pr-1">
                    <v-text-field
                      v-model="selectedFilters[filter.name][0]"
                      density="compact"
                      variant="outlined"
                      hide-details
                      single-line
                      class="text-center"
                      inputmode="numeric"
                      prefix="$"
                      style="appearance: textfield;"
                      @change="fetchApprovedItems"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="6" class="pa-0 pl-1">
                    <v-text-field
                      v-model="selectedFilters[filter.name][1]"
                      density="compact"
                      variant="outlined"
                      hide-details
                      single-line
                      class="text-center"
                      inputmode="numeric"
                      prefix="$"
                      style="appearance: textfield"
                      @change="fetchApprovedItems"
                    ></v-text-field>
                  </v-col>
                </v-row>
              </div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>


      <v-col cols="12" md="9">
        <v-card flat elevation="0">
          <v-card-title class="text-h5 font-weight-medium px-0 mb-4">
            <v-icon color="primary" class="mr-2">mdi-check-circle</v-icon>
            Approved Items
            <v-chip
              v-if="selectedCategory"
              color="grey-lighten-5"
              variant="flat"
              class="ml-3 text-primary font-weight-medium"
              size="small"
            >
              {{ selectedCategory.name }}
            </v-chip>
            <v-chip
              color="grey-lighten-4"
              variant="flat"
              class="ml-2 text-grey-darken-2"
              size="small"
            >
              {{ items.length }} items
            </v-chip>
          </v-card-title>

          <v-card-text class="px-0">

            <v-row v-if="loading">
              <v-col cols="12" class="text-center">
                <v-progress-circular indeterminate color="primary"></v-progress-circular>
              </v-col>
            </v-row>

            <!-- Items Grid -->
            <v-row v-else-if="items.length > 0">
              <v-col
                v-for="item in items"
                :key="item.id"
                cols="12"
                sm="6"
                lg="4"
              >
                <v-card class="item-card" elevation="2" rounded="xl">
                  <div class="image-container">
                    <v-img
                      height="200"
                      :src="item.files?.[0]?.fileUrl || 'https://via.placeholder.com/300'"
                      cover
                      class="item-image"
                      gradient="to bottom, rgba(0,0,0,0.1), rgba(0,0,0,0.3)"
                    >
                      <v-chip
                        v-if="item.available"
                        color="green-lighten-5"
                        variant="flat"
                        class="ma-2 text-green-darken-2 font-weight-bold"
                        size="small"
                      >
                        Available Now
                      </v-chip>
                      <v-chip
                        v-else
                        color="orange-lighten-5"
                        variant="flat"
                        class="ma-2 text-orange-darken-2 font-weight-bold"
                        size="small"
                      >
                        Booked
                      </v-chip>
                    </v-img>
                  </div>

                  <v-card-title class="text-subtitle-1 font-weight-bold px-4 pt-4">
                    {{ item.name }}
                  </v-card-title>
                  <v-card-subtitle class="text-caption px-4 text-grey-darken-1">
                    {{ item.description }}
                  </v-card-subtitle>

                  <v-card-text class="px-4">
                    <div class="d-flex align-center mb-2">
                      <v-icon color="primary" size="small" class="mr-1">mdi-currency-kzt</v-icon>
                      <span class="text-subtitle-2 font-weight-medium text-primary">
                        {{ item.pricePerDay?.toLocaleString() }} <span class="text-caption text-grey">/day</span>
                      </span>
                    </div>
                  </v-card-text>

                  <v-card-actions class="px-4 pb-4">
                    <v-btn
                      color="primary"
                      variant="outlined"
                      size="small"
                      prepend-icon="mdi-eye"
                      rounded="lg"
                      class="font-weight-bold"
                      @click="viewItemDetails(item.id)"
                    >
                      View
                    </v-btn>
                    <v-spacer></v-spacer>
                    <v-chip
                      color="green-lighten-5"
                      variant="flat"
                      size="small"
                      class="text-green-darken-2 font-weight-bold"
                    >
                      <v-icon size="small" class="mr-1">mdi-check-decagram</v-icon>
                      Approved
                    </v-chip>
                  </v-card-actions>
                </v-card>
              </v-col>
              <!-- Pagination -->
              <v-col cols="12" class="mt-4">
                  <div class="pagination-container" v-if="totalPages > 1">
                    <span
                      style="border-left: 0.7px #9e9e9e solid;"
                      :class="['pagination-item', { 'disabled': currentPage === 0 }]"
                      @click="currentPage > 0 && handlePageChange(currentPage - 1)"
                    >
                      ← Previous
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
                      Next →
                    </span>
                  </div>
                </v-col>
            </v-row>

            <!-- Empty State -->
            <v-alert
              v-else
              type="info"
              variant="tonal"
              class="my-6"
              rounded="lg"
            >
              <template v-slot:prepend>
                <v-icon color="info">mdi-information</v-icon>
              </template>
              <div class="text-h6 font-weight-medium mb-2">No approved items found</div>
              <div>Try adjusting your filters or select a different category</div>
            </v-alert>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import Header from '@/components/Header.vue';
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import CategoryItem from './CategoryItem.vue'
import axios from "@/axios.js";


const router = useRouter()

// Data
const categories = ref([])
const filters = ref([])
const items = ref([])
const loading = ref(false)
const selectedCategory = ref(null)
const selectedFilters = ref({})


const totalPages = ref(1);
const currentPage = ref(0);
const itemsPerPage = ref(9);

const searchQuery = ref('');

const isSearching = computed(() => {
  return searchQuery.value.length > 0;
});


const clearSearch = () => {
  searchQuery.value = '';
  fetchApprovedItems();
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

const handlePageChange = (page) => {
  currentPage.value = page;
  fetchApprovedItems();
};


const fetchCategories = async () => {
  try {
    const token = localStorage.getItem('authToken');
    const response = await axios.get('/open-api/categories', {
      headers: { 'Authorization': `Bearer ${token}` }
    });

    if (response.data.success) {
      categories.value = response.data.body;
    }
  } catch (error) {
    console.error('Error fetching categories:', error);
  }
};


const fetchFilters = async (categoryId) => {
  try {
    const token = localStorage.getItem('authToken');
    const response = await axios.get(
      `/open-api/filters/category/${categoryId}`,
      { headers: { 'Authorization': `Bearer ${token}` } }
    );


    filters.value = response.data.body.map(filter => {
      const options = JSON.parse(filter.options).values;
      return {
        ...filter,
        options: options.map(value => ({ value }))
      };
    });


    filters.value.forEach(filter => {
      if (filter.type === 'multi-select') {
        selectedFilters.value[filter.name] = [];
      } else if (filter.type === 'range') {
        selectedFilters.value[filter.name] = [filter.min, filter.max];
      }
    });
  } catch (error) {
    console.error('Error fetching filters:', error);
    filters.value = [];
  }
};


const fetchApprovedItems = async () => {
  try {
    loading.value = true;
    const token = localStorage.getItem('authToken');


    const filterAttributes = Object.entries(selectedFilters.value)
      .filter(([_, value]) => value.length > 0)
      .map(([name, values]) => {
        const filter = filters.value.find(f => f.name === name);
        if (!filter) return null;

        return {
          name,
          type: filter.type,
          options: JSON.stringify({ values })
        };
      })
      .filter(Boolean);

      const payload = {
        page: currentPage.value,
        size: itemsPerPage.value,
        name: searchQuery.value,
        categoryId: selectedCategory.value?.id || null,
        filterAttributes
      };

    const response = await axios.post(
      '/api/items/approved',
      payload,
      {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      }
    );

    items.value = response.data.body?.content || [];

    totalPages.value = response.data.body?.page?.totalPages ||
                  Math.ceil(response.data.body?.totalElements / itemsPerPage.value) ||
                  1;


    if (currentPage.value >= totalPages.value) {
      currentPage.value = Math.max(0, totalPages.value - 1);
    }
  } catch (error) {
    console.error('Error fetching approved items:', error);
  } finally {
    loading.value = false;
  }
};


const selectCategory = (category) => {
  selectedCategory.value = typeof category === 'object' ? category :
    findCategoryInTree(categories.value, category);

  if (selectedCategory.value) {
    fetchFilters(selectedCategory.value.id);
  } else {
    filters.value = [];
    selectedFilters.value = {};
  }
  fetchApprovedItems();
};


const findCategoryInTree = (categories, idOrName) => {
  if (!categories) return null;


  const items = Array.isArray(categories) ? categories : [categories];

  for (const cat of items) {
    if (cat.id === idOrName || cat.name === idOrName) return cat;
    if (cat.subcategories) {
      const found = findCategoryInTree(cat.subcategories, idOrName);
      if (found) return found;
    }
  }
  return null;
};


const allSubcategories = computed(() => {
  return categories.value.subcategories || [];
});

const activeFilters = computed(() => {
  return selectedCategory.value ? filters.value : [];
});

// item details
const viewItemDetails = (id) => {
  router.push(`/my-check-items/${id}`)
}


onMounted(() => {
  fetchCategories();
  fetchApprovedItems();
});
</script>

<style scoped>

.search-card {
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid rgba(244, 67, 54, 0.1);
}

.search-field {
  background-color: #fff;
}

.search-field .v-field__outline {
  color: rgba(244, 67, 54, 0.2);
}

.search-field .v-field--focused .v-field__outline {
  color: rgba(244, 67, 54, 0.5);
}

.search-button {
  height: 48px !important;
  margin-right: -12px !important;
  border-radius: 0 8px 8px 0 !important;
  box-shadow: none !important;
}


.v-text-field {
  transition: all 0.3s ease;
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


.category-container {
  border-left: 2px solid #f5f5f5;
  padding-left: 12px;
}

.item-card {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid rgba(25, 118, 210, 0.1);
}

.item-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 16px rgba(25, 118, 210, 0.1) !important;
}

.image-container {
  position: relative;
  overflow: hidden;
  border-radius: 12px 12px 0 0;
}

.item-image {
  transition: transform 0.3s ease;
}

.item-card:hover .item-image {
  transform: scale(1.03);
}

.v-card-title {
  letter-spacing: -0.25px;
}

.v-chip {
  transition: all 0.2s ease;
}

.v-chip:hover {
  transform: translateY(-1px);
}

.text-primary {
  color: #1867C0;
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
</style>
