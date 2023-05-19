import './assets/main.css'

import { createApp } from 'vue'
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue';
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

import '@/assets/custom_vars.scss';


const app = createApp(App);

app.use(BootstrapVue);
app.use(createPinia());
app.use(router)
app.mount('#app')
