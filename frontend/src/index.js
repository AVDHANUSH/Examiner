import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import App from './App';
import StudentHome from './pages/StudentHome';
import FacultyHome from './pages/FacultyHome';
import CreateQuiz from './pages/CreateQuiz'; // New Page
import Results from './pages/Results'; // New Page
import Attendance from './pages/Attendance'; // New Page
import PastQuizzes from './pages/PastQuizzes'; // New Page
import TakeQuiz from './pages/TakeQuiz';
import QuizDetails from './pages/QuizDetails';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <Router>
    <Routes>
      <Route path="/" element={<App />} />
      <Route path="/student/home" element={<StudentHome />} />
      <Route path="/student/take-quiz" element={<TakeQuiz />} />
      <Route path="/student/take-quiz/quiz-details/:quizId" element={<QuizDetails />} />
      <Route path="/faculty/home" element={<FacultyHome />} />
      <Route path="/faculty/create-quiz" element={<CreateQuiz />} />
      <Route path="/faculty/results" element={<Results />} />
       <Route path="/student/results" element={<Results />} />
      <Route path="/faculty/attendance" element={<Attendance />} />
      <Route path="/faculty/past-quizzes" element={<PastQuizzes />} />
    </Routes>
  </Router>
);
