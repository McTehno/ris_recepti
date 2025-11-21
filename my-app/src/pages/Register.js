import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Container, TextField, Button, Typography, Box, Alert } from '@mui/material';

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
    
    // Simulacija registracije (kasneje povežemo z API)
    console.log("Podatki za registracijo:", podatki);
    
    if (podatki.ime && podatki.priimek && podatki.enaslov && podatki.geslo) {
        // Tukaj bi bil klic: await api.post('/uporabniki/post', podatki);
        navigate('/login'); 
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