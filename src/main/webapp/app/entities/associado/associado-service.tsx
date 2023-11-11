import axios from 'axios';

const API_URL = '/api'; // Substitua pela URL do seu backend

const userService = {
  searchUsers: async (query, page = 1, size = 10, sort) => {
    try {
      // associados?nome.contains=ale&page=0&size=20
      const response = await axios.get(`${API_URL}/associados?nome.contains=${query}`, {
        params: {
          page,
          size,
          sort,
        },
      });

      return response;
    } catch (error) {
      console.error('Error searching users:', error);
      throw error;
    }
  },
};

export default userService;
