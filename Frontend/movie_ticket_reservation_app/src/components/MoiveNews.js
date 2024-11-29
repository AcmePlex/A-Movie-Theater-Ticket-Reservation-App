import { Box, Dialog, List, Card, CardMedia } from "@mui/material";
import { DialogContent, DialogTitle, DialogActions, Button, Typography, ListItem, Image } from "@mui/material";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router";
import { getMovieNews } from "../api/Services";

function MovieNews({open, onClose}) {
  const navigate = useNavigate();
  const [movieNews, setMovieNews] = useState([]);
  const viewMovieDetail = (id) => { navigate(`/movie/${id}`); };
  const [loading, setLoading] = useState(false);

  const convertToDate = (timestamp) => {
    const date = new Date(timestamp);
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${month}-${day}`;
  };

  const convertToTime = (timestamp) => {
    const date = new Date(timestamp);
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${hours}:${minutes}`;
  };

  useEffect(() => {
    // Fetch movie news here
    const fetchMovieNews = async () => {
      try {
        setLoading(true);
        const data = await getMovieNews();
        setMovieNews(data);
      } catch (error) {
        console.error('Error fetching movie news:', error);
      } finally {
        setLoading(false);
      }
    };
    if(open) {
      fetchMovieNews();
    }
  }, [open]);

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle alignSelf='center'>Movie News</DialogTitle>
      <DialogContent>
        <Typography variant="body1" color="success">
          Some showtimes are offering early tickets!
        </Typography>
        {
          !loading &&
          <List>
          {movieNews.map((news, index) => (
            <ListItem key={index}>
              <Box sx={{display: 'flex', alignItems: 'center', padding: '5px', gap: 1}}>
                <Card sx={{borderRadius: 0}}>
                  <CardMedia
                    component="img"
                    image={news.image}
                    alt={news.movieTitle}
                    sx={{ width: 120, height: 140 }}
                  />
                </Card>
                <Box sx={{display: 'flex', flexDirection: 'column', padding: '5px', gap: 1}}>
                  <Typography variant="h6">{news.movieTitle}</Typography>
                  <Typography variant="body2">{`${convertToDate(news.startTime)} \t ${convertToTime(news.startTime)} - ${convertToTime(news.endTime)}`}</Typography>
                  <Typography variant="body2">{news.theatreName}</Typography>
                </Box>
                <Button variant="contained" color="primary" onClick={()=>{viewMovieDetail(news.movieId)}}>Take a look</Button>
              </Box>
            </ListItem>
          ))}
        </List>
        }
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="primary">
          Close
        </Button>
      </DialogActions>
    </Dialog>
  )
}

export default MovieNews;