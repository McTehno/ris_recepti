import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import { Container, TextField, Button, Typography, Box, Alert } from '@mui/material';

function Login() {
  const [podatki, setPodatki] = useState({
    enaslov: '',
    geslo: ''
  });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPodatki({ ...podatki, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    
    try {
      // Simulacija klica - prilagodite endpoint glede na backend
      // const response = await api.post('/uporabniki/login', podatki);
      console.log("Podatki za prijavo:", podatki);
      
      // Začasna rešitev za testiranje (dokler ni backend login endpointa):
      // Preusmerimo na domov, če sta polji izpolnjeni
      if (podatki.enaslov && podatki.geslo) {
         navigate('/');
      } else {
         setError('Prosim izpolnite vsa polja.');
      }
      
    } catch (err) {
      console.error("Napaka pri prijavi:", err);
      setError('Napaka pri prijavi. Preverite e-naslov in geslo.');
    }
  };

  return (
    <Container maxWidth="sm">
      <div className="login-container">
        <Typography component="h1" variant="h5">
          Prijava
        </Typography>
        
        <Box component="form" onSubmit={handleSubmit} className="login-form-box">
          {error && <Alert severity="error" className="login-alert">{error}</Alert>}
          
          <TextField
            margin="normal"
            required
            fullWidth
            id="enaslov"
            label="E-naslov"
            name="enaslov"
            autoComplete="email"
            autoFocus
            value={podatki.enaslov}
            onChange={handleChange}
          />
          
          <TextField
            margin="normal"
            required
            fullWidth
            name="geslo"
            label="Geslo"
            type="password"
            id="geslo"
            autoComplete="current-password"
            value={podatki.geslo}
            onChange={handleChange}
          />
          
          <Button
            type="submit"
            fullWidth
            variant="contained"
            className="login-submit-btn"
          >
            Prijavi se
          </Button>
        </Box>
      </div>
    </Container>
  );
}

export default Login;