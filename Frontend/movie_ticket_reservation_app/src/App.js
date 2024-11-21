import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import NavBar from './components/NavBar';
import CancelTicket from './pages/CancelTicket';
import BookMovie from './pages/BookMovie';
import UserRegistration from './pages/UserRegistration';


function App() {

  return (
      <Router>
          <NavBar />
          <Routes>
              <Route path="/BookMovie" element={<BookMovie />} />
              <Route path="/CancelTicket" element={<CancelTicket />} />
              <Route path="/UserRegistration" element={<UserRegistration />} />
              
          </Routes>
      </Router>
  );
}

export default App;
