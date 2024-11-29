import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import NavBar from './components/NavBar';
import CancelTicketPage from './pages/CancelTicket';
import BookMovie from './pages/BookMovie';
import UserRegistration from './pages/UserRegistration';
import BuyTickets from './pages/BuyTicket';
import TicketPaymentPage from './pages/TicketPaymentPage';
import { AuthProvider } from './context/AuthContext';

function App() {
  return (
    <AuthProvider>
      <Router>
        <NavBar />
        <Routes>
          <Route path="/" element={<BookMovie />} />
          <Route path="/BookMovie" element={<BookMovie />} />
          <Route path="/CancelTicket" element={<CancelTicketPage />} />
          <Route path="/UserRegistration" element={<UserRegistration />} />
          <Route path="/movie/:id" element={<BuyTickets />} />
          <Route path="/Payment" element={<TicketPaymentPage />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;