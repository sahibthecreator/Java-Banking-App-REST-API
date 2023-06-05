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

export const getIbanByName = async ({ firstName, lastName }, token) => {
  try {
    const response = await axios.get(`/accounts/iban?firstname=${firstName}&lastname=${lastName}`, {
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
    console.log(accountData);
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

export const getAllRequests = async (token) => {
  try {
    const response = await axios.get(`/accounts/requests`, {
      headers: {
        Authorization: `Bearer ${token}`,
      }
    });
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};

export const approveRequest = async (requestId, token) => {
  try {
    await axios.put(`/accounts/requests/${requestId}/approve`, null, {
      headers: {
        Authorization: `Bearer ${token}`,
      }
    });
  } catch (error) {
    throw error.response.data;
  }
};
export const denyRequest = async (requestId, token) => {
  try {
    await axios.put(`/accounts/requests/${requestId}/deny`, null, {
      headers: {
        Authorization: `Bearer ${token}`,
      }
    });
  } catch (error) {
    throw error.response.data;
  }
};