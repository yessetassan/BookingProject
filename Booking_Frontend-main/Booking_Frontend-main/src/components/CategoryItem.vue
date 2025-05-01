<template>
  <div class="category">
    <div class="category-header"  @mouseover="hover = true" @mouseleave="hover = false">
      <div class="div-icon" @mouseover="hover = true" @mouseleave="hover = false">
        <v-icon
          :class="{ 'hovered-icon': hover }"
          @click="toggle"
          v-if="hasSubcategories"
          size="20"
        >
          {{ isOpen ? 'mdi-menu-down' : 'mdi-menu-right' }}
        </v-icon>
      </div>
      <span class="text-primary cursor-pointer" style="font-size: 14px;" @click="selectCategory(category.id)">{{ category.name }}</span>
    </div>

    <v-expand-transition>
      <div v-if="isOpen" class="subcategory">
        <CategoryItem
          v-for="sub in category.subcategories"
          :key="sub.id"
          :category="sub"
          @select-category="selectCategory"
        />
      </div>
    </v-expand-transition>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
const props = defineProps({
  category: {
    type: Object,
    required: true,
  },
});

console.log('CategoryItem Props:', props.category);

const emit = defineEmits(['select-category']);

const isOpen = ref(false);
const hover = ref(false);

const hasSubcategories = computed(() => props.category?.subcategories?.length > 0);

const toggle = () => {
  if (hasSubcategories.value) isOpen.value = !isOpen.value;
};

const selectCategory = (categoryId) => {
  emit('select-category', categoryId);
};
</script>

<style scoped>
.category {
  margin-left: 10px;
  margin-top: 7px;
}
.category-header {
  display: flex;
  align-items: center;
  gap: 5px;
}
.subcategory {
  margin-left: 10px;
  margin-top: 5px;
}

.div-icon {
  border-radius: 15px;
  display: flex;
  text-align: center;
  align-items: center;
  justify-content: center;
  background: white;
  border-width: 1px;
  border-style: double;
  border-color: gray;
}

.v-icon {
  color: gray;
}

.hovered-icon {
  color: black !important;
}
</style>
