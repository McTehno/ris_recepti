import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom"; // Dodan useNavigate za preusmeritev, če ni prijave
import api from "../services/api";

function MojiRecepti() {
  const [recepti, setRecepti] = useState([]);
  const [search, setSearch] = useState("");
  const navigate = useNavigate();

  // Pridobimo ID uporabnika iz sessionStorage
  const userId = sessionStorage.getItem("userId");

  // pridobivanje receptov uporabnika
  useEffect(() => {
    // Če uporabnik ni prijavljen, ga lahko preusmerimo na login ali prikažemo sporočilo
    if (!userId) {
        console.warn("Uporabnik ni prijavljen!");
        navigate("/login"); // Preusmeritev na login
        return;
    }

    api.get(`/recepti/uporabnik/${userId}`) // Uporabimo dinamičen userId
      .then(res => setRecepti(res.data))
      .catch(err => console.error("Napaka pri pridobivanju receptov:", err));
  }, [userId, navigate]); // userId in navigate dodamo v dependency array


  const handleIzbrisi = (id) => {
    if (window.confirm("Ali ste prepričani da želite izbrisati recept?")) {
      api.delete(`/recepti/${id}`)
        .then(() => setRecepti(recepti.filter(r => r.id !== id)))
        .catch(err => console.error(err));
    }
  };

  const handleSearch = () => {
     if (!userId) return;

     if(search === "") {
        api.get(`/recepti/uporabnik/${userId}`)
          .then(response => setRecepti(response.data))
          .catch(err => console.error(err));
      } else {
        api.get(`/recepti/uporabnik/${userId}/ime/${search}`)
          .then(response => setRecepti(response.data))
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
        <button onClick={handleSearch}>Išči</button> 
      </div>

    <div className="moji-recepti-container">
      <h2>Moji recepti</h2>
      {recepti.length === 0 ? (
        <p>Ni receptov za prikaz...</p>
      ) : (
        <div className="cards-container">
          {recepti.map((recept) => (
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
            ))}
        </div>
      )}
    </div>

  </div>
  );
}

export default MojiRecepti;