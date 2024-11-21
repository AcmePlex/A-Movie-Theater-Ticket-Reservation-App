import React,{ useState } from 'react';

import { Box, Typography, Container, TextField, Button,Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from '@mui/material';

function CancelTicket() {

    const [ticketNumber, setTicketNumber] = useState('');
    const [dialogOpen, setDialogOpen] = useState(false);
    const [notFoundDialogOpen, setNotFoundDialogOpen] = useState(false);

    const handleInputChange = (event) => {
        setTicketNumber(event.target.value);
    };

    const issueRefund = () => {
        // Implement the refund logic here
        console.log(`Refund issued for ticket number: ${ticketNumber}`);
    };

    const handleDialogClose = () => {
        setDialogOpen(false);
    };

    return (
        <Container>
            <Box sx={{ mt: 4 }}>
                <Typography variant="h2" component="h1" gutterBottom align="center">
                    Cancel Ticket
                </Typography>
                <TextField
                    label="Ticket Number"
                    variant="outlined"
                    fullWidth
                    value={ticketNumber}
                    onChange={handleInputChange}
                    sx={{ mb: 2 }}
                />
                <Button variant="contained" color="primary" onClick={issueRefund}>
                    cancel Ticket
                </Button>
            </Box>
            <Dialog open={dialogOpen} onClose={handleDialogClose}>
                <DialogTitle>Refund Issued</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Refund has been issued for ticket number: {ticketNumber}
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDialogClose} color="primary">
                        Close
                    </Button>
                </DialogActions>
            </Dialog>
            <Dialog open={notFoundDialogOpen} onClose={handleDialogClose}>
                <DialogTitle>Ticket Not Found</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Ticket number: {ticketNumber} was not found.
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDialogClose} color="primary">
                        Close
                    </Button>
                </DialogActions>
            </Dialog>
        </Container>
    );
}

export default CancelTicket;