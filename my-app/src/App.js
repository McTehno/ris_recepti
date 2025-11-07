import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import './App.css';
import Domov from './pages/Domov';
import VsiRecepti from './pages/VsiRecepti';
import MojiRecepti from './pages/MojiRecepti';
import ReceptPodrobnosti from './pages/ReceptPodrobnosti';
import ReceptForm from './pages/ReceptForm';

function App() {
  return (
    <Router>
      <header>
        <h1>Moji Recepti</h1>
        <nav>
          <Link to="/">Domov</Link> | 
          <Link to="/vsi-recepti">Vsi recepti</Link> | 
          <Link to="/moji-recepti">Moji recepti</Link> | 
          <Link to="/nov-recept">Ustvari nov recept</Link>
          
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
        </Routes>

      </main>

      <footer>
        &copy; 2025 Moji Recepti
      </footer>
    </Router>
  );
}

export default App;