import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

import api from "../services/api"; 
import IzvozButton from "./IzvozButton"; 


function ReceptPodrobnosti() {
  const { id } = useParams();
  const [recept, setRecept] = useState(null);
  const [sestavine, setSestavine] = useState([]);

  useEffect(() => {
    // Povik za detalite na receptot
    api.get(`http://localhost:8180/api/recepti/${id}`)
      .then(response => setRecept(response.data))
      .catch(err => console.error("Greska pri dobivanje na recept:", err));

    // Povik za sostojkite
    api.get(`http://localhost:8180/api/sestavine/recept/${id}`)
      .then(response => setSestavine(response.data))
      .catch(err => console.error("Greska pri dobivanje na sostojki:", err));
  }, [id]);

  if (!recept) return <p>Loading...</p>;

  return (
    <div className="recept-podrobnosti">
      <h2>{recept.ime}</h2>
      <p><strong>Tip:</strong> {recept.tip}</p>
      <p><strong>Ocena:</strong> {recept.povprecnaOcena ? recept.povprecnaOcena.toFixed(1) : "Brez ocen"}</p>
      <p><strong>Priprava:</strong> {recept.priprava}</p>
      <p><strong>Uporabnik:</strong>{recept.uporabnik ? `${recept.uporabnik.ime} ${recept.uporabnik.priimek}` : "Neznan avtor"}</p>

      <h3>Sestavine</h3>
      {sestavine.length > 0 ? (
        <ul>
          {sestavine.map(s => (
            <li key={s.id}>{s.ime}</li>
          ))}
        </ul>
      ) : (
        <p>Nima sestavine za recept: {recept.ime}.</p>
      )}

      <IzvozButton recept={recept} sestavine={sestavine} />
    </div>
  );
}

export default ReceptPodrobnosti;
