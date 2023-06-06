import Vuex from 'vuex';
import { login, register } from '@/service/auth';
import { getUser, getUsers, createUser, updateUser, deleteUser } from '@/service/users';
import { getAccounts, createAccount } from '@/service/accounts';
import { getTransactions, getTransaction } from '@/service/transactions';
import { activateAccount, approveRequest, deactivateAccount, denyRequest, getAccountsByUserId, getAllRequests, getIbanByName, requestAccount, updateAccount } from '../service/accounts';
import { getTransactionsByUserId, performTransaction } from '../service/transactions';
import { getRemainingDayLimit, getUsersByName } from '../service/users';

// Vue.use(Vuex);

const store = new Vuex.Store({
    state: {
        token: localStorage.getItem('jwt_token') || null, // Initial token value retrieved from local storage
        user: null,
        userId: localStorage.getItem('user_id') || '',
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
        setUserId(state, userId) {
            state.userId = userId;
            localStorage.setItem('user_id', userId); // Store token in local storage

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
            localStorage.removeItem('jwt_token'); // Remove token from local storage
        },
    },
    getters: {
        getCurrentUserToken(state) {
            return state.token;
        },
        getUserId(state) {
            return state.userId;
        },
    },
    actions: {
        async login({ commit }, credentials) {
            try {
                const response = await login(credentials);
                commit('setToken', response.jwtToken);
                commit('setUserId', response.userId);
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async register({ commit }, userData) {
            try {
                const response = await register(userData);
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
                return users;
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async getUser({ commit, state }, userId) {
            try {
                const user = await getUser(userId, state.token);
                commit('setUser', user);
                return user;
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async getUsersByName({ commit, state }, request) {
            try {
                const users = await getUsersByName(request, state.token);
                //commit('setUsers', users);
                return users;
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async getRemainingDayLimit({ commit, state }, userId) {
            try {
                const limit = await getRemainingDayLimit(userId, state.token);
                return limit;
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
                //const updatedUsers = state.users.map((u) => (u.id === userId ? user : u));
                //commit('setUsers', updatedUsers);
                return user;
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async deleteUser({ commit, state }, userId) {
            try {
                await deleteUser(userId, state.token);
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async getAccountsByUserId({ commit, state }, userId) {
            try {
                const accounts = await getAccountsByUserId(userId, state.token);
                commit('setAccounts', accounts);
                return accounts;
            } catch (error) {
                console.log(error);
                throw new Error(error.message);
            }
        },
        async getAccounts({ commit, state }) {
            try {
                const accounts = await getAccounts(state.token);
                commit('setAccounts', accounts);
                return accounts;
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async getIbanByName({ commit, state }, { firstName, lastName }) {
            try {
                const ibans = await getIbanByName({ firstName, lastName }, state.token);
                return ibans;
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async createAccount({ commit, state }, accountData) {
            try {
                await createAccount(accountData, state.token);
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async updateAccount({ commit, state }, { iban, accountData }) {
            try {
                await updateAccount({ iban, accountData }, state.token);
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async deactivateAccount({ commit, state }, iban) {
            try {
                const res = await deactivateAccount(iban, state.token);
                return res;
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async activateAccount({ commit, state }, iban) {
            try {
                const res = await activateAccount(iban, state.token);
                return res;
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async performTransaction({ commit, state }, transactionData) {
            try {
                const transaction = await performTransaction(transactionData, state.token);
                return transaction;
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
        async getTransactionsByUserId({ commit, state }, userId) {
            try {
                const transactions = await getTransactionsByUserId(userId, state.token);
                commit('setTransactions', transactions);
                return transactions;
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

        async requestAccount({ commit, state }, requestData) {
            try {
                const request = await requestAccount(requestData, state.token);
                return request;
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async getAllRequests({ commit, state }) {
            try {
                const requests = await getAllRequests(state.token);
                return requests;
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async approveRequest({ commit, state }, requestId) {
            try {
                await approveRequest(requestId, state.token);
            } catch (error) {
                throw new Error(error.message);
            }
        },
        async denyRequest({ commit, state }, requestId) {
            try {
                await denyRequest(requestId, state.token);
            } catch (error) {
                throw new Error(error.message);
            }
        },
    },

});

export default store;
