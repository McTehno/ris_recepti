import React, { useState, useEffect } from 'react';
import api from '../services/api';

function Komentarji({ receptId }) {
  const [komentarji, setKomentarji] = useState([]);
  const [novKomentar, setNovKomentar] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  const uporabnikId= sessionStorage.getItem('userId'); 
  //const jeAdmin = sessionStorage.getItem('isAdmin') === 'true'; 

  useEffect(() => {
    if (!receptId) return;
    
    api.get(`/recept/${receptId}/komentarji`)
      .then(response => {
        setKomentarji(response.data);
        setLoading(false);
      })
      .catch(err => {
        console.error("Napaka pri pridobivanju komentarjev:", err);
        setLoading(false);
      });
  }, [receptId]);

  const handleSubmit = (e) => {
    e.preventDefault();
    setError("");
    if (!novKomentar.trim()) return;

    if (!uporabnikId) {
      setError("Za oddajo komentarja morate biti prijavljeni.");
      return;
    }

    api.post(`/recept/${receptId}/ustvariKom`, { uporabnikId: parseInt(uporabnikId),vsebina: novKomentar })
      .then(response => {
        setKomentarji([...komentarji, response.data]);
        setNovKomentar(""); 
      })
      .catch(err => { console.error("Napaka pri oddaji komentarja:", err); setError("Napaka pri oddaji komentarja."); });
  };

  if (loading) return <p>Nalagam komentarje...</p>;

  return (
    <div className="komentarji-container">
      <h4>Komentarji</h4>
      <form onSubmit={handleSubmit} className="komentar-form">
        <textarea value={novKomentar} onChange={(e) => setNovKomentar(e.target.value)} placeholder="Napiši komentar..." rows="3" required />
        <button className='komentar-button' type="submit">Objavi</button>
        {error && <p style={{ color: 'red', marginTop: '10px' }}>{error}</p>}
      </form>
      <br/>
      <div className="komentarji-list">
        {komentarji.length > 0 ? (
          komentarji.map(komentar => (
            <div key={komentar.id} className="komentar">
              <p><strong>{komentar.ime ? komentar.ime : 'Neznan uporabnik'}:</strong> {komentar.vsebina}</p>
            </div>
          ))
        ) : (
          <p>Bodi prvi in komentiraj!</p>
        )}
      </div>
    </div>
  );
}

export default Komentarji;