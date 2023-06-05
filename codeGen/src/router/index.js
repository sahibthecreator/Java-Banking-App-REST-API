import { createRouter, createWebHistory } from 'vue-router'

import Login from '@/components/Login.vue';
import Home from '@/components/Home.vue';
import DashboardV2 from '@/components/Dashboard/Dashboard.vue';
import Transaction from '@/components/Transaction/Transaction.vue';
import EmployeeTransaction from '@/components/Transaction/EmployeeTransaction.vue';
import RequestAccount from '@/components/RequestAccount/RequestAccount.vue';
import EmployeePanel from '@/components/EmployeePanel/EmployeePanel.vue'
import CreateBankAccount from '@/components/CreateAccount/CreateAccount.vue';
import Profile from '@/components/Profile/Profile.vue';
import NotFound from '@/components/NotFound.vue';
import store from '../store'; // Import the Vuex store



const router = createRouter({
  mode: 'history',
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'Home', component: Home, },
    { path: '/login', name: 'Log in', component: Login },
    { path: '/dashboard', name: 'my dashboard', component: DashboardV2, meta: { requiresAuth: true } },
    { path: '/:pathMatch(.*)*', name: '404 Not Found', component: NotFound },
    { path: '/dashboard/transaction', name: 'transfer', component: Transaction, meta: { requiresAuth: true } },
    { path: '/dashboard/requestAccount', name: 'request', component: RequestAccount, meta: { requiresAuth: true } },
    { path: '/dashboard/employeePanel', name: 'employeePanel', component: EmployeePanel, meta: { requiresAuth: true } },
    { path: '/dashboard/employeePanel/createAccount', name: 'createAccount', component: CreateBankAccount, meta: { requiresAuth: true } },
    { path: '/dashboard/profile', name: 'profile', component: Profile, meta: { requiresAuth: true } },
    { path: '/dashboard/employeePanel/transaction', name: 'employeeTransfer', component: EmployeeTransaction, meta: { requiresAuth: true} },
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