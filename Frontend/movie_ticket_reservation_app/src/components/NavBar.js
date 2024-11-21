import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { AppBar, Toolbar, IconButton, Button, Menu, MenuItem, Box, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, TextField } from '@mui/material';
import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown';


function NavBar() {

    const [open, setOpen] = useState(false);
    const [loggedIn, setLoggedIn] = useState(false);
    const [userName, setUserName] = useState('');
    const [anchorEl, setAnchorEl] = useState(null);

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleLogin = () => {
        // Simulate login action
        setUserName('JX'); // Replace with actual user name
        setLoggedIn(true);
        setOpen(false);
    };

    const handleMenuOpen = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleMenuClose = () => {
        setAnchorEl(null);
    };

    return (
        <AppBar position="static">
            <Toolbar>
                <Box sx={{ flexGrow: 1 }}>
                    <Button color="inherit" component={Link} to="/BookMovie">Book Movie</Button>
                    <Button color="inherit" component={Link} to="/CancelTicket">Cancel Ticket</Button>
                    <Button color="inherit" component={Link} to="/UserRegistration">User Registration</Button>
                </Box>
                <Box sx={{ flexGrow: 0 }}>
                    {loggedIn ? (
                        <>
                            <Button
                                color="inherit"
                                endIcon={<ArrowDropDownIcon />}
                                onClick={handleMenuOpen}
                            >
                                {userName}
                            </Button>
                            <Menu
                                anchorEl={anchorEl}
                                open={Boolean(anchorEl)}
                                onClose={handleMenuClose}
                            >
                                <MenuItem onClick={handleMenuClose}>Subscription Renew</MenuItem>
                            </Menu>
                        </>
                    ) : (
                        <Button color="inherit" onClick={handleClickOpen}>Login</Button>
                    )}
                </Box>
            </Toolbar>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Login</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Please enter your login details.
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="name"
                        label="Email Address"
                        type="email"
                        fullWidth
                        variant="standard"
                    />
                    <TextField
                        margin="dense"
                        id="password"
                        label="Password"
                        type="password"
                        fullWidth
                        variant="standard"
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleLogin}>Login</Button>
                </DialogActions>
            </Dialog>
        </AppBar>
    );

}

export default NavBar;