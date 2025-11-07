import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../services/api";

function MojiRecepti() {
  const [recepti, setRecepti] = useState([]);
  const [search, setSearch] = useState("");

  //pridobivanje receptov uporabnika 1
  useEffect(() => {
  api.get("http://localhost:8180/api/recepti/uporabnik/1")
    .then(res => setRecepti(res.data))
    .catch(err => console.error("Napaka pri pridobivanju receptov:", err));
}, []);


  const handleIzbrisi = (id) => {
    if (window.confirm("Ali ste prepričani da želite izbrisati recept?")) {
      api.delete(`/recepti/${id}`)
        .then(() => setRecepti(recepti.filter(r => r.id !== id)))
        .catch(err => console.error(err));
    }
  };

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
    api.get("http://localhost:8180/api/recepti/uporabnik/1")
      .then(response => setRecepti(response.data))
      .catch(err => console.error(err));
  } else {
    api.get(`http://localhost:8180/api/recepti/uporabnik/1/ime/${search}`)
      .then(response => setRecepti(response.data))
      .catch(err => console.error(err));
  }
}}>Išči</button>

</div>
    <div className="moji-recepti-container">
      <h2>Moji recepti</h2>
      {recepti.length === 0 ? (
        <p>Ni receptov za prikaz...</p>
      ) : (
        <div className="cards-container">
  {recepti.length > 0 ? (
    recepti.map((recept) => (
      <div key={recept.id} className="card">
        <h5 className="recept-link">
          <Link to={`/recept/${recept.id}`}>{recept.ime}</Link>
        </h5>
        <p><strong>Datum:</strong> {recept.datumUstvarjanja}</p>
        <p><strong>Tip:</strong> {recept.tip}</p>
        <div>
          <Link to={`/recept/${recept.id}/edit`}>
            <button className="btn-action">Spremeni</button>
          </Link>
          <button className="btn-action delete" onClick={() => handleIzbrisi(recept.id)}>Izbrisi</button>
        </div>
      </div>
    ))
  ) : (
    <p style={{textAlign: "center", gridColumn: "1/-1"}}>Loading...</p>
  )}
</div>
      )}
    </div>

  </div>
  );
}

export default MojiRecepti;
