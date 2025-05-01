/**
 * plugins/index.js
 *
 * Automatically included in `./src/main.js`
 */

// Plugins
import { createI18n } from 'vue-i18n'

import vuetify from './vuetify'
import pinia from '@/stores'
import router from '@/router'




import ru from '@/locales/ru.json'
import kz from '@/locales/kz.json'
import en from '@/locales/en.json'


const messages = {
  ru,
  kz,
  en,
}

const i18n = createI18n({
  legacy: false,
  locale: 'ru',
  fallbackLocale: 'ru',
  messages,
})

export function registerPlugins (app) {
  app
    .use(vuetify)
    .use(router)
    .use(pinia)
    .use(i18n)
}
