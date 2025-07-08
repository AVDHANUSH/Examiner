import React, { useState, useEffect } from 'react';
import { Grid, Card, CardContent, Typography, CardActionArea, Box, Container, CircularProgress } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';

const subjects = ['SCOPE', 'SENSE', 'SAS', 'SHINE', 'VIT BS', 'V-SIGN', 'SBST'];

const TakeQuiz = () => {
  const navigate = useNavigate();
  const [quizzes, setQuizzes] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchQuizzes = async () => {
      const accessToken = Cookies.get('access_token');

      try {
        const response = await fetch('http://localhost:8080/api/v1/quizzes/available', {
          method: 'GET',
          headers: {
            Authorization: `Bearer ${accessToken}`,
            'Content-Type': 'application/json',
          },
        });

        if (!response.ok) {
          throw new Error('Failed to fetch quizzes');
        }

        const quizIds = await response.json();

        // Check if we received an array of IDs
        if (Array.isArray(quizIds)) {
          // Map quiz IDs to subjects
          const quizzesWithSubjects = quizIds.map((id, index) => ({
            id,
            subject: subjects[index % subjects.length], // Use modulo to cycle through subjects
          }));

          setQuizzes(quizzesWithSubjects);
        } else {
          throw new Error('Invalid response format');
        }
      } catch (error) {
        console.error('Error fetching quizzes:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchQuizzes();
  }, []);

  const handleCardClick = (quizId) => {
    navigate(`/student/take-quiz/quiz-details/${quizId}`);
  };

  return (
    <Container>
      <Box
        display="flex"
        flexDirection="column"
        alignItems="center"
        minHeight="100vh"
        padding={3}
      >
        {loading ? (
          <CircularProgress />
        ) : (
          <Grid container spacing={4} justifyContent="center">
            {quizzes.map((quiz, index) => {
              // Alternate colors for cards
              const colors = [
                { background: '#e3f2fd', text: '#0d47a1' },
                { background: '#c8e6c9', text: '#1b5e20' },
                { background: '#ffe0b2', text: '#e65100' },
                { background: '#e1bee7', text: '#4a148c' },
                { background: '#fff9c4', text: '#f57f17' }
              ];
              const { background, text } = colors[index % colors.length];

              return (
                <Grid item xs={12} sm={6} md={4} key={quiz.id}>
                  <Card
                    onClick={() => handleCardClick(quiz.id)}
                    sx={{
                      backgroundColor: background,
                      color: text,
                      borderRadius: 2,
                      boxShadow: 3,
                      cursor: 'pointer',
                      transition: 'transform 0.3s ease, box-shadow 0.3s ease',
                      '&:hover': {
                        transform: 'scale(1.05)',
                        boxShadow: 6
                      }
                    }}
                  >
                    <CardActionArea>
                      <CardContent>
                        <Typography variant="h6" component="div" align="center" gutterBottom>
                          {quiz.subject}
                        </Typography>
                        <Typography variant="body2" align="center">
                          Quiz ID: {quiz.id}
                        </Typography>
                      </CardContent>
                    </CardActionArea>
                  </Card>
                </Grid>
              );
            })}
          </Grid>
        )}
      </Box>
    </Container>
  );
};

export default TakeQuiz;
