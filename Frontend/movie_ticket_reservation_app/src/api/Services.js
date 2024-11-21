import axios from 'axios'

const DEFAULT_PAGE_SIZE = 10;

const api = axios.create({
    baseURL: "http://localhost:8080", // Replace with your base API URL
    headers: {
      'Content-Type': 'application/json',
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
  const response = await api.get(`/showtimes/movie/${movieId}/theatre/${theatreId}`)
  return response.data;
}

export const getSeats = async (theatreId, showtimeId) => {
  const response = await api.get(`/seats/theatre/${theatreId}/showtime/${showtimeId}`)
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