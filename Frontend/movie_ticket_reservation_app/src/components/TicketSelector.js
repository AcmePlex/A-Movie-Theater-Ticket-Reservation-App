import React, {useState, useEffect} from "react";
import { Box, Divider, Button, ButtonGroup, Card, CardMedia, IconButton, List, ListItem, ListItemButton, ListItemText, Typography } from "@mui/material";
import CheckIcon from '@mui/icons-material/Check';
import { ChevronLeft } from "@mui/icons-material";
import { useHorizontalScroll } from "../utils/horizontalScroll";
import { getMovieDetail, getTheatres, getShowtimes, getSeats, bookTickets, getTotalPrice } from "../api/Services";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import LoadingScreen from "./LoadingScreen";

function MovieOverview({movie}) {
    return (
        <Box sx={{display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
            alignSelf: 'flex-start'
        }}>
            <Box
            sx={{display: 'flex',
                flexDirection: 'column',
                width: 300
            }}
            >
                <Typography variant="h3">
                    {movie?.title}
                </Typography>
                <Card>
                    <Box>
                        <CardMedia 
                            component='img'
                            image={movie?.image}
                            sx={{width: 300, height: 380}}
                        />
                    </Box>
                </Card>
            </Box>
        </Box>
    );
}

function MovieDetail({movie, handleBuyTickets}) {
    return (
        <Box
            sx={{display: 'flex', 
                flexDirection: 'column',
                gap: 2,
                padding: 6,
                width: 500
            }}
        >
            <Typography variant="h6">
                Information
            </Typography>
            <Typography variant="body">
                Genre: {movie?.genre}, Length: {movie?.length} min
            </Typography>
            <Typography variant="body">
                Release date: {movie?.releaseDate}
            </Typography>
            <Typography variant="body">
                {movie?.description}
            </Typography>
            {
                !movie?.hasShowtime && 
                <Typography variant="body" color="error">
                    This movie has no showtime right now.
                </Typography>
            }
            <Button disabled={!movie?.hasShowtime} variant="contained" sx={{width: 200, mt: 4}} onClick={()=>{handleBuyTickets(1)}}>
                Buy tickets
            </Button>
        </Box>
    );
}

function TheatreSelector({theatres, selected, setTheatre, handleBuyTickets, handleBack}) {
    const [selectedId, setSelectedId] = useState(selected);

    return (
        <Box
        sx={{display: 'flex',
            flexDirection: 'column',
            gap: 2,
            width: 500
        }}
        >
            <Box sx={{display: 'flex', gap: 2, alignItems: 'center', justifyContent: 'space-between'}}>
                <Box><IconButton sx={{padding: 0}} onClick={()=>{handleBack(2)}}><ChevronLeft /></IconButton></Box>
                <Box>Select theatre</Box>
                <Box width={24}></Box>
            </Box>
            <Box>
                <List>
                    {
                        theatres.map((theatre)=>
                            <ListItem key={theatre.id} disablePadding
                            >
                                <ListItemButton sx={{padding: 0}} onClick={()=>{setSelectedId(theatre.id)}}>
                                <ListItemText 
                                primary={theatre.name} 
                                secondary={theatre.location}
                                />
                                <CheckIcon color="success" sx={{visibility: selectedId===theatre.id?'visible':'hidden'}}/>
                                </ListItemButton>
                            </ListItem>
                        )
                    }
                </List>
            </Box>
            <Box sx={{display: 'flex', justifyContent: 'center'}}>
                <Button disabled={selectedId===0} variant="contained" onClick={()=>{setTheatre(selectedId); handleBuyTickets(2);}}>Confirm</Button>
            </Box>
        </Box>
    );
}

function ShowtimeSelector({showtimes, selected, setShowtime, handleBuyTickets, handleBack}) {
    const dates = Object.keys(showtimes);
    const [selectedDate, setSelectedDate] = useState(null);
    const scrollRef = useHorizontalScroll();
    const [showtimeList, setShowtimeList] = useState([]);
    const [selectedId, setSeletedId] = useState(selected);
    
    useEffect(()=>{
        setSelectedDate(dates[0]);
        setShowtimeList(showtimes[dates[0]])
    }, [])

    function convertToLocaltime(timestamp) {
        const date = new Date(timestamp);

        const localTime = date.toLocaleTimeString('en-US', {
          hour: '2-digit',
          minute: '2-digit',
          hour12: false, // Use `true` for 12-hour format
        });
      
        return localTime;
    }

    return (
        <Box
        sx={{display: 'flex',
            flexDirection: 'column',
            gap: 2,
            width: 500
        }}
        >
            <Box sx={{display: 'flex', gap: 2, alignItems: 'center', justifyContent: 'space-between'}}>
                <Box><IconButton sx={{padding: 0}} onClick={()=>{handleBack(3)}}><ChevronLeft /></IconButton></Box>
                <Box>Select showtime</Box>
                <Box width={24}></Box>
            </Box>
            <Box
            ref={scrollRef}
            sx={{
            display: "flex",
            overflowX: "auto",
            whiteSpace: "nowrap",
            '&::-webkit-scrollbar': {
                display: "none",
            },
            }}>
                <ButtonGroup variant="outlined">
                    {
                        dates.map((date, index)=> 
                            <Button key={index}
                            variant={selectedDate===date?"contained":"outlined"}
                            onClick={(event)=>{setShowtimeList(showtimes[date]); setSelectedDate(date)}}
                            sx={{width: 100, paddingLeft: 2, paddingRight: 2}}
                            fullWidth
                            >
                                {date}
                            </Button>
                        )
                    }
                </ButtonGroup>
            </Box>
            <Box sx={{
                height: 330,
                overflowY: 'auto',
                scrollbarWidth: 'none'
            }}>
                <List>
                    {showtimeList.map((showtime, index)=>(
                        <React.Fragment key={showtime.id}>
                            <ListItem  key={showtime.id}>
                                <ListItemButton 
                                onClick={()=>{setSeletedId(showtime.id)}}
                                disabled = {showtime.state==='Full' || showtime.state==='Closed' || showtime.state=='Playing'}
                                >
                                    <ListItemText 
                                    primary={convertToLocaltime(showtime.startTime) + " - " + convertToLocaltime(showtime.endTime)}
                                    secondary={(showtime.tickets-showtime.ticketsSold) + " tickets left"}
                                    secondaryTypographyProps={{color: showtime?.preAnnouncement ? 'success': 'text.secondary'}}
                                    />
                                    <ListItemText 
                                        primary={'Showroom ' + showtime?.showroomName}
                                        secondary={showtime?.preAnnouncement ? "early tickets": ""}
                                        secondaryTypographyProps={{color: 'success'}}
                                    />
                                    {
                                        selectedId === showtime.id && <CheckIcon color="success"/>
                                    }
                                    <Typography variant="body2">
                                        {showtime.state}
                                    </Typography>                                    
                                </ListItemButton>
                            </ListItem>
                            {index < showtimeList.length - 1 && <Divider />}
                        </React.Fragment>
                    ))}
                </List>
            </Box>
            <Box sx={{display: 'flex', justifyContent: 'center'}}>
                <Button disabled={selectedId===0} variant="contained" onClick={()=>{setShowtime(selectedId); handleBuyTickets(3)}}>Confirm</Button>
            </Box>
        </Box>
    );
}

function SeatArea({seatsData, selected, setSelected, selectLimit}) {
    const seats = seatsData.seats;
    const rows = seatsData.rows;
    const columns = seatsData.columns;
    let boxSize = Math.floor((70-columns)/3);
    const selectMax = 5;

    const seatStyles = (state) => {
        switch (state) {
          case "available":
            return {
                border: "2px solid darkgrey", // Outlined for available seats
                backgroundColor: "transparent",
                cursor: "pointer",
              };
          case "reserved":
            return {
                border: "2px solid darkgrey",
                backgroundColor: "lightgrey", // Filled for selected seats
              };
          case "selected":
            return { 
                border: "2px solid lightgreen",
                backgroundColor: "lightgreen",
                cursor: "pointer"
            };
          default:
            return {};
        }
      };
      return (
        <Box sx={{ display: "flex", flexDirection: "column", gap: '2px' }}>
          {/* Render rows */}
          {Array.from({ length: rows }).map((_, rowIndex) => (
            <Box
              key={rowIndex}
              sx={{
                display: "flex",
                gap: '2px',
                justifyContent: "center",
              }}
            >
              {/* Render columns */}
              {Array.from({ length: columns }).map((_, colIndex) => {
                const seat = seats.find(
                  (s) =>
                    s.showroomRow === rowIndex + 1 &&
                    s.showroomColumn === colIndex + 1
                );
                return (
                    <Box
                      key={seat ? seat.id : `${rowIndex}-${colIndex}`}
                      sx={{
                        width: boxSize,
                        height: boxSize,
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        borderRadius: 1,
                        backgroundColor: seat ? seatStyles(seat.state).backgroundColor : "transparent",
                        border: seat ? seatStyles(seat.state).border : "none",
                        cursor: seat ? seatStyles(seat.state).cursor : "default",
                      }}
                      onClick={() =>{
                        if(seat) {
                            if (seat.state==='available'){
                                if(selected.length >= Math.min(selectLimit, selectMax)) {
                                    alert("You can only select up to " + Math.min(selectLimit, selectMax) + " seats");
                                    return;
                                }
                                seat.state = 'selected'
                                setSelected([...selected, seat.id]);
                                console.log(selected);
                            } else if(seat.state==='selected') {
                                seat.state = 'available';
                                setSelected(selected.filter(num => num!==seat.id));   
                            }
                        }
                      }
                      }
                    >
                    </Box>
                  );
              })}
            </Box>
          ))}
        </Box>
      );
}

function SeatSelector({handleBack, seats, selected, setSelected, selectLimit}) {
    const [selectedSeats, setSelectedSeats] = useState(selected);
    return (
        <Box
        sx={{display: 'flex',
            flexDirection: 'column',
            gap: 2
        }}
        >
            <Box sx={{display: 'flex', gap: 2, alignItems: 'center', justifyContent: 'space-between'}}>
                <Box><IconButton sx={{padding: 0}} onClick={()=>{handleBack(4)}}><ChevronLeft /></IconButton></Box>
                <Box>Select seats</Box>
                <Box width={24}></Box>
            </Box>
            <Box sx={{display: 'flex', flexDirection: 'column', 
                alignItems: 'center', gap: 10, 
                border:'2px solid grey', borderRadius: '5px',
                padding: 5, paddingTop: 1
                }}>
                <Box sx={{width: '80%', height: '10px', border: '2px solid darkgrey'}}></Box>
                <SeatArea 
                seatsData={seats}
                selected={selectedSeats}
                setSelected={setSelectedSeats}
                selectLimit={selectLimit}
                />
            </Box>
            <Box sx={{display: 'flex', justifyContent: 'center'}}>
                <Button disabled={selectedSeats.length===0} 
                variant="contained"
                onClick={()=>{console.log(selectedSeats); setSelected(selectedSeats);}}
                >Confirm</Button>
            </Box>
        </Box>
    )
}

function BookingStepper({step, movie, handleBuyTickets, handleBack,
                        theatres, selectedTheatre, setTheatre,
                        showtimes, selectedShowtime, setShowtime,
                        seats, selectedSeats, setSelectedSeats, selectLimit}) {
    return (
        <Box sx={{
            width: 500,
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center'
            }}>
            {step===1 && 
            <MovieDetail 
            movie={movie}
            handleBuyTickets={handleBuyTickets}
            />}
            {step===2 && 
            <TheatreSelector 
            setTheatre={setTheatre}
            handleBuyTickets={handleBuyTickets}
            handleBack={handleBack}
            theatres={theatres} 
            selected={selectedTheatre}/>
            }
            {step===3 &&
            <ShowtimeSelector 
            handleBuyTickets={handleBuyTickets}
            setShowtime={setShowtime}
            handleBack={handleBack}
            showtimes={showtimes} 
            selected={selectedShowtime} />
            }
            {step===4 &&
            <SeatSelector 
            handleBack={handleBack}
            seats={seats} 
            selected={selectedSeats}
            setSelected={setSelectedSeats}
            selectLimit={selectLimit}
            />
            }
        </Box>
    );
}

function TicketSelector() {
    const [movie, setMovie] = useState(null);
    const [theatre, setTheatre] = useState(null);
    const [showtime, setShowtime] = useState(null);
    const [selectedSeats, setSelectedSeats] = useState([]);
    const [step, setStep] = useState(1);
    const [steps, setSteps] = useState([1, 2, 3, 4]);
    const [theatres, setTheatres] = useState([]);
    const [showtimes, setShowtimes] = useState([]);
    const [seats, setSeats] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const {id: movieId} = useParams();
    const [selectLimit, setSelectLimit] = useState(0);

    const handleBuyTickets = (currentStep) => {
        setStep(currentStep+1);
    }

    const handleBack = (currentStep) => {
        setStep(currentStep-1);
    }

    const handleConfirmTheatre = async (id) => {
        const theTheatre = theatres.filter((theatre)=>theatre.id===id).pop();
        setTheatre(theTheatre);
        try {
            setLoading(true);
            const data = await getShowtimes(movieId, theTheatre.id);
            console.log(data);
            setShowtimes(data);
        } catch (err) {
            setError(err);
        } finally {
            setLoading(false);
        }
    }

    const handleConfirmShowtime = async (id) => {
        const allShowtimes = Object.values(showtimes).flat(); 
        const theShowtime = allShowtimes.find((showtime)=>showtime.id===id);
        setSelectLimit(theShowtime.tickets - theShowtime.ticketsSold);
        setShowtime(theShowtime);
        try {
            setLoading(true);
            const data = await getSeats(theShowtime.showroomId, id);
            setSeats(data);
        } catch (err) {
            setError(err);
        } finally {
            setLoading(false);
        }
    }

    const navigate = useNavigate();


    // this is temporary, need to redirect to payment
    const handleConfirmSeats = async (ids) => {
        setSelectedSeats(ids);
        const email = "example@example.com";
        const requestData = {
            ids: ids,
            email: email
        }
        try {
            setLoading(true);
            //const data = await bookTickets(requestData);
            const price = await getTotalPrice(requestData);
            // console.log(data);
            // alert(data.message);
            navigate("/Payment", { state: {ids:ids, totalPrice: price} });
        } catch (err) {
            setError(err);
        } finally {
            setLoading(false);
        }
    }

    useEffect(()=>{
        const fetchMovieDetail = async () => {
            setLoading(true);
            try {
                const data = await getMovieDetail(movieId);
                const theatresData = await getTheatres(movieId);
                setMovie(data);
                setTheatres(theatresData);
            } catch (err) {
                setError(err.message)
            } finally {
                setLoading(false);
            }
        }
        fetchMovieDetail();
        setStep(1);
    }, []);

    if(loading) return <LoadingScreen />
    if(error) return <p>Network error</p>  

    return (
        <Box 
        sx={{display: 'flex',
            flexDirection: 'row',
            gap: 15,
            justifyContent: 'center',
            mt: 5
        }}>
            <MovieOverview movie={movie}/>
            <BookingStepper 
            step={step} 
            movie={movie}
            handleBuyTickets={handleBuyTickets}
            handleBack={handleBack}
            theatres={theatres} 
            selectedTheatre={theatre?theatre.id:0} 
            setTheatre={handleConfirmTheatre}
            showtimes={showtimes}
            selectedShowtime={showtime?showtime.id:0}
            setShowtime={handleConfirmShowtime}
            seats={seats}
            selectedSeats={selectedSeats}
            setSelectedSeats={handleConfirmSeats}
            selectLimit={selectLimit}
            />
        </Box>
    );
}

export default TicketSelector;