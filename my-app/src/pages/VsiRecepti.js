import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../services/api"; 
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import { IconButton } from '@mui/material';


function VsiRecepti() {
  const [recepti, setRecepti] = useState([]);

  const [search, setSearch] = useState("");
  const [likedRecipes, setLikedRecipes] = useState(new Set());
  const currentUserId = sessionStorage.getItem('userId');

  const fetchLikedRecipes = async () => {
      if (!currentUserId) return;
      try {
          const response = await api.get(`/uporabniki/${currentUserId}/liked`);
          // backend uporablja seznam receptov, samo pa rabimo id-je
          if (Array.isArray(response.data)) {
              setLikedRecipes(new Set(response.data.map(r => r.id)));
          }
      } catch (error) {
          console.error("napaka pri fetch-anju receptov:", error);
      }
  };

  const handleLike = async (receptId) => {
      if (!currentUserId) {
          alert("Za všečkanje se morate prijaviti!");
          return;
      }

      const isLiked = likedRecipes.has(receptId);
      
      try {
          if (isLiked) {
              await api.delete(`/uporabniki/${currentUserId}/like/${receptId}`);
              setLikedRecipes(prev => {
                  const newSet = new Set(prev);
                  newSet.delete(receptId);
                  return newSet;
              });
          } else {
              await api.post(`/uporabniki/${currentUserId}/like/${receptId}`);
              setLikedRecipes(prev => new Set(prev).add(receptId));
          }
      } catch (error) {
          console.error("napaka pri toggle-janju like:", error);
      }
  };


  useEffect(() => {
    api.get("http://localhost:8180/api/recepti")
      .then(response => {
        setRecepti(response.data);
        fetchLikedRecipes();
      })
      .catch(error => console.error("napaka pri fetch-anju receptov:", error));
  }, []);

  

  return (
    <div>
      <div className="search-container">
  <input 
    type="text" 
    placeholder="Vnesite ime recepta" 
    value={search} 
    onChange={(e) => setSearch(e.target.value)}
  />
  <button onClick={() => {
    if(search === "") {
      // Ako e prazno, vrati gi site recepti
      api.get("http://localhost:8180/api/recepti")
        .then(response => setRecepti(response.data))
        .catch(err => console.error(err));
    } else {
      // Povikaj endpoint za prebaruvanje po ime
      api.get(`http://localhost:8180/api/recepti/ime/${search}`)
        .then(response => setRecepti(response.data))
        .catch(err => console.error("napaka pri fetch-anju receptov:", err));
    }
  }}>Išči</button>
</div>

    <div>
      <h2>Vsi recepti</h2>
      <div className="cards-container">
  {//ce imamo recepte na voljo
  recepti.length > 0 ? (
    //za vsak recept v recepti da bo ime clcickable, da se prikaze datum,ocena,tip
    // gumb spremeni vodi do stran /recept/id/edit oziroma ReceptForm.js
    //izbrisi  buttonot ja povikuva funkcijata izbrisi
    recepti.map((recept) => (
      <div key={recept.id} className="card">
        <h5 className="recept-link">
          <Link to={`/recept/${recept.id}`}>{recept.ime}</Link> 
        </h5>
        <p><strong>Datum:</strong> {recept.datumUstvarjanja}</p>
        <p><strong>Ocena:</strong> {recept.povprecnaOcena ? recept.povprecnaOcena.toFixed(1) : "Brez ocen"}</p>
        <p><strong>Tip:</strong> {recept.tip}</p>
        <p><strong>Porcij:</strong> {recept.st_porcij}</p>
        
        { (!recept.uporabnik || String(recept.uporabnik.id) !== String(currentUserId)) && (
          //to bo tudi preverilo ce uporabnik gleda svoj recept, da ne more vseckat svojega recepta
             <IconButton 
                onClick={() => handleLike(recept.id)} 
                sx={{ position: 'absolute', bottom: 8, right: 8, color: 'red' }}
             >
                {likedRecipes.has(recept.id) ? <FavoriteIcon /> : <FavoriteBorderIcon />}
             </IconButton>
        )}
      </div>
    ))
  ) : (
    <p style={{textAlign: "center", gridColumn: "1/-1"}}>Loading...</p>
  )}
</div>
    </div>
  </div>
  );
}

export default VsiRecepti;
