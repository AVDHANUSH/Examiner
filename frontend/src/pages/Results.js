import React, { useEffect, useState } from 'react';
import { Container, Typography, Card, CardContent, Grid } from '@mui/material';
import axios from 'axios';
import Cookies from 'js-cookie';

const Results = () => {
  const [results, setResults] = useState([]);

  useEffect(() => {
    const fetchResults = async () => {
      try {
        const token = Cookies.get('access_token'); // Assuming you store the token in localStorage
        const response = await axios.get('http://localhost:8080/api/v1/quizzes/quiz-results', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setResults(response.data);
      } catch (error) {
        console.error('Error fetching quiz results:', error);
      }
    };

    fetchResults();
  }, []);

  return (
    <Container>
      <Typography variant="h4" align="center" gutterBottom>
        Results Page
      </Typography>
      <Grid container spacing={3}>
        {results.map((result, index) => (
          <Grid item xs={12} sm={6} md={4} key={index}>
            <Card variant="outlined">
              <CardContent>
                <Typography variant="h6" color="primary" gutterBottom>
                  Quiz Number: {result.quizNumber}
                </Typography>
                <Typography variant="body1" gutterBottom>
                  Total Marks: {result.totalMarks}
                </Typography>
                <Typography variant="body2" gutterBottom>
                  Marks for Each Question:
                </Typography>
                <ul>
                  {result.marksForEachQuestion.map((mark, i) => (
                    <li key={i}>{mark}</li>
                  ))}
                </ul>
                <Typography variant="caption" color="textSecondary">
                  Completed on: {new Date(result.createdAt).toLocaleString()}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Container>
  );
};

export default Results;
