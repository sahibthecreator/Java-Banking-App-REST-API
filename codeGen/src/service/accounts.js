import axios from '@/axios-auth.js';

export const getAccounts = async (token) => {
  try {
    const response = await axios.get(`/accounts`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};

export const getAccountsByUserId = async (userId, token) => {
  try {
    const response = await axios.get(`/accounts?userId=${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};

export const createAccount = async (accountData, token) => {
  try {
    const response = await axios.post(`/accounts`, accountData, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};

export const requestAccount = async (requestData, token) => {
  try {
    const response = await axios.post(`/accounts/requests`, requestData, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};
