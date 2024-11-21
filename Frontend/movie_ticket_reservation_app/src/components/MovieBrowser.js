import React, {useState, useEffect} from 'react'
import {CircularProgress, IconButton, TextField} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search'
import {InputAdornment} from '@mui/material';
import {Box, Divider} from '@mui/material'
import {Pagination} from '@mui/material';
import {List, ListItem, ListItemText, ListItemButton} from '@mui/material';
import {Grid2, Card, CardMedia, CardContent, Typography} from '@mui/material'
import { getGenres, getMovies, getMoviesByGenre, getMoviesBySearch } from '../api/Services'; 
import { Link, useNavigate } from 'react-router-dom';
function SearchBar({search, handleValueChange, handleSearch}) {
    const handleKeyDown = async (event) => {
        if(event.key === 'Enter') {
            await handleSearch()
        }
    }

    return (
        <Box>
            <TextField
            label='Search'
            value={search}
            onChange={(event) => {handleValueChange(event.target.value)}}
            onKeyDown={handleKeyDown}
            type='search'
            variant='standard'
            size='small'
            slotProps={{
                input: {
                    endAdornment: (
                        <InputAdornment position="end">
                            <IconButton type='submit' onClick={handleSearch}><SearchIcon /></IconButton>
                        </InputAdornment>
                    )
                }
            }}
            fullWidth
            />
        </Box>
    );
}

function GenreList({genres, handleClick, selectedGenre}) {
    
    const listItems = [];
    listItems.push(
        <ListItem key={0} disablePadding>
                <ListItemButton 
                sx={{padding: '0px'}} 
                onClick={() => handleClick(0)}                
                >
                    <ListItemText 
                    primary={'All'}
                    primaryTypographyProps={{
                        fontSize: '14px',
                        fontWeight: selectedGenre === 0 ? 'bold': 'normal'
                    }}
                    />
                </ListItemButton>
            </ListItem>
    );
    genres.forEach(genre => {
        listItems.push(
            <ListItem key={genre.id} disablePadding>
                <ListItemButton 
                sx={{padding: '0px'}} 
                onClick={()=>handleClick(genre.id)}
                >
                    <ListItemText 
                    primary={genre.title}
                    primaryTypographyProps={{
                        fontSize: '14px',
                        fontWeight: selectedGenre===genre.id ? 'bold': 'normal'
                    }}
                    />
                </ListItemButton>
            </ListItem>
        )
    })
    return (
        <Box>
            <List>{listItems}</List>
        </Box>
    );
}

function MovieSideBar({search, handleValueChange, handleSearch, 
    genres, selectedGenre, handleClick, 
    width=200, top=100, right=20}) {
    return (
        <Box sx={{
            width: width,
            height: '70vh',
            position: 'fixed',
            top: top,
            right: right,
            boxShadow: "0px 0px 10px rgba(0, 0, 0, 0.1)",
            padding: '20px',
            display: 'flex',
            flexDirection: 'column'
            }}>
            <Box
                sx={{
                    position: 'sticky',
                    top: 0,
                    zIndex: 1
                }}
            >
                <SearchBar 
                search={search}
                handleValueChange={handleValueChange} 
                handleSearch={handleSearch}/>
                <Divider sx={{my: 2}}/>
                <Typography variant='h6'>
                    Genre
                </Typography>
            </Box>
            <Box sx={{
                flexGrow: 1,
                overflowY: 'auto',
                marginTop: '8px',
                scrollbarWidth: 'none'
            }}
            >
                <GenreList genres={genres} selectedGenre={selectedGenre} handleClick={handleClick} />
            </Box>
        </Box>
    )
}

