import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
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
      if (podatki.enaslov && podatki.geslo) {
          // Klic na backend za prijavo
          const response = await api.post('/uporabniki/login', podatki);
          
          console.log("Uspešna prijava:", response.data);

          // Shranimo ID uporabnika v sessionStorage
          // response.data je objekt Uporabnik, ki smo ga vrnili iz controllerja
          sessionStorage.setItem('userId', response.data.id);
          
          // Preusmerimo na domov
          navigate('/');
      } else {
         setError('Prosim izpolnite vsa polja.');
      }
      
    } catch (err) {
      console.error("Napaka pri prijavi:", err);
      // Preverimo, če je backend vrnil specifično napako
      if (err.response && err.response.status === 401) {
          setError('Napačno geslo.');
      } else if (err.response && err.response.status === 404) {
          setError('Uporabnik s tem e-naslovom ne obstaja.');
      } else {
          setError('Napaka pri prijavi. Preverite e-naslov in geslo.');
      }
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

          <div className="auth-switch-container">
            <span>Še nimate računa?</span>
            <Link to="/register" className="auth-link">
              Registrirajte se
            </Link>
          </div>

          
        </Box>
      </div>
    </Container>
  );
}

export default Login;