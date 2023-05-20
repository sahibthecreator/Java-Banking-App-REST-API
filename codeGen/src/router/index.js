import { createRouter, createWebHistory } from 'vue-router'

import Login from '@/components/Login.vue';
import Home from '@/components/Home.vue';
import Dashboard from '@/components/Dashboard.vue';
import NotFound from '@/components/NotFound.vue';


const router = createRouter({
  mode: 'history',
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'Home', component: Home, },
    { path: '/login', name: 'Log in', component: Login },
    { path: '/dashboard', name: 'my dashboard', component: Dashboard },
    { path: '/:pathMatch(.*)*', name: '404 Not Found', component: NotFound },
  ],
});

router.beforeEach((to, from, next) => {
  if (!to.name) {
    document.title = DEFAULT_TITLE;
  }
  document.title = to.name;
  next();
});

export default router