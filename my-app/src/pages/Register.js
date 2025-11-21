import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Container, TextField, Button, Typography, Box, Alert } from '@mui/material';
import api from '../services/api';

function Register() {
  const [podatki, setPodatki] = useState({
    ime: '',
    priimek: '',
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
    
    if (podatki.ime && podatki.priimek && podatki.enaslov && podatki.geslo) {
        try {
            // Klic na backend za registracijo
            await api.post('/uporabniki/post', podatki);
            // Uspesna registracija pa preusmeritev na login
            navigate('/login'); 
        } catch (err) {
            console.error("Napaka pri registraciji:", err);
            setError('Napaka pri registraciji. Poskusite znova.');
        }
    } else {
        setError('Prosim izpolnite vsa polja.');
    }
  
  };

  return (
    <Container maxWidth="sm">
      <div className="login-container">
        <Typography component="h1" variant="h5">
          Registracija
        </Typography>
        
        <Box component="form" onSubmit={handleSubmit} className="login-form-box">
          {error && <Alert severity="error" className="login-alert">{error}</Alert>}
          
          <TextField
            margin="normal"
            required
            fullWidth
            id="ime"
            label="Ime"
            name="ime"
            autoFocus
            value={podatki.ime}
            onChange={handleChange}
          />

          <TextField
            margin="normal"
            required
            fullWidth
            id="priimek"
            label="Priimek"
            name="priimek"
            value={podatki.priimek}
            onChange={handleChange}
          />

          <TextField
            margin="normal"
            required
            fullWidth
            id="enaslov"
            label="E-naslov"
            name="enaslov"
            autoComplete="email"
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
            autoComplete="new-password"
            value={podatki.geslo}
            onChange={handleChange}
          />
          
          <Button
            type="submit"
            fullWidth
            variant="contained"
            className="login-submit-btn"
          >
            Ustvari račun
          </Button>

          <div className="auth-switch-container">
            <span>Že imate račun?</span>
            <Link to="/login" className="auth-link">
              Prijavite se
            </Link>
          </div>
        </Box>
      </div>
    </Container>
  );
}

export default Register;