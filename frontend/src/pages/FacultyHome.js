import React from 'react';
import { Grid, Card, CardContent, Typography, CardActionArea, Box, Container } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const FacultyHome = () => {
  const navigate = useNavigate();

  const handleCardClick = (route) => {
    navigate(route);
  };

  return (
    <Container>
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        minHeight="100vh"
      >
        <Grid container spacing={4} justifyContent="center">
          <Grid item xs={12} sm={6} md={3}>
            <Card 
              onClick={() => handleCardClick('/faculty/create-quiz')}
              sx={{
                backgroundColor: '#1976d2',
                color: 'white',
                '&:hover': {
                  backgroundColor: '#1565c0',
                }
              }}
            >
              <CardActionArea>
                <CardContent>
                  <Typography variant="h5" component="div" align="center">
                    Create Quiz
                  </Typography>
                </CardContent>
              </CardActionArea>
            </Card>
          </Grid>

          <Grid item xs={12} sm={6} md={3}>
            <Card 
              onClick={() => handleCardClick('/faculty/results')}
              sx={{
                backgroundColor: '#388e3c',
                color: 'white',
                '&:hover': {
                  backgroundColor: '#2e7d32',
                }
              }}
            >
              <CardActionArea>
                <CardContent>
                  <Typography variant="h5" component="div" align="center">
                    Results
                  </Typography>
                </CardContent>
              </CardActionArea>
            </Card>
          </Grid>

          <Grid item xs={12} sm={6} md={3}>
            <Card 
              onClick={() => handleCardClick('/faculty/attendance')}
              sx={{
                backgroundColor: '#f57c00',
                color: 'white',
                '&:hover': {
                  backgroundColor: '#ef6c00',
                }
              }}
            >
              <CardActionArea>
                <CardContent>
                  <Typography variant="h5" component="div" align="center">
                    Attendance
                  </Typography>
                </CardContent>
              </CardActionArea>
            </Card>
          </Grid>

          <Grid item xs={12} sm={6} md={3}>
            <Card 
              onClick={() => handleCardClick('/faculty/past-quizzes')}
              sx={{
                backgroundColor: '#d32f2f',
                color: 'white',
                '&:hover': {
                  backgroundColor: '#c62828',
                }
              }}
            >
              <CardActionArea>
                <CardContent>
                  <Typography variant="h5" component="div" align="center">
                    Past Quizzes
                  </Typography>
                </CardContent>
              </CardActionArea>
            </Card>
          </Grid>
        </Grid>
      </Box>
    </Container>
  );
};

export default FacultyHome;
