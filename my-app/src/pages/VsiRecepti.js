import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../services/api"; 


function VsiRecepti() {
  const [recepti, setRecepti] = useState([]);

  const [search, setSearch] = useState("");


  useEffect(() => {
    api.get("http://localhost:8180/api/recepti")
      .then(response => setRecepti(response.data))
      .catch(error => console.error(error));
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
        .catch(err => console.error(err));
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
