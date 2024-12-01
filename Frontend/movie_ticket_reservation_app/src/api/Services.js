import axios from 'axios'
import axiosInstance from '../utils/axiosInstance'

const DEFAULT_PAGE_SIZE = 10;

const api = axios.create({
    baseURL: "http://localhost:8080", // Replace with your base API URL
    headers: {
      "Content-Type": 'application/json',
    },
  });

export const getMovies = async (page=1, pageSize=DEFAULT_PAGE_SIZE) => {
    const query = page == 1 ? "" : `?page=${page}&pageSize=${pageSize}`;
    const response = await api.get('/movies'+query);
    console.log(response.data);
    return response.data;
}

export const getGenres = async () => {
  const response = await api.get('/genres');
  return response.data;
}

export const getMoviesByGenre = async (genreId, page=1, pageSize=DEFAULT_PAGE_SIZE) => {
  const query = page == 1 ? "" : `?page=${page}&pageSize=${pageSize}`;
  const response = await api.get(`/movies/genre/${genreId}`+query);
  return response.data;
}

export const getMoviesBySearch = async (search, page=1, pageSize=DEFAULT_PAGE_SIZE) => {
  const query = page == 1 ? `?q=${search}` : `?q=${search}&page=${page}&pageSize=${pageSize}`;
  const response = await api.get(`/movies/search`+query);
  return response.data;
}

export const getMovieDetail = async (movieId) => {
  const response = await api.get(`/movie/${movieId}`);
  return  response.data;
}

export const getTheatres = async (movieId) => {
  const response = await api.get(`/theatres/movie/${movieId}`)
  return response.data;
}

export const getShowtimes = async (movieId, theatreId) => {
  const response = await axiosInstance.get(`/showtimes/movie/${movieId}/theatre/${theatreId}`)
  return response.data;
}

export const getSeats = async (showroomId, showtimeId) => {
  const response = await api.get(`/seats/showroom/${showroomId}/showtime/${showtimeId}`)
  console.log(response.data);
  return response.data;
}

export const bookTickets = async (data) => {
  const response = await api.post(`/book`, data);
  return response.data;
}

export const getCreditCards = async () => {
  const response = await api.get(`/creditcards`);
  return response.data;
}

export const getBookings = async () => {
  const response = await api.get(`/bookings`);
  return response.data;
}

export const cancelTicket = async (ticketNumber) => {
  const response = await api.put(`/cancel/${ticketNumber}`);
  console.log(response.data);
  return response.data;
}

export const issueRefund = async (ticketNumber) => {
  const response = await api.put(`/issuerefund/${ticketNumber}`);
  console.log(response.data);
  return response.data;
}

export const getTotalPrice = async (data) => {
  const response = await api.post(`/totalprice`, data);
  console.log(response.data);
  return response.data;
}

export const ticketPayment = async (data) => {
  const response = await api.post(`/ticketpayment`, data);
  return response.data;
}

export const createRegisteredUser = async (data) => {
  const response = await api.post(`/api/registered-users/createregistereduser`, data);
  console.log(response.data);
  return response.data;
}

export const subscriptionRenew = async (data) => {
  const response = await api.post(`/api/registered-users/createregistereduser`, data);
  console.log(response.data);
  return response.data;
}

export const membershipPayment = async (email, method) => {
  const response = await api.put(`/membershippayment/${email}/method/${method}`);
  return response.data;
}

export const login = async (email, password) => {
  const request = {
    email: email,
    password: password
  }
  const response = await api.post(`/api/auth/login`, request); // TODO: Match the route from backend
  return response.data;
}


export const getMovieNews = async () => {
  const response = await axiosInstance.get('/movie-news');
  return response.data;
}

export const addCard = async (data) => {
  const response = await api.post(`/addcard`, data);
  return response.data;
}