import React, { useState } from 'react';
import Cookies from 'js-cookie';
import {
  Box,
  Button,
  TextField,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  MenuItem,
  Typography,
  Paper,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import { v4 as uuidv4 } from 'uuid';

const CreateQuiz = () => {
  const [questions, setQuestions] = useState([]);
  const [open, setOpen] = useState(false);
  const [questionType, setQuestionType] = useState('');
  const [currentQuestion, setCurrentQuestion] = useState({
    text: '',
    options: [],
    correctAnswer: '',
    predefinedAnswer: '',
  });

  const handleClickOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const handleAddQuestion = () => {
    setQuestions([...questions, { ...currentQuestion, id: uuidv4() }]);
    setCurrentQuestion({ text: '', options: [], correctAnswer: '', predefinedAnswer: '' });
    handleClose();
  };

  const handleCompleteQuiz = async () => {
    const quizData = {
      quizNumber: uuidv4(), // Generate a unique quiz number
      questions: questions.map((question) => ({
        text: question.text,
        type: questionType === 'multiple-choice' ? 'MULTIPLE_CHOICE' : 'DESCRIPTIVE',
        options: questionType === 'multiple-choice' ? question.options : [],
        answer: questionType === 'multiple-choice' ? question.correctAnswer : question.predefinedAnswer,
      })),
    };

    console.log('Quiz Data:', quizData);

    const accessToken = Cookies.get('access_token');
    console.log('access token:', accessToken)
    if (accessToken) {
      try {
        const response = await fetch('http://localhost:8080/api/v1/quizzes', {
          method: 'POST',
          headers: {
            Authorization: `Bearer ${accessToken}`,
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(quizData),
        });

        if (response.ok) {
          console.log('Quiz successfully submitted');
          setQuestions([]);
        } else {
          console.error('Failed to submit quiz', response.statusText);
        }
      } catch (error) {
        console.error('Error during quiz submission:', error);
      }
    } else {
      console.error('No access token found');
    }
  };

  return (
    <Box p={3}>
      <Typography variant="h4" gutterBottom>
        Create Quiz
      </Typography>

      <Box mb={4}>
        <Button variant="contained" startIcon={<AddIcon />} onClick={handleClickOpen}>
          Add Question
        </Button>
        <Button
          variant="contained"
          color="primary"
          onClick={handleCompleteQuiz}
          style={{ marginLeft: '10px' }}
        >
          Complete Quiz
        </Button>
      </Box>

      {questions.length > 0 && (
        <Box>
          <Typography variant="h5" gutterBottom>
            Questions
          </Typography>
          {questions.map((question, index) => (
            <Paper key={index} elevation={3} sx={{ padding: 2, marginBottom: 2 }}>
              <Typography variant="h6">{`Q${index + 1}: ${question.text}`}</Typography>
              {question.options.length > 0 && (
                <Box mt={2}>
                  <Typography variant="body1" fontWeight="bold">
                    Options:
                  </Typography>
                  <ul>
                    {question.options.map((option, idx) => (
                      <li key={idx}>{option}</li>
                    ))}
                  </ul>
                </Box>
              )}
              {question.predefinedAnswer && (
                <Box mt={2}>
                  <Typography variant="body1" fontWeight="bold">
                    Predefined Answer:
                  </Typography>
                  <Typography variant="body2">{question.predefinedAnswer}</Typography>
                </Box>
              )}
            </Paper>
          ))}
        </Box>
      )}

      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Select Question Type</DialogTitle>
        <DialogContent>
          <TextField
            select
            label="Question Type"
            value={questionType}
            onChange={(e) => setQuestionType(e.target.value)}
            fullWidth
            margin="normal"
          >
            <MenuItem value="multiple-choice">Multiple Choice</MenuItem>
            <MenuItem value="descriptive">Descriptive</MenuItem>
          </TextField>

          <TextField
            label="Question Text"
            value={currentQuestion.text}
            onChange={(e) => setCurrentQuestion({ ...currentQuestion, text: e.target.value })}
            fullWidth
            margin="normal"
          />

          {questionType === 'multiple-choice' && (
            <>
              {currentQuestion.options.map((option, idx) => (
                <Box key={idx} display="flex" alignItems="center" mb={1}>
                  <TextField
                    value={option}
                    onChange={(e) => {
                      const newOptions = [...currentQuestion.options];
                      newOptions[idx] = e.target.value;
                      setCurrentQuestion({ ...currentQuestion, options: newOptions });
                    }}
                    fullWidth
                    margin="normal"
                  />
                  <Button
                    onClick={() => {
                      const newOptions = currentQuestion.options.filter((_, index) => index !== idx);
                      setCurrentQuestion({ ...currentQuestion, options: newOptions });
                    }}
                    color="error"
                    sx={{ marginLeft: 1 }}
                  >
                    <DeleteIcon />
                  </Button>
                </Box>
              ))}
              <Button
                onClick={() => setCurrentQuestion({ ...currentQuestion, options: [...currentQuestion.options, ''] })}
                startIcon={<AddIcon />}
              >
                Add Option
              </Button>

              <TextField
                select
                label="Correct Answer"
                value={currentQuestion.correctAnswer}
                onChange={(e) => setCurrentQuestion({ ...currentQuestion, correctAnswer: e.target.value })}
                fullWidth
                margin="normal"
              >
                {currentQuestion.options.map((option, idx) => (
                  <MenuItem key={idx} value={option}>
                    {option}
                  </MenuItem>
                ))}
              </TextField>
            </>
          )}

          {questionType === 'descriptive' && (
            <TextField
              label="Predefined Answer"
              value={currentQuestion.predefinedAnswer}
              onChange={(e) => setCurrentQuestion({ ...currentQuestion, predefinedAnswer: e.target.value })}
              fullWidth
              margin="normal"
            />
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="secondary">
            Cancel
          </Button>
          <Button onClick={handleAddQuestion} color="primary">
            Add Question
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default CreateQuiz;
