import axios from 'axios';

const API_URL = 'http://localhost:80'; // Your backend API URL

export const getTransactions = async (token) => {
  try {
    const response = await axios.get(`${API_URL}/transactions`, {
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
    const response = await axios.post(`${API_URL}/transactions`, transactionData, {
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
    const response = await axios.get(`${API_URL}/transactions/userId/${userId}`, {
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
    const response = await axios.get(`${API_URL}/transactions/${transactionId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};
