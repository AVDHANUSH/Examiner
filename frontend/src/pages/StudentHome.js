import React from 'react';
import { Grid, Card, CardContent, Typography, CardActionArea, Box, Container } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const StudentHome = () => {
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
        padding={3}
      >
        <Grid container spacing={4} justifyContent="center">
          <Grid item xs={12} sm={6} md={3}>
            <Card
              onClick={() => handleCardClick('/student/take-quiz')}
              sx={{ backgroundColor: '#f5f5f5', borderRadius: 2, boxShadow: 3 }}
            >
              <CardActionArea>
                <CardContent>
                  <Typography variant="h5" component="div" align="center">
                    Take Quiz
                  </Typography>
                </CardContent>
              </CardActionArea>
            </Card>
          </Grid>

          <Grid item xs={12} sm={6} md={3}>
            <Card
              onClick={() => handleCardClick('/student/results')}
              sx={{ backgroundColor: '#e3f2fd', borderRadius: 2, boxShadow: 3 }}
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
              onClick={() => handleCardClick('/student/attendance')}
              sx={{ backgroundColor: '#c8e6c9', borderRadius: 2, boxShadow: 3 }}
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
              onClick={() => handleCardClick('/student/past-quizzes')}
              sx={{ backgroundColor: '#ffe0b2', borderRadius: 2, boxShadow: 3 }}
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

export default StudentHome;
