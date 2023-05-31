import axios from '@/axios-auth.js';


export const getTransactions = async (token) => {
  try {
    const response = await axios.get(`/transactions`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};

export const performTransaction = async (transactionData, token) => {
  try {
    const response = await axios.post(`/transactions`, transactionData, {
      headers: {
        Authorization: `Bearer ${token}`,
      }
    });
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};

export const getTransactionsByUserId = async (userId, token) => {
  try {
    const response = await axios.get(`/transactions/userId/${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};

export const getTransaction = async (transactionId, token) => {
  try {
    const response = await axios.get(`/transactions/${transactionId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};