function MovieGrid({movies}) {
    let content;
    if(movies.length===0) {
        content = <Typography variant='h3'>No movies found.</Typography>
    } else {
        content = <Grid2
        container
        size={{xs: 12, sm: 6, md: 4, lg: 3, ex: 2}}
        spacing={2}
        sx={{scrollbarWidth: 'none'}}
        >
            {movies.map((movie) => (
                <Grid2 item xs={6} sm={4} md={3} key={movie.id}>
                    <Link to={"/movie/"+movie.id}>
                    <Card sx={{ 
                        maxWidth: 160, 
                        border: 'none',
                        boxShadow: 'none'
                        }}
                        >
                        <Box sx={{ position: 'relative' }}>
                        <CardMedia
                            component="img"
                            image={movie.image}
                            alt={movie.title}
                            sx={{ width: 160, height: 200 }}
                        />
                        </Box>
                        <CardContent sx={{ textAlign: 'center', padding: '8px 8px 4px 4px' }}>
                        <Typography variant="body2" noWrap>
                            {movie.title}
                        </Typography>
                        </CardContent>
                    </Card>
                    </Link>
                    
                </Grid2>
            ))}
    </Grid2>
    }
    return (
        <Box>
            {content}
        </Box>
    )
}

function MoviePagination({pagination, handlePageChange}) {
    return (
        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 2 , mb: 2}}>
        <Pagination
          count={pagination.totalPages} // Total number of pages
          page={pagination.currentPage}
          onChange={handlePageChange}
          color="primary"
        />
      </Box>
    );
}

function MovieDisplayer({movies, pagination, handlePageChange}) {
    return (
        <Box sx={{ flexGrow: 1, 
            mr: '280px', 
            overflowX: 'hidden' 
        }}>
            <MovieGrid movies={movies}/>
            <MoviePagination pagination={pagination} handlePageChange={handlePageChange} />
        </Box>
    );
}

function MovieBrowser() {
    const [movies, setMovies] = useState([]);
    const [genres, setGenres] = useState([]);
    const [pagination, setPagination] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedGenre, setSelectedGenre] = useState(0);
    const [search, setSearch] = useState("");

    useEffect(() => {
        const fetchData = async () => {
            try {
                const moviesData = await getMovies();
                const genresData = await getGenres();
                setMovies(moviesData.movies);
                setPagination(moviesData.pagination);
                setGenres(genresData)
            } catch (err) {
                setError(err.message)
            } finally {
                setLoading(false);
            }
        }

        fetchData();
    }, [])

    const handlePageChange = async (event, page) => {
        setLoading(true);
        try{
            let data;
            if (search.length!==0) {
                data = await getMoviesBySearch(search, page);
            }
            else if(selectedGenre === 0) {
                data = await getMovies(page);
            } else {
                data = await getMoviesByGenre(selectedGenre, page);
            }
            setMovies(data.movies);
            setPagination(data.pagination);
        } catch (err) {
            setError(err);
        } finally {
            setLoading(false);
        }
    }

    const handleClick = async (id) => {
        setLoading(true);
        setSelectedGenre(id);
        try {
            let data;
            if (id !== 0) {
                data = await getMoviesByGenre(id);               
            } else {
                data = await getMovies();
            }
            setMovies(data.movies);
            setPagination(data.pagination)
        } catch (err) {
            setError(err.message)
        } finally {
            setLoading(false);
        }  
    }

    const handleSearch = async () => {
        if(search.length===0) setSelectedGenre(0);
        else setSelectedGenre(-1);
        setLoading(true);
        try {
            const data = await getMoviesBySearch(search);
            setMovies(data.movies);
            setPagination(data.pagination)
        } catch (err) {
            setError(err.message)
        } finally {
            setLoading(false);
        } 
    }

    const LoadingScreen = () => {
        return (
            <Box sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
                mt: 30,
            }}>
                <CircularProgress />
                <Typography variant='body2'>
                    loading
                </Typography>
            </Box>)
    }
    
    const ErrorScreen = ({err}) => {
        return (
            <
            Box sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
                mt: 30,
            }}>
                <Typography variant='h6'>
                    {err}
                </Typography>
            </Box>)
    }

    if(loading) return <LoadingScreen />;

    if(error) return <ErrorScreen err={error} />;

    return (
        <Box sx={{
            display: 'flex', 
            flexDirection: 'row-reverse', 
            mt: '100px',
            ml: '100px',
            }}>   
            <MovieDisplayer 
            movies={movies} 
            pagination={pagination} 
            handlePageChange={handlePageChange}
            />
            <MovieSideBar 
            search={search}
            handleValueChange={setSearch}
            handleSearch={handleSearch}
            genres={genres} 
            selectedGenre={selectedGenre} 
            handleClick={handleClick}/>
        </Box>
    )
}

export default MovieBrowser;
