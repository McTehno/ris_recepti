import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import { 
  Container, 
  TextField, 
  Button, 
  Typography, 
  Box, 
  Alert, 
  Dialog, 
  DialogActions, 
  DialogContent, 
  DialogContentText, 
  DialogTitle 
} from '@mui/material';

function Profil() {
  const navigate = useNavigate();
  const [podatki, setPodatki] = useState({
    ime: '',
    priimek: '',
    enaslov: '',
    geslo: ''
  });
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState({ type: '', text: '' });
  const [openDialog, setOpenDialog] = useState(false);

  const userId = sessionStorage.getItem('userId');

  useEffect(() => {
    if (!userId) {
      navigate('/login');
      return;
    }
    fetchUserData();
  }, [userId, navigate]);

  const fetchUserData = async () => {
    try {
      const response = await api.get(`/uporabniki/${userId}`);
      setPodatki(response.data);
      setLoading(false);
    } catch (err) {
      console.error("Napaka pri pridobivanju podatkov:", err);
      setMessage({ type: 'error', text: 'Napaka pri nalaganju podatkov profila.' });
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPodatki({ ...podatki, [name]: value });
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    setMessage({ type: '', text: '' });
    
    // enako kot pri ReceptForm.js, izognemo se infinite loop-u 
    const uporabnikZaPosiljanje = {
      ime: podatki.ime,
      priimek: podatki.priimek,
      enaslov: podatki.enaslov,
      geslo: podatki.geslo
    };

    try {
      await api.put(`/uporabniki/${userId}`, uporabnikZaPosiljanje);
      setMessage({ type: 'success', text: 'Podatki uspešno posodobljeni!' });
    } catch (err) {
      console.error("Napaka pri posodabljanju:", err);
      if (err.response && err.response.data) {
         console.log("Server error data:", err.response.data);
      }
      setMessage({ type: 'error', text: 'Napaka pri posodabljanju podatkov.' });
    }
  };

  const handleLogout = () => {
    sessionStorage.removeItem('userId');
    navigate('/login');
  };

  const handleDeleteAccount = async () => {
    try {
      await api.delete(`/uporabniki/${userId}`);
      sessionStorage.removeItem('userId');
      navigate('/login');
    } catch (err) {
      console.error("Napaka pri brisanju računa:", err);
      setMessage({ type: 'error', text: 'Napaka pri brisanju računa.' });
      setOpenDialog(false);
    }
  };

  if (loading) return <div className="loading-text">Nalaganje...</div>;

  return (
    <Container maxWidth="md">
      {/* Uporabljamo isti stilski razred kot za recepte za konsistentnost, plus specifičnega za profil */}
      <div className="recept-form profil-container"> 
        <Typography component="h2" variant="h4" className="profil-title">
          Moj Profil
        </Typography>

        {message.text && (
          <Alert severity={message.type} className="profil-alert">
            {message.text}
          </Alert>
        )}

        <Box component="form" onSubmit={handleUpdate} className="profil-form-box">
          <TextField
            label="Ime"
            name="ime"
            value={podatki.ime || ''}
            onChange={handleChange}
            fullWidth
            margin="normal"
            variant="outlined"
            className="custom-textfield"
          />
          
          <TextField
            label="Priimek"
            name="priimek"
            value={podatki.priimek || ''}
            onChange={handleChange}
            fullWidth
            margin="normal"
            variant="outlined"
            className="custom-textfield"
          />

          <TextField
            label="E-naslov"
            name="enaslov"
            value={podatki.enaslov || ''}
            onChange={handleChange}
            fullWidth
            required
            margin="normal"
            variant="outlined"
            className="custom-textfield"
          />

          <TextField
            label="Geslo"
            name="geslo"
            type="password"
            value={podatki.geslo || ''}
            onChange={handleChange}
            fullWidth
            required
            margin="normal"
            helperText="Vnesite novo geslo ali pustite trenutnega"
            variant="outlined"
            className="custom-textfield"
          />

          {/* Gumbi */}
          <div className="profil-actions">
            <Button 
              type="submit" 
              variant="contained" 
              size="large"
              className="btn-save"
            >
              Shrani spremembe
            </Button>

            <div className="profil-secondary-actions">
                <Button 
                variant="contained" 
                className="btn-logout" 
                onClick={handleLogout}
                >
                Odjava
                </Button>

                <Button 
                variant="outlined" 
                className="btn-delete-account"
                onClick={() => setOpenDialog(true)}
                >
                Izbriši račun
                </Button>
            </div>
          </div>
        </Box>
      </div>

      {/* Dialog za izbris - uporablja privzete MUI stile, ker je modalno okno */}
      <Dialog
        open={openDialog}
        onClose={() => setOpenDialog(false)}
      >
        <DialogTitle>Izbris računa</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Ali ste prepričani, da želite trajno izbrisati svoj račun? Tega dejanja ni mogoče razveljaviti.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)} color="primary">
            Prekliči
          </Button>
          <Button onClick={handleDeleteAccount} color="error" autoFocus>
            Izbriši
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}

export default Profil;