import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import './App.css';
import Domov from './pages/Domov';
import VsiRecepti from './pages/VsiRecepti';
import MojiRecepti from './pages/MojiRecepti';
import ReceptPodrobnosti from './pages/ReceptPodrobnosti';
import ReceptForm from './pages/ReceptForm';
import Login from './pages/Login';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import { IconButton } from '@mui/material';

function App() {
  return (
    <Router>
      <header>
        <h1>Moji Recepti</h1>
        <nav>
          <Link to="/">Domov</Link> | 
          <Link to="/vsi-recepti">Vsi recepti</Link> | 
          <Link to="/moji-recepti">Moji recepti</Link> | 
          <Link to="/nov-recept">Ustvari nov recept</Link> | 
          <Link to="/login" className="nav-user-icon-link">
            <IconButton color="inherit" aria-label="login" size="small">
              <AccountCircleIcon fontSize="large" />
            </IconButton>
          </Link>
        </nav>
      </header>

      <main>
        <Routes>
          <Route path="/" element={<Domov />} />
          <Route path="/vsi-recepti" element={<VsiRecepti />} />
          <Route path="/moji-recepti" element={<MojiRecepti />} />
          <Route path="/nov-recept" element={<ReceptForm />} />   {/*kreiranje recept */}
          <Route path="/recept/:id/edit" element={<ReceptForm />} /> {/* editiranje recept */}
          <Route path="/recept/:id" element={<ReceptPodrobnosti />} />
          <Route path="/login" element={<Login />} />
        </Routes>

      </main>

      <footer>
        &copy; 2025 Moji Recepti
      </footer>
    </Router>
  );
}

export default App;