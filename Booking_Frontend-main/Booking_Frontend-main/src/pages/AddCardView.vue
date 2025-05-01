<template>
  <div class="booking-hierarchy">

    <div class="hero-section">
      <Header/>
      <!-- <div class="hero-content">
        <h1 class="hero-title">Explore Our Products</h1>
        <p class="hero-subtitle">Select a category to find what you need</p>
      </div> -->
    </div>

    <!-- Main Content -->
    <div class="main-content">
      <v-container>

        <div class="navigation-path">
          <v-chip
            v-for="(item, index) in navigationStack"
            :key="item.id"
            class="path-chip"
            :color="index === navigationStack.length - 1 ? 'primary' : 'grey lighten-3'"
            :text-color="index === navigationStack.length - 1 ? 'white' : 'grey darken-2'"
            @click="navigateTo(index)"
          >
            <v-icon left small>{{ getCategoryIcon(item.name) }}</v-icon>
            {{ item.name }}
          </v-chip>
        </div>

        <!-- Categories Grid -->
        <div class="categories-grid">
          <div
            v-for="item in currentLevelItems"
            :key="item.id"
            class="category-card"
            :class="{ 'leaf-category': item.subcategories.length === 0 }"
            @click="selectCategory(item)"
          >
            <div class="card-content">
              <div class="category-icon">
                <v-icon x-large>{{ getCategoryIcon(item.name) }}</v-icon>
              </div>
              <div class="category-info">
                <h3 class="category-name">{{ item.name }}</h3>
                <p v-if="item.subcategories.length > 0" class="subcategory-count">
                  {{ item.subcategories.length }} {{ item.subcategories.length === 1 ? 'subcategory' : 'subcategories' }}
                </p>
                <p v-else class="leaf-indicator">Select to add product</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Product Form -->
        <v-expand-transition>
          <div v-if="selectedLeafCategory" class="product-form-container">
            <div class="form-header">
              <h2>{{ $t('add_item_shablon') }} {{ selectedLeafCategory.name }}</h2>
              <v-btn icon @click="selectedLeafCategory = null">
                <v-icon>mdi-close</v-icon>
              </v-btn>
            </div>

            <v-card class="form-card">
              <v-row>

                <v-col cols="12" md="6">
                  <div class="form-section">
                    <v-text-field
                      v-model="product.name"
                      :label="$t('add_item_product_name')"
                      outlined
                      clearable
                      prepend-inner-icon="mdi-tag-outline"
                      class="form-input"
                    ></v-text-field>

                    <v-text-field
                      v-model="product.price"
                      :label="$t('add_item_price_per_day')"
                      outlined
                      type="number"
                      prefix="tg"
                      prepend-inner-icon="mdi-cash"
                      class="form-input"
                    ></v-text-field>

                    <v-textarea
                      v-model="product.description"
                      :label="$t('add_item_description')"
                      outlined
                      rows="3"
                      prepend-inner-icon="mdi-text-box-outline"
                      class="form-input"
                    ></v-textarea>



                    <div class="location-selector">
                      <h3 class="section-title">{{ $t('add_item_location') }}</h3>
                      <v-select
                      v-model="selectedDistrict"
                      :items="formattedDistricts"
                      item-title="text"
                      item-value="value"
                      :label="$t('add_item_select_district')"
                      outlined
                      clearable
                      prepend-inner-icon="mdi-map-marker"
                      @change="fetchMicroDistricts"
                      class="form-input"
                    />

                    <v-select
                      v-model="product.location.microDistrictId"
                      :items="formattedMicroDistricts"
                      item-title="text"
                      item-value="value"
                      :label="$t('add_item_select_micro_district')"
                      outlined
                      clearable
                      prepend-inner-icon="mdi-map-marker-outline"
                      :disabled="!selectedDistrict"
                      class="form-input"
                    />

                    <v-text-field
                      v-model="product.fullAddress"
                      :label="$t('add_item_street_address')"
                      outlined
                      prepend-inner-icon="mdi-home-map-marker"
                      class="form-input"
                      :hint="$t('add_item_example_street')"
                      persistent-hint
                    ></v-text-field>

                    <div class="selected-location-preview" v-if="selectedDistrict && product.location.microDistrictId">
                      <v-chip small class="mr-1">
                        <v-icon small left>mdi-map-marker</v-icon>
                        {{ getDistrictName(selectedDistrict) }}
                      </v-chip>
                      <v-chip small>
                        <v-icon small left>mdi-map-marker-outline</v-icon>
                        {{ getMicroDistrictName(product.location.microDistrictId) }}
                      </v-chip>
                    </div>


                  </div>


                  <div class="document-upload-section">
                    <h3 class="section-title">{{$t('add_item_verification_document')}}</h3>
                    <v-file-input
                      v-model="product.verificationDocument"
                      :label="$t('add_item_upload_document')"
                      prepend-icon="mdi-file-document"
                      outlined
                      clearable
                      accept=".pdf,.jpg,.jpeg,.png"
                      :rules="[
                        value => !value || value.size < 5000000 || 'Document size should be less than 5 MB',
                      ]"
                      class="form-input"
                    ></v-file-input>
                    <p class="text-caption text-grey">{{$t('add_item_example_document')}}</p>
                  </div>

                    <!-- Updated Dynamic Characteristics Section -->
                    <div v-if="characteristics.length > 0" class="characteristics-section">
                      <h3 class="section-title">{{$t('add_item_characteristics')}}</h3>
                      <div v-for="(char, index) in characteristics" :key="index" class="characteristic-field">
                        <div class="characteristic-header">
                          <div class="characteristic-label">{{ char.name }}</div>
                          <v-chip small class="characteristic-type">{{ char.type }}</v-chip>
                        </div>
                        <div class="characteristic-options">
                          <v-radio-group
                            v-model="product.characteristics[char.name]"
                            :mandatory="false"
                            class="radio-group"
                          >
                            <v-radio
                              v-for="(option, optIndex) in char.options"
                              :key="optIndex"
                              :label="option"
                              :value="option"
                              color="primary"
                              class="radio-option"
                            ></v-radio>
                          </v-radio-group>
                        </div>
                      </div>
                    </div>
                  </div>
                </v-col>


                <v-col cols="12" md="6">
                  <div class="image-upload-section">
                    <div class="image-upload-header">
                      <h3 class="section-title">{{$t('add_item_product_images')}}</h3>
                      <div class="upload-counter">
                        {{ product.images.length }}/12 {{ $t('add_item_images') }}
                      </div>
                    </div>

                    <div class="images-grid">

                      <div
                        class="upload-thumbnail"
                        @click="$refs.fileInput.click()"
                      >
                        <v-icon large>mdi-plus</v-icon>
                        <p>{{$t('add_item_add_image')}}</p>
                      </div>


                      <div
                        v-for="(img, index) in product.images"
                        :key="index"
                        class="image-thumbnail"
                      >
                        <v-img
                          :src="img"
                          aspect-ratio="1"
                          class="thumbnail-image"
                        ></v-img>
                        <v-btn
                          icon
                          small
                          class="remove-image-btn"
                          @click.stop="removeImage(index)"
                        >
                          <v-icon small>mdi-close</v-icon>
                        </v-btn>
                      </div>
                    </div>


                    <input
                      ref="fileInput"
                      type="file"
                      accept="image/*"
                      style="display: none"
                      multiple
                      @change="handleImageUpload"
                    >
                  </div>
                </v-col>
              </v-row>


              <div class="form-actions">
                <v-btn
                  color="primary"
                  large
                  @click="submitProduct"
                  :loading="isSubmitting"
                  :disabled="!isFormValid"
                >
                  <v-icon left>mdi-content-save</v-icon>
                  {{$t('add_item_save_product')}}
                </v-btn>
                <v-btn
                  text
                  large
                  @click="resetForm"
                  class="ml-2"
                >
                  {{$t('add_item_cancel')}}
                </v-btn>
              </div>
            </v-card>
          </div>
        </v-expand-transition>



      </v-container>

    </div>

  </div>

      <v-snackbar v-model="showSuccess" color="success" location="top right" :timeout="3000">
        <div class="d-flex align-center">
          <v-icon class="mr-3">mdi-check-circle</v-icon>
          <div class="text-subtitle-1">{{ successMessage }}</div>
        </div>
      </v-snackbar>

      <v-snackbar v-model="showValidationError" color="error" timeout="3000">
        <v-icon left>mdi-alert-circle</v-icon>
        Please fill all required fields
      </v-snackbar>
</template>

<script>
import Header from '@/components/Header.vue'
import { uploadImageToS3 } from '@/utils/s3-uploader';
import { ref, computed, onMounted, watch } from 'vue';
import axios from "@/axios.js";


export default {
  setup() {
    const navigationStack = ref([]);
    const selectedLeafCategory = ref(null);
    const isSubmitting = ref(false);
    const isLoading = ref(false);
    const characteristics = ref([]);
    const districts = ref([]);
    const microDistricts = ref([]);
    const selectedDistrict = ref(null);

    const showSuccess = ref(false)
    const successMessage = ref('')


    const product = ref({
      name: '',
      price: '',
      description: '',
      images: [],
      characteristics: {},
      fullAddress: '',
      verificationDocument: null,
      location: {
        districtId: null,
        microDistrictId: null
      }
    });


    const isFormValid = computed(() => {
    if (!product.value.name ||
        !product.value.price ||
        !product.value.description ||
        !selectedDistrict.value ||
        !product.value.location.microDistrictId ||
        !product.value.fullAddress) {
      return false;
    }

    if (product.value.images.length === 0) {
      return false;
    }

    for (const char of characteristics.value) {
      if (!product.value.characteristics[char.name]) {
        return false;
      }
    }

    return true;
  });


  const showValidationError = ref(false);


    const getDistrictName = (districtId) => {
      const district = districts.value.find(d => d.id === districtId);
      return district ? district.name : '';
    };

    const getMicroDistrictName = (microDistrictId) => {
      const microDistrict = microDistricts.value.find(m => m.id === microDistrictId);
      return microDistrict ? microDistrict.name : '';
    };

    const currentLevelItems = computed(() => {
      if (selectedLeafCategory.value) {
        return [];
      }
      if (navigationStack.value.length === 0) return [];
      return navigationStack.value[navigationStack.value.length - 1].subcategories;
    });



    async function fetchDistricts() {
      try {
        isLoading.value = true;
        const response = await axios.get('/open-api/microDistinct');
        const data = await response.data;
        districts.value = data.body;
      } catch (error) {
        console.error('Error fetching districts:', error);
      } finally {
        isLoading.value = false;
      }
    }

    function fetchMicroDistricts() {
  console.log('Selected district ID:', selectedDistrict.value);

  if (!selectedDistrict.value) {
    microDistricts.value = [];
    product.value.location.districtId = null;
    product.value.location.microDistrictId = null;
    return;
  }

  const district = districts.value.find(d => d.id === selectedDistrict.value);
  console.log('Found district:', district);

  if (district && district.list?.length > 0) {
    microDistricts.value = district.list;
    product.value.location.districtId = district.id;
    product.value.location.microDistrictId = null;
  } else {
    microDistricts.value = [];
    console.warn('No micro-districts found for selected district.');
  }
}



const formattedDistricts = computed(() => {
  return districts.value.map(district => ({
    text: district.name,
    value: district.id
  }));
});


const formattedMicroDistricts = computed(() => {
  return microDistricts.value.map(micro => ({
    text: micro.name,
    value: micro.id
  }));
});



    async function fetchCategories() {
      try {
        isLoading.value = true;
        const response = await axios.get('/open-api/categories');
        const data = await response.data;
        navigationStack.value = [data.body];
      } catch (error) {
        console.error('Error fetching categories:', error);
      } finally {
        isLoading.value = false;
      }
    }


    async function fetchFilters(categoryId) {
      try {
        isLoading.value = true;
        const response = await axios.get(`/open-api/filters/category/${categoryId}`);
        const data = await response.data;


        characteristics.value = data.body.map(filter => ({
          ...filter,
          type: 'single-select', // Force single select
          options: JSON.parse(filter.options).values
        }));


        product.value.characteristics = {};
        characteristics.value.forEach(char => {
          product.value.characteristics[char.name] = null;
        });
      } catch (error) {
        console.error('Error fetching filters:', error);
      } finally {
        isLoading.value = false;
      }
    }

    async function selectCategory(item) {
      if (item.subcategories.length > 0) {
        navigationStack.value.push(item);
        selectedLeafCategory.value = null;
      } else {
        navigationStack.value.push(item);
        selectedLeafCategory.value = item;
        await fetchFilters(item.id);
      }
    }

    function navigateTo(index) {
      if (index < navigationStack.value.length - 1) {
        navigationStack.value = navigationStack.value.slice(0, index + 1);
        selectedLeafCategory.value = null;
      }
    }

    function getCategoryIcon(name) {
      const icons = {
        'All': 'mdi-view-grid',
        'Transport': 'mdi-car',
        'Cars': 'mdi-car-sports',
        'Sedans': 'mdi-car-side',
        'SUVs & Crossovers': 'mdi-car-estate',
        'Bicycles & Scooters': 'mdi-bike',
        'Bicycles': 'mdi-bicycle',
        'Electric Scooters': 'mdi-scooter-electric',
        'Motorcycles': 'mdi-motorbike',
        'Moving & Delivery Vehicles': 'mdi-truck-delivery',
        'Cargo Vans': 'mdi-van-utility',
        'Pickup Trucks': 'mdi-truck',
        'Electronics & Gadgets': 'mdi-devices',
        'Computers & Accessories': 'mdi-laptop',
        'Laptops': 'mdi-laptop-mac',
        'Tablets': 'mdi-tablet',
        'Gaming Consoles': 'mdi-gamepad-variant',
        'Projectors': 'mdi-projector'
      };
      return icons[name] || 'mdi-shape-outline';
    }


    async function handleImageUpload(event) {
      const files = event.target.files;
      if (!files) return;

      const remainingSlots = 12 - product.value.images.length;
      const filesToAdd = Array.from(files).slice(0, remainingSlots);


      const loadingImages = filesToAdd.map(() => null);
      product.value.images = [...product.value.images, ...loadingImages];

      try {
        const uploadPromises = filesToAdd.map(file => uploadImageToS3(file));
        const uploadedUrls = await Promise.all(uploadPromises);

        const existingImages = product.value.images.filter(img => img !== null);
        product.value.images = [...existingImages, ...uploadedUrls];
      } catch (error) {
        console.error('Error uploading images:', error);
        product.value.images = product.value.images.filter(img => img !== null);
      }
    }

    function removeImage(index) {
      product.value.images.splice(index, 1);
    }

    // function toggleCharacteristic(charName, option) {
    //   if (!product.value.characteristics[charName]) {
    //     product.value.characteristics[charName] = [];
    //   }

    //   const index = product.value.characteristics[charName].indexOf(option);
    //   if (index === -1) {
    //     product.value.characteristics[charName].push(option);
    //   } else {
    //     product.value.characteristics[charName].splice(index, 1);
    //   }
    // }

    function resetForm() {
      product.value = {
        name: '',
        price: '',
        description: '',
        images: [],
        characteristics: {},
        fullAddress: '',
        verificationDocument: null,
        location: {
          districtId: null,
          microDistrictId: null
        }
      };
      selectedDistrict.value = null;
      selectedLeafCategory.value = null;
    }

    async function submitProduct() {
      if (!selectedLeafCategory.value) {
        console.error('No category selected');
        return;
      }

      if (!isFormValid.value) {
        showValidationError.value = true;
        return;
      }

      isSubmitting.value = true;

      try {
        if (!product.value.name || !product.value.price) {
          throw new Error('Please fill all required fields');
        }

        if (!product.value.location?.microDistrictId) {
          throw new Error('Please select a micro-district');
        }

        const districtName = getDistrictName(selectedDistrict.value);
        const microDistrictName = getMicroDistrictName(product.value.location.microDistrictId);

        if (!districtName || !microDistrictName) {
          throw new Error('Invalid location selection');
        }

        let verificationDocUrl = '';
        if (product.value.verificationDocument) {
          verificationDocUrl = await uploadImageToS3(product.value.verificationDocument);
        }

        const productData = {
          categoryId: selectedLeafCategory.value.id,
          name: product.value.name,
          description: product.value.description,
          pricePerDay: Number(product.value.price),
          available: true,
          days: 0,
          microDistinctId: product.value.location.microDistrictId,
          attributes: JSON.stringify(product.value.characteristics),
          fullAddress: `${product.value.fullAddress || ''}, ${microDistrictName}, ${districtName}`.replace(/^,\s*/, ''),
          verificationDocument: verificationDocUrl,
          urls: product.value.images
        };

        const token = localStorage.getItem('authToken');

        if (!token) {
          throw new Error('User is not authenticated');
        }

        const response = await axios.post('/api/items/create', productData, {

          headers: {
            // 'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
        });



        successMessage.value = 'Product sended successfully'
        showSuccess.value = true
        const result = await response.data;
        console.log('Product created successfully:', result);
        resetForm();

      } catch (error) {
        console.error('Error submitting product:', error);
        alert(error.response?.data?.message || error.message);
        alert(error.message);
      } finally {
        isSubmitting.value = false;
      }
    }






    onMounted(() => {
      fetchCategories();
      fetchDistricts();
    });

    watch(selectedDistrict, (newVal) => {
      fetchMicroDistricts();
    });


    return {
      navigationStack,
      selectedLeafCategory,
      currentLevelItems,
      product,
      characteristics,
      isSubmitting,
      isLoading,
      districts,
      microDistricts,
      selectedDistrict,
      showSuccess,
      successMessage,
      isFormValid,
      showValidationError,

      // Methods
      selectCategory,
      navigateTo,
      getCategoryIcon,
      handleImageUpload,
      removeImage,
      resetForm,
      submitProduct,
      fetchMicroDistricts,
      formattedDistricts,
      formattedMicroDistricts,
      getDistrictName,
      getMicroDistrictName

    };
  }
};

window.global = window;

</script>

<style scoped>


.v-messages__message {
  color: #ff5252;
  font-size: 0.75rem;
  margin-top: 4px;
}


.v-input--error .v-input__slot {
  border-color: #ff5252 !important;
}


.v-text-field--error .v-label {
  color: #ff5252 !important;
}

.hero-section {

  padding: 25px 0;
  text-align: center;
}

.hero-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 16px;
}

.hero-subtitle {
  font-size: 1.25rem;
  opacity: 0.9;
  margin-bottom: 30px;
}


.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 0;
}


.navigation-path {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 30px;
}

.path-chip {
  cursor: pointer;
  transition: all 0.3s ease;
  border-radius: 20px;
}

.path-chip:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* Categories Grid */
.categories-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.category-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  cursor: pointer;
  height: 120px;
  border-left: 4px solid transparent;
}

.category-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.category-card.leaf-category {
  border-left-color: #4CAF50;
}

.card-content {
  display: flex;
  align-items: center;
  padding: 20px;
  height: 100%;
}

.category-icon {
  margin-right: 20px;
  color: #3f51b5;
}

.category-info {
  flex: 1;
}

.category-name {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 4px;
  color: #2d3748;
}

.subcategory-count {
  font-size: 0.85rem;
  color: #718096;
  margin: 0;
}

.leaf-indicator {
  font-size: 0.85rem;
  color: #4CAF50;
  font-weight: 500;
  margin: 0;
}


.product-form-container {
  margin-top: 40px;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.form-header h2 {
  font-size: 1.5rem;
  font-weight: 600;
  color: #2d3748;
}

.form-card {
  border-radius: 16px !important;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08) !important;
  padding: 30px;
}

.form-section {
  padding-right: 20px;
}

.form-input {
  margin-bottom: 20px;
}

.section-title {
  font-size: 1rem;
  font-weight: 600;
  color: #4a5568;
  margin-bottom: 15px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* Characteristics Section */
.characteristics-section {
  margin-top: 30px;
}

.characteristic-field {
  margin-bottom: 25px;
}

.characteristic-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.characteristic-label {
  font-weight: 600;
  color: #2d3748;
  margin-right: 10px;
}

.characteristic-type {
  background-color: #e3f2fd !important;
  color: #1976d2 !important;
  font-size: 0.7rem;
  height: 22px;
}

.characteristic-options {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}


.radio-group {
  margin-top: 0;
  padding-top: 0;
}

.radio-option {
  margin-bottom: 8px;
}

.radio-option:deep(.v-input--selection-controls__input) {
  margin-right: 12px;
}

.radio-option:deep(.v-label) {
  font-size: 0.9rem;
  color: #4a5568;
}

.option-item {
  padding: 8px 16px;
  border-radius: 20px;
  background-color: #f5f5f5;
  color: #424242;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  font-size: 0.9rem;
}

.option-item:hover {
  background-color: #e0e0e0;
}

.option-item.selected {
  background-color: #bbdefb;
  color: #0d47a1;
  font-weight: 500;
}

.option-icon {
  margin-right: 6px;
  font-size: 1rem;
}

/* Image Upload Section */
.image-upload-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  height: 100%;
}

.image-upload-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.upload-counter {
  font-size: 0.9rem;
  color: #718096;
}

.images-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.image-thumbnail, .upload-thumbnail {
  position: relative;
  aspect-ratio: 1;
  border-radius: 8px;
  overflow: hidden;
  background-color: #f8fafc;
}

.thumbnail-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-image-btn {
  position: absolute;
  top: 5px;
  right: 5px;
  background-color: rgba(0, 0, 0, 0.5) !important;
  color: white !important;
}

.upload-thumbnail {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 2px dashed #cbd5e0;
  cursor: pointer;
  transition: all 0.3s ease;
}

.upload-thumbnail:hover {
  border-color: #3f51b5;
  background-color: rgba(63, 81, 181, 0.05);
}

.upload-thumbnail p {
  margin-top: 8px;
  font-size: 0.8rem;
  color: #718096;
}

/* Form Actions */
.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #e2e8f0;
}

/* ... existing styles ... */

/* Location Selector Styles */
.location-selector {
  margin-bottom: 30px;
  background: #f8fafc;
  padding: 20px;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

/* Updated Characteristic Styles */
.characteristic-field {
  margin-bottom: 25px;
  background: #f8fafc;
  padding: 20px;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.characteristic-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.characteristic-label {
  font-weight: 600;
  color: #2d3748;
  margin-right: 10px;
  font-size: 0.95rem;
}

.characteristic-type {
  background-color: #e3f2fd !important;
  color: #1976d2 !important;
  font-size: 0.7rem;
  height: 22px;
}

.radio-group {
  margin-top: 0;
  padding-top: 0;
}

.radio-option {
  margin-bottom: 8px;
}

.radio-option:deep(.v-input--selection-controls__input) {
  margin-right: 12px;
}

.radio-option:deep(.v-label) {
  font-size: 0.9rem;
  color: #4a5568;
}

/* Responsive Adjustments */
@media (max-width: 960px) {
  .characteristic-field {
    padding: 15px;
  }
}


@media (max-width: 960px) {
  .hero-title {
    font-size: 2rem;
  }

  .categories-grid {
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  }

  .images-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 600px) {
  .hero-section {
    padding: 60px 0;
  }

  .categories-grid {
    grid-template-columns: 1fr;
  }

  .form-card {
    padding: 20px;
  }

  .form-section {
    padding-right: 0;
  }

  .images-grid {
    grid-template-columns: 1fr;
  }
}

.document-upload-section {
  margin-top: 30px;
  background: #f8fafc;
  padding: 20px;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.document-upload-section .v-file-input {
  margin-top: 10px;
}

.document-upload-section .text-caption {
  margin-top: 8px;
}
</style>
