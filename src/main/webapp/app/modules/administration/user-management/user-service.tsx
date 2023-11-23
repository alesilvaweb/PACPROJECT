import axios from 'axios';

const API_URL = '/api'; // Substitua pela URL do seu backend

const userService = {
  searchUsers: async (query, page = 1, size = 10) => {
    try {
      // /api/admin/users/search?query=
      const response = await axios.get(`/api/admin/users/search`, {
        params: {
          query,
          page,
          size,
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
