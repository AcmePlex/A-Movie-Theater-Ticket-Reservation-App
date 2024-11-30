import React, { useState, useEffect } from 'react';
import { Box, Typography, Container, TextField, Button, Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, RadioGroup, FormControlLabel, Radio,Snackbar, Alert  } from '@mui/material';
import { useLocation } from 'react-router-dom';
import { bookTickets, ticketPayment } from '../api/Services';
import { useNavigate } from 'react-router-dom';

function TicketPaymentPage() {
    const location=useLocation();
    const [email, setEmail] = useState('');
    const [creditCard, setCreditCard] = useState('');
    const [dialogOpen, setDialogOpen] = useState(false);

    const [expireMonth, setExpireMonth] = useState('');
    const [expireYear, setExpireYear] = useState('');
    const [cvv, setCvv] = useState('');
    const [cardname, setName] = useState('');
    const [method, setCardType] = useState('credit');

    const [confirmationDialogOpen, setConfirmationDialogOpen] = useState(false);
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [snackbarSeverity, setSnackbarSeverity] = useState('success');
    const [responseMessage, setResponseMessage] = useState(''); // Add state for response message


    const navigate = useNavigate();


    const { ids, totalPrice} = location.state || { ids: [], totalPrice: 0};

    const handleSubmit = () => {
        setConfirmationDialogOpen(true);
    };

    const handleDialogClose = () => {
        setConfirmationDialogOpen(false);
    };

    const handleSnackbarClose = () => {
        setSnackbarOpen(false);
    };

    const handlePayment = async () => {
        const paymentData = {
            ids,
            email,
            totalPrice,
            method
        };
        console.log('Payment Data:', paymentData);

        const requestData = {
            ids,
            email
        };

        try {
            const data = await bookTickets(requestData);
            console.log(data);
            if(!data?.error){
                const response = await ticketPayment(paymentData);

                console.log(response);
                setResponseMessage(response|| 'Your payment has been processed successfully.'); // Set response message
                setSnackbarMessage('Payment successful. Confirmation Email Set');
                setSnackbarSeverity('success');
                setSnackbarOpen(true);
                setDialogOpen(true);
                setConfirmationDialogOpen(false);
            }else{
                setResponseMessage('Failed to book ticket. Please contact admin') // Set response message
                setDialogOpen(true);
                setConfirmationDialogOpen(false);
            }
            
        } catch (error) {
            console.error('Error processing payment:', error);
            setSnackbarMessage('Error processing payment. Please try again.');
            setSnackbarSeverity('error');
            setSnackbarOpen(true);
        }
    };

    const handlePaymentDialogClose = () => {
        setDialogOpen(false);
        navigate('/');
    };

    return (
        <Container>
            <Box sx={{ pt: 0, pl: 3 }}>
                <Typography variant="h2" component="h1" gutterBottom align="center">
                    Ticket Payment
                </Typography>
                <Typography variant="h5" component="h2" gutterBottom align="center">
                    Total Price: ${totalPrice}
                </Typography>
                <TextField
                    label="Email"
                    variant="outlined"
                    fullWidth
                    required
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    sx={{ mb: 2 }}
                />
                <Button variant="contained" color="primary" onClick={handleSubmit}>
                    Pay Now
                </Button>
            </Box>
            <Dialog open={confirmationDialogOpen} onClose={handleDialogClose}>
                <DialogTitle>Enter Payment Details</DialogTitle>
                <DialogContent>
                <RadioGroup
                        aria-label="cardType"
                        name="cardType"
                        value={method}
                        onChange={(e) => setCardType(e.target.value)}
                        row
                    >
                        <FormControlLabel value="credit" control={<Radio />} label="Credit" />
                        <FormControlLabel value="debit" control={<Radio />} label="Debit" />
                    </RadioGroup>
                    <TextField
                        label="Card Number"
                        variant="outlined"
                        fullWidth
                        required
                        value={creditCard}
                        onChange={(e) => setCreditCard(e.target.value)}
                        sx={{ mb: 2 }}
                    />
                    <TextField
                        label="Expire Month"
                        variant="outlined"
                        fullWidth
                        required
                        value={expireMonth}
                        onChange={(e) => setExpireMonth(e.target.value)}
                        slotProps={{ htmlinput: { min: 1, max: 12 } }}
                        type="number"
                        sx={{ mb: 2 }}
                    />
                    <TextField
                        label="Expire Year"
                        variant="outlined"
                        fullWidth
                        required
                        value={expireYear}
                        onChange={(e) => setExpireYear(e.target.value)}
                        type="number"
                        sx={{ mb: 2 }}
                    />
                    <TextField
                        label="CVV"
                        variant="outlined"
                        fullWidth
                        required
                        value={cvv}
                        onChange={(e) => setCvv(e.target.value)}
                        slotProps={{ htmlinput: {maxLength:3} }}
                        type="number"
                        sx={{ mb: 2 }}
                    />
                    <TextField
                        label="Name on Card"
                        variant="outlined"
                        fullWidth
                        required
                        value={cardname}
                        onChange={(e) => setName(e.target.value)}
                        sx={{ mb: 2 }}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDialogClose} color="primary">
                        Cancel
                    </Button>
                    <Button onClick={handlePayment} color="primary">
                        Confirm Info
                    </Button>
                </DialogActions>
            </Dialog>
            <Dialog open={dialogOpen} onClose={handlePaymentDialogClose}>
                <DialogTitle>Payment Successful</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        {responseMessage} {/* Display response message */}
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handlePaymentDialogClose} color="primary">
                        Close
                    </Button>
                </DialogActions>
            </Dialog>
            <Snackbar open={snackbarOpen} autoHideDuration={6000} onClose={handleSnackbarClose}>
                <Alert onClose={handleSnackbarClose} severity={snackbarSeverity} sx={{ width: '100%' }}>
                    {snackbarMessage}
                </Alert>
            </Snackbar>
        </Container>
    );
}

export default TicketPaymentPage;