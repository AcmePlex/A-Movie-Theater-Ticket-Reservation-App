import React, { useState, useContext } from 'react';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  Menu,
  MenuItem,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  TextField,
  DialogActions,
} from '@mui/material';
import { Link, useNavigate } from 'react-router-dom'; // Import useNavigate
import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown';
import { membershipPayment, login as apiLogin } from '../api/Services';
import { AuthContext } from '../context/AuthContext';

function NavBar() {
  const { auth, login: authLogin, logout: authLogout } = useContext(AuthContext);
  const navigate = useNavigate(); // Initialize useNavigate

  const [anchorEl, setAnchorEl] = useState(null);
  const [open, setOpen] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errorDialogOpen, setErrorDialogOpen] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleLoginOpen = () => {
    setOpen(true);
  };

  const handleLoginClose = () => {
    setOpen(false);
  };

  const handleErrorDialogClose = () => {
    setErrorDialogOpen(false);
  };

  const handleLogin = async () => {
    try {
      const response = await apiLogin(email, password);
      console.log('Login response:', response);
      if (response?.token) {
        authLogin(response.token); // Update AuthContext with token
        setOpen(false);
        setEmail('');
        setPassword('');
        navigate('/dashboard'); // Navigate to Dashboard after login
      } else if (response?.expired) {
        alert(response.error);
      } else if (response?.wrong) {
        setErrorMessage(response.message);
        setErrorDialogOpen(true);
      } else {
        // Handle unexpected responses
        setErrorMessage('An unexpected error occurred.');
        setErrorDialogOpen(true);
      }
    } catch (error) {
      console.error('Login error:', error);
      setErrorMessage('Failed to login. Please try again.');
      setErrorDialogOpen(true);
    }
  };

  const handleLogout = () => {
    authLogout(); // Clear authentication in AuthContext
    handleMenuClose();
    navigate('/'); // Optionally navigate to home after logout
  };

  const handleSubscriptionRenew = async () => {
    if (!auth.user || !auth.user.email) {
      alert('User email not available.');
      return;
    }

    try {
      const response = await membershipPayment(auth.user.email, 'credit');
      console.log('Membership payment successful:', response);
      alert('Membership renewed successfully.');
    } catch (error) {
      console.error('Error renewing membership:', error);
      alert('Failed to renew membership. Please try again.');
    }
    handleMenuClose();
  };

  return (
    <AppBar position="static">
      <Toolbar>
        <Box sx={{ flexGrow: 1 }}>
          <Button color="inherit" component={Link} to="/BookMovie">
            Book Movie
          </Button>
          <Button color="inherit" component={Link} to="/CancelTicket">
            Cancel Ticket
          </Button>
          <Button color="inherit" component={Link} to="/UserRegistration">
            User Registration
          </Button>
        </Box>
        <Box sx={{ flexGrow: 0 }}>
          {auth.user ? (
            <>
              <Button
                color="inherit"
                endIcon={<ArrowDropDownIcon />}
                onClick={handleMenuOpen}
              >
                {auth.user.name || 'User'}
              </Button>
              <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
              >
                <MenuItem onClick={handleSubscriptionRenew}>
                  Subscription Renew
                </MenuItem>
                <MenuItem onClick={handleLogout}>Logout</MenuItem>
                <MenuItem component={Link} to="/dashboard">
                  Dashboard
                </MenuItem>
              </Menu>
            </>
          ) : (
            <Button color="inherit" onClick={handleLoginOpen}>
              Login
            </Button>
          )}
        </Box>
      </Toolbar>

      {/* Login Dialog */}
      <Dialog open={open} onClose={handleLoginClose}>
        <DialogTitle>Login</DialogTitle>
        <DialogContent>
          <DialogContentText>Please enter your login details.</DialogContentText>
          <TextField
            autoFocus
            margin="dense"
            id="email"
            label="Email Address"
            type="email"
            fullWidth
            variant="standard"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <TextField
            margin="dense"
            id="password"
            label="Password"
            type="password"
            fullWidth
            variant="standard"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleLoginClose} color="primary">
            Cancel
          </Button>
          <Button onClick={handleLogin} color="primary">
            Login
          </Button>
        </DialogActions>
      </Dialog>

      {/* Error Dialog */}
      <Dialog open={errorDialogOpen} onClose={handleErrorDialogClose}>
        <DialogTitle>Error</DialogTitle>
        <DialogContent>
          <DialogContentText>{errorMessage}</DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleErrorDialogClose} color="primary">
            Close
          </Button>
        </DialogActions>
      </Dialog>
    </AppBar>
  );
}

export default NavBar;