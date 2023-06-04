import './assets/main.css'

import { createApp } from 'vue'
import { BootstrapVue, IconsPlugin, BootstrapVueIcons } from 'bootstrap-vue';
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import store from './store'; 



import '@/assets/custom_vars.scss';


const app = createApp(App);

app.use(BootstrapVue);
app.use(BootstrapVueIcons);
app.use(createPinia());
app.use(router)
app.use(store);
app.mount('#app')

