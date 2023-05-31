import axios from '@/axios-auth.js';

export const login = async (credentials) => {
  try {
    const response = await axios.post(`/auth/login`, credentials);
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};

export const register = async (userData) => {
  try {
    const response = await axios.post(`/auth/register`, userData);
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};
