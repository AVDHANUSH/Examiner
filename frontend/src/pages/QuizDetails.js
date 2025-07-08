import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Container, Box, Typography, TextField, Button, Card, CardContent, Dialog, DialogActions, DialogContent, DialogTitle, IconButton, Snackbar, Alert } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';

const QuizDetails = () => {
  const { quizId } = useParams();
  const [quiz, setQuiz] = useState(null);
  const [answers, setAnswers] = useState([]);
  const [isSubmitted, setIsSubmitted] = useState(false);
  const [isCopyPasteModalOpen, setIsCopyPasteModalOpen] = useState(false);
  const [isTabModalOpen, setIsTabModalOpen] = useState(false);
  const navigate = useNavigate();
  useEffect(() => {
    const fetchQuizDetails = async () => {
      const accessToken = Cookies.get('access_token');

      try {
        const response = await fetch(`http://localhost:8080/api/v1/quizzes/${quizId}/questions`, {
          method: 'GET',
          headers: {
            Authorization: `Bearer ${accessToken}`,
            'Content-Type': 'application/json',
          },
        });

        if (!response.ok) {
          throw new Error('Failed to fetch quiz details');
        }

        const quizData = await response.json();
        console.log('Fetched quiz data:', quizData);
        setQuiz(quizData);

        // Initialize answers state
        const initialAnswers = quizData.map((q) => ({
          text: q.text,
          type: q.type,
          options: q.options,
          answer: q.type === 'MULTIPLE_CHOICE' ? '' : '',
        }));
        setAnswers(initialAnswers);
      } catch (error) {
        console.error('Error fetching quiz details:', error);
      }
    };

    fetchQuizDetails();
  }, [quizId]);

  const handleAnswerChange = (index, answer) => {
    const updatedAnswers = [...answers];
    updatedAnswers[index].answer = answer;
    setAnswers(updatedAnswers);
  };

  const handleSubmit = async () => {
    const accessToken = Cookies.get('access_token');

    const submissionData = {
      quizNumber: quizId,
      questions: answers,
    };

    try {
      const response = await fetch('http://localhost:8080/api/v1/quizzes/evaluate', {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${accessToken}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(submissionData),
      });

      if (!response.ok) {
        throw new Error('Failed to submit quiz');
      }

      console.log('Quiz submitted successfully');
      setIsSubmitted(true);
      navigate(`/student/home?token=${encodeURIComponent(accessToken)}`);
    } catch (error) {
      console.error('Error submitting quiz:', error);
    }
  };

  const handleCopyPaste = (e) => {
    e.preventDefault();
    setIsCopyPasteModalOpen(true);
  };

  const handleVisibilityChange = () => {
    if (document.hidden) {
      setIsTabModalOpen(true);
    }
  };

  const handleCloseModal = () => {
    setIsCopyPasteModalOpen(false);
    setIsTabModalOpen(false);
  };

  useEffect(() => {
    document.addEventListener('visibilitychange', handleVisibilityChange);

    return () => {
      document.removeEventListener('visibilitychange', handleVisibilityChange);
    };
  }, []);

  return (
    <Container>
      <Box
        display="flex"
        flexDirection="column"
        alignItems="center"
        minHeight="100vh"
        padding={3}
      >
        {quiz ? (
          <>
            <Typography variant="h4" gutterBottom>
              Quiz ID: {quizId}
            </Typography>

            {quiz.length > 0 ? (
              quiz.map((q, index) => (
                <Card
                  key={index}
                  sx={{
                    backgroundColor: '#e3f2fd',
                    borderRadius: 2,
                    boxShadow: 3,
                    marginBottom: 2,
                    width: '100%',
                    maxWidth: 600
                  }}
                >
                  <CardContent>
                    <Typography variant="h6" gutterBottom>
                      {q.text}
                    </Typography>
                    {q.type === 'MULTIPLE_CHOICE' ? (
                      <Box>
                        {q.options.map((option, idx) => (
                          <Box key={idx} display="flex" alignItems="center">
                            <input
                              type="radio"
                              name={`question-${index}`}
                              value={option}
                              onChange={() => handleAnswerChange(index, option)}
                            />
                            <Typography variant="body1" sx={{ marginLeft: 1 }}>
                              {option}
                            </Typography>
                          </Box>
                        ))}
                      </Box>
                    ) : (
                      <TextField
                        fullWidth
                        multiline
                        rows={4}
                        placeholder="Enter your answer here..."
                        variant="outlined"
                        onPaste={handleCopyPaste}
                        onCopy={handleCopyPaste}
                        onChange={(e) => handleAnswerChange(index, e.target.value)}
                      />
                    )}
                  </CardContent>
                </Card>
              ))
            ) : (
              <Typography variant="h6">No questions available for this quiz.</Typography>
            )}

            <Button
              variant="contained"
              color="primary"
              sx={{ marginTop: 2 }}
              onClick={handleSubmit}
            >
              Submit
            </Button>

            {isSubmitted && (
              <Snackbar open autoHideDuration={6000}>
                <Alert severity="success" sx={{ width: '100%' }}>
                  Quiz successfully submitted!
                </Alert>
              </Snackbar>
            )}
          </>
        ) : (
          <Typography variant="h6">Loading...</Typography>
        )}

        {/* Copy-Paste Disabled Modal */}
        <Dialog
          open={isCopyPasteModalOpen}
          onClose={handleCloseModal}
          aria-labelledby="copy-paste-dialog-title"
          aria-describedby="copy-paste-dialog-description"
        >
          <DialogTitle id="copy-paste-dialog-title">
            <Typography variant="h6">Action Not Allowed</Typography>
          </DialogTitle>
          <DialogContent>
            <Typography id="copy-paste-dialog-description">
              Copy and paste actions are disabled for this input field. Please enter your answer manually.
            </Typography>
          </DialogContent>
          <DialogActions>
            <IconButton edge="end" color="inherit" onClick={handleCloseModal}>
              <CloseIcon />
            </IconButton>
          </DialogActions>
        </Dialog>

        {/* Tab Switching Modal */}
        <Dialog
          open={isTabModalOpen}
          onClose={handleCloseModal}
          aria-labelledby="tab-switch-dialog-title"
          aria-describedby="tab-switch-dialog-description"
        >
          <DialogTitle id="tab-switch-dialog-title">
            <Typography variant="h6">Warning</Typography>
          </DialogTitle>
          <DialogContent>
            <Typography id="tab-switch-dialog-description">
              Switching tabs is not allowed while completing the quiz. Your activity will be reported.
            </Typography>
          </DialogContent>
          <DialogActions>
            <IconButton edge="end" color="inherit" onClick={handleCloseModal}>
              <CloseIcon />
            </IconButton>
          </DialogActions>
        </Dialog>
      </Box>
    </Container>
  );
};

export default QuizDetails;
