import axios from '@/axios-auth.js';

export const getUser = async (userId, token) => {
  try {
    const response = await axios.get(`/users/${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};

export const getUsers = async (token) => {
  try {
    const response = await axios.get(`/users`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};

export const getUsersByName = async ({firstName, lastName}, token) => {
  try {
    const response = await axios.get(`/users?firstName=${firstName}&lastName=${lastName}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};

export const getRemainingDayLimit = async (userId, token) => {
  try {
    const response = await axios.get(`/users/${userId}/remaining-day-limit`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};

export const createUser = async (userData, token) => {
  try {
    const response = await axios.post(`/users`, userData, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.response.data.error);
  }
};

export const updateUser = async (userId, userData, token) => {
  try {
    const response = await axios.put(`/users/${userId}`, userData, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.response.data.error);
  }
};

export const deleteUser = async (userId, token) => {
  try {
    const response = await axios.delete(`/users/${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.response.data.error);
  }
};
