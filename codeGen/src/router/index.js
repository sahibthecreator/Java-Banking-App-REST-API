import { createRouter, createWebHistory } from 'vue-router'

import Login from '@/components/Login.vue';
import Home from '@/components/Home.vue';


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', component: Home},
    { path: '/login', component: Login}
  ]
})

export default router