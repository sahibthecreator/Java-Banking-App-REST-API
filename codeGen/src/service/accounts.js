import axios from 'axios';

const API_URL = 'http://localhost:8080'; // Your backend API URL

export const getAccounts = async (token) => {
  try {
    const response = await axios.get(`${API_URL}/accounts`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.response.data.error);
  }
};

export const createAccount = async (accountData, token) => {
  try {
    const response = await axios.post(`${API_URL}/accounts`, accountData, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.response.data.error);
  }
};
