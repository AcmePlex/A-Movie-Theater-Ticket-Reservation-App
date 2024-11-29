import axios from 'axios';

// Set up an Axios instance that automatically includes the JWT token in headers
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080', // Adjust the base URL
    headers: { 'Content-Type': 'application/json' }
});

axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = 'Bearer ' + token;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default axiosInstance;