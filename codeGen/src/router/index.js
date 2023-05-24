import { createRouter, createWebHistory } from 'vue-router'

import Login from '@/components/Login.vue';
import Home from '@/components/Home.vue';
import Dashboard from '@/components/Dashboard.vue';
import NotFound from '@/components/NotFound.vue';
import store from '../store'; // Import the Vuex store



const router = createRouter({
  mode: 'history',
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'Home', component: Home, },
    { path: '/login', name: 'Log in', component: Login },
    { path: '/dashboard', name: 'my dashboard', component: Dashboard, meta: { requiresAuth: true } },
    { path: '/:pathMatch(.*)*', name: '404 Not Found', component: NotFound },
  ],
});

router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some((record) => record.meta.requiresAuth);
  const token = store.getters.getCurrentUserToken; // Access the store instance directly
  if (requiresAuth && !token) {
    next('/login');
  } else {
    next();
  }
});

export default router