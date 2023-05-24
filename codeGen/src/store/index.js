import Vuex from 'vuex';
import { login, register } from '@/service/auth';
import { getUsers, createUser, updateUser, deleteUser } from '@/service/users';
import { getAccounts, createAccount } from '@/service/accounts';
import { getTransactions, getTransaction } from '@/service/transactions';

// Vue.use(Vuex);

const store = new Vuex.Store({
    state: {
        token: localStorage.getItem('jwt_token') || '', // Initial token value retrieved from local storage
        user: null,
        users: [],
        accounts: [],
        transactions: [],
    },
    mutations: {
        setToken(state, token) {
            state.token = token;
            localStorage.setItem('jwt_token', token); // Store token in local storage
        },
        setUser(state, user) {
            state.user = user;
        },
        setUsers(state, users) {
            state.users = users;
        },
        setAccounts(state, accounts) {
            state.accounts = accounts;
        },
        setTransactions(state, transactions) {
            state.transactions = transactions;
        },
        clearAuthData(state) {
            state.token = null;
            state.user = null;
            localStorage.removeItem('token'); // Remove token from local storage
        },
    },
    getters: {
        getCurrentUserToken(state) {
            return state.token;
        },
    },
    actions: {
        async login({ commit }, credentials) {
            try {
                const response = await login(credentials);
                commit('setToken', response);
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async register({ commit }, userData) {
            try {
                const response = await register(userData);
                commit('setToken', response.token);
                commit('setUser', response.user);
            } catch (error) {
                throw new Error(error.message);
            }
        },
        logout({ commit }) {
            commit('clearAuthData');
        },
        async getUsers({ commit, state }) {
            try {
                const users = await getUsers(state.token);
                commit('setUsers', users);
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async createUser({ commit, state }, userData) {
            try {
                const user = await createUser(userData, state.token);
                commit('setUsers', [...state.users, user]);
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async updateUser({ commit, state }, { userId, userData }) {
            try {
                const user = await updateUser(userId, userData, state.token);
                const updatedUsers = state.users.map((u) => (u.id === userId ? user : u));
                commit('setUsers', updatedUsers);
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async deleteUser({ commit, state }, userId) {
            try {
                await deleteUser(userId, state.token);
                const updatedUsers = state.users.filter((u) => u.id !== userId);
                commit('setUsers', updatedUsers);
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async getAccounts({ commit, state }) {
            try {
                const accounts = await getAccounts(state.token);
                commit('setAccounts', accounts);
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async createAccount({ commit, state }, accountData) {
            try {
                const account = await createAccount(accountData, state.token);
                commit('setAccounts', [...state.accounts, account]);
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async getTransactions({ commit, state }) {
            try {
                const transactions = await getTransactions(state.token);
                commit('setTransactions', transactions);
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async getTransaction({ commit, state }, transactionId) {
            try {
                const transaction = await getTransaction(transactionId, state.token);
                return transaction;
            } catch (error) {
                throw new Error(error.message);
            }
        },
    },

});

export default store;
