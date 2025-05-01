<template>
  <div class="language-selector" ref="selector" v-cloak>
    <!-- Current Language Display - Mobile Compact Version -->
    <button
      v-if="isMobile"
      @click="toggleDropdown"
      class="language-selector__trigger language-selector__trigger--mobile"
      :class="{ 'language-selector__trigger--active': isDropdownOpen }"
    >
      <img
        :src="getFlag(selectLang)"
        alt="Selected flag"
        class="language-selector__flag"
      />
      <!-- <svg
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 24 24"
        stroke-width="2"
        stroke="currentColor"
        class="language-selector__chevron"
        :class="{ 'language-selector__chevron--active': isDropdownOpen }"
      >
        <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 8.25l-7.5 7.5-7.5-7.5" />
      </svg> -->
    </button>

    <!-- Current Language Display - Desktop Version -->
    <button
      v-else
      @click="toggleDropdown"
      class="language-selector__trigger"
      :class="{ 'language-selector__trigger--active': isDropdownOpen }"
    >
      <span class="language-selector__current">
        <img
          :src="getFlag(selectLang)"
          alt="Selected flag"
          class="language-selector__flag"
        />
        <span class="language-selector__text">
          {{ getLanguageTitle(selectLang) }}
        </span>
      </span>
      <svg
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 24 24"
        stroke-width="2"
        stroke="currentColor"
        class="language-selector__chevron"
        :class="{ 'language-selector__chevron--active': isDropdownOpen }"
      >
        <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 8.25l-7.5 7.5-7.5-7.5" />
      </svg>
    </button>

    <!-- Dropdown Menu -->
    <transition name="language-dropdown">
      <teleport to="body">
        <div
          v-if="isDropdownOpen"
          class="language-selector__dropdown-container"
          :class="{ 'language-selector__dropdown-container--mobile': isMobile }"
          :style="dropdownStyles"
        >
          <ul class="language-selector__dropdown">
            <li
              v-for="item in langItems"
              :key="item.lang"
              @click="selectLanguage(item.lang)"
              class="language-selector__option"
              :class="{ 'language-selector__option--active': selectLang === item.lang }"
            >
              <img :src="item.icon" alt="Flag icon" class="language-selector__flag" />
              <span class="language-selector__text">
                {{ item.title }}
              </span>
              <svg
                v-if="selectLang === item.lang"
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="2"
                stroke="currentColor"
                class="language-selector__checkmark"
              >
                <path stroke-linecap="round" stroke-linejoin="round" d="M4.5 12.75l6 6 9-13.5" />
              </svg>
            </li>
          </ul>
        </div>
      </teleport>
    </transition>
  </div>
</template>

<script>
import { ref, onMounted, onBeforeUnmount } from 'vue';

export default {
  setup() {
    const isMobile = ref(false);

    const checkMobile = () => {
      isMobile.value = window.innerWidth < 960;
    };

    onMounted(() => {
      checkMobile();
      window.addEventListener('resize', checkMobile);
    });

    onBeforeUnmount(() => {
      window.removeEventListener('resize', checkMobile);
    });

    return { isMobile };
  },

  data() {
    return {
      langItems: [
        { title: 'Қазақша', lang: 'kz', icon: 'https://flagcdn.com/w40/kz.png' },
        { title: 'English', lang: 'en', icon: 'https://flagcdn.com/w40/us.png' },
        { title: 'Русский', lang: 'ru', icon: 'https://flagcdn.com/w40/ru.png' },
      ],
      selectLang: typeof window !== 'undefined' ? localStorage.getItem('currentLanguage') || 'ru' : 'ru',
      isDropdownOpen: false,
      dropdownStyles: {
        position: 'absolute'
      }
    }
  },

  methods: {
    toggleDropdown() {
      this.isDropdownOpen = !this.isDropdownOpen;
      this.$nextTick(() => {
        this.updateDropdownPosition();
      });
    },

    selectLanguage(lang) {
      this.selectLang = lang;
      if (typeof window !== 'undefined') {
        localStorage.setItem('currentLanguage', lang);
      }
      this.$i18n.locale = lang;
      this.isDropdownOpen = false;
    },

    getFlag(lang) {
      const langItem = this.langItems.find(item => item.lang === lang);
      return langItem ? langItem.icon : '';
    },

    getLanguageTitle(lang) {
      const langItem = this.langItems.find(item => item.lang === lang);
      return langItem ? langItem.title : '';
    },

    handleClickOutside(event) {
      if (this.isDropdownOpen && !this.$refs.selector?.contains(event.target)) {
        this.isDropdownOpen = false;
      }
    },

    updateDropdownPosition() {
      const trigger = this.$refs.selector?.querySelector('.language-selector__trigger');
      if (trigger) {
        const rect = trigger.getBoundingClientRect();
        const viewportHeight = window.innerHeight;
        const dropdownHeight = this.langItems.length * 44;

        const spaceBelow = viewportHeight - rect.bottom;
        const openUpwards = spaceBelow < dropdownHeight && rect.top > dropdownHeight;

        this.dropdownStyles = {
          left: `${rect.left}px`,
          width: `${rect.width}px`,
          position: 'fixed',
          [openUpwards ? 'bottom' : 'top']: openUpwards
            ? `${viewportHeight - rect.top + 8}px`
            : `${rect.bottom + 8}px`,
          'max-height': 'calc(100vh - 20px)',
          'overflow-y': 'auto'
        };
      }
    }
  },

  mounted() {
    document.addEventListener('click', this.handleClickOutside);
    this.$i18n.locale = this.selectLang;
  },

  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside);
  }
}
</script>

<style scoped>
/* Base Styles */
.language-selector {
  position: relative;
  min-width: 120px;
  z-index: 20;
  margin-left: 8px;
}


.language-selector__trigger {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 8px 12px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(5px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 8px;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}


.language-selector__trigger--mobile {
  padding: 6px 8px;
  min-width: auto;
  border-radius: 50px;
}

.language-selector__trigger:hover {
  background: rgba(255, 255, 255, 0.15);
  border-color: rgba(255, 255, 255, 0.3);
}

.language-selector__trigger--active {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.4);
}

.language-selector__current {
  display: flex;
  align-items: center;
  gap: 8px;
}

.language-selector__flag {
  width: 20px;
  height: 15px;
  object-fit: cover;
  border-radius: 2px;
  flex-shrink: 0;
}

.language-selector__chevron {
  width: 16px;
  height: 16px;
  transition: transform 0.3s ease;
  flex-shrink: 0;
}

.language-selector__chevron--active {
  transform: rotate(180deg);
}

.language-selector__dropdown-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  z-index: 9999;
  transform-origin: top center;
}


.language-selector__dropdown-container--mobile {
  width: 160px !important;
  left: auto !important;
  right: 12px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.language-selector__dropdown {
  padding: 4px 0;
  margin: 0;
  list-style: none;
}


.language-dropdown-enter-active,
.language-dropdown-leave-active {
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

.language-dropdown-enter-from,
.language-dropdown-leave-to {
  opacity: 0;
  transform: translateY(-8px) scale(0.95);
}


.language-selector__option {
  display: flex;
  align-items: center;
  padding: 10px 16px;
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
  color: #333;
}

.language-selector__option:hover {
  background: rgba(0, 0, 0, 0.05);
}

.language-selector__option--active {
  background: rgba(var(--v-theme-primary), 0.08);
  color: rgb(var(--v-theme-primary));
}

.language-selector__text {
  flex-grow: 1;
  margin: 0 12px;
  font-size: 14px;
  white-space: nowrap;
}

.language-selector__checkmark {
  width: 16px;
  height: 16px;
  color: rgb(var(--v-theme-primary));
}

[v-cloak] {
  display: none;
}

@media (max-width: 600px) {
  .language-selector {
    min-width: auto;
    margin-left: 4px;
  }

  .language-selector__dropdown-container--mobile {
    width: 140px !important;
    right: 8px;
  }

  .language-selector__option {
    padding: 8px 12px;
  }

  .language-selector__text {
    font-size: 13px;
    margin: 0 8px;
  }
}
</style>
