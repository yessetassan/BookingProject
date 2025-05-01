<template>
  <div>
    <!-- Current Category -->
    <v-list-item class="px-0 pl-1" @click="toggleCategory(category) || $emit('set-active-category', category)">
      <template v-slot:prepend>
        <v-icon>{{ category.open ? 'mdi-chevron-down' : 'mdi-chevron-right' }}</v-icon>
      </template>
      <v-list-item-title class="text-primary">
        {{ category.name }}
      </v-list-item-title>
    </v-list-item>

    <!-- Subcategories -->
    <div v-if="category.open">
      <recursive-category
        v-for="subcategory in category.subcategories"
        :key="subcategory.id"
        :category="subcategory"
        @set-active-category="$emit('set-active-category', $event)"
        class="pl-8"
      />
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

const props = defineProps({
  category: {
    type: Object,
    required: true,
  },
});

const toggleCategory = (category) => {
  category.open = !category.open;
};
</script>
