import React, { useState } from 'react';
import { Container, Box, Paper, Tab, Tabs, Typography, TextField, Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Cookies from 'js-cookie';

function App() {
  const [role, setRole] = useState('student'); // Default role is student
  const [formData, setFormData] = useState({
    id: '',
    password: ''
  });
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleRoleChange = (event, newRole) => {
    setRole(newRole);
    setFormData({ id: '', password: '' }); // Reset form data when changing role
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleLogin = async () => {
    setError(null);
    const apiEndpoint = 'http://localhost:8080/api/v1/auth/authenticate';

    try {
      const response = await axios.post(apiEndpoint, {
        email: formData.id,
        password: formData.password
      }, {
        headers: {
          'Content-Type': 'application/json'
        }
      });

      const { access_token, refresh_token, success, message } = response.data;

      if (success) {
        Cookies.set('access_token', access_token, { secure: true, sameSite: 'Strict' });
        Cookies.set('refresh_token', refresh_token, { secure: true, sameSite: 'Strict' });

        if (role === 'student') {
          navigate(`/student/home?token=${encodeURIComponent(access_token)}`);
        } else if (role === 'faculty') {
          navigate(`/faculty/home?token=${encodeURIComponent(access_token)}`);
        }
      } else {
        setError(message || 'Invalid ID or password.');
      }
    } catch (error) {
      if (error.response && error.response.status === 404) {
        setError(error.response.data.message || 'Invalid ID or password.');
      } else {
        setError('An error occurred during login. Please try again.');
        console.error(error.message);
      }
    }
  };

  return (
    <Container maxWidth="sm">
      <Paper elevation={3} style={{ padding: '20px', marginTop: '50px' }}>
        <Typography variant="h4" align="center" gutterBottom>
          Login
        </Typography>

        <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
          <Tabs value={role} onChange={handleRoleChange} centered>
            <Tab label="Student Login" value="student" />
            <Tab label="Faculty Login" value="faculty" />
          </Tabs>
        </Box>

        <Box mt={3}>
          <TextField
            fullWidth
            label={role === 'student' ? 'Student ID' : 'Faculty ID'}
            name="id"
            value={formData.id}
            onChange={handleChange}
            variant="outlined"
            margin="normal"
          />
          <TextField
            fullWidth
            label="Password"
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            variant="outlined"
            margin="normal"
          />

          {error && (
            <Typography color="error" variant="body2" style={{ marginTop: '10px' }}>
              {error}
            </Typography>
          )}

          <Button
            variant="contained"
            color="primary"
            fullWidth
            onClick={handleLogin}
            style={{ marginTop: '20px' }}
          >
            {role === 'student' ? 'Login as Student' : 'Login as Faculty'}
          </Button>
        </Box>
      </Paper>
    </Container>
  );
}

export default App;
