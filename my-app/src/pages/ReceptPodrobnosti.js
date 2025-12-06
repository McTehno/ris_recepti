import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

import api from "../services/api";
import Komentarji from "./Komentarji";
import IzvozButton from "./IzvozButton"; 

function ReceptPodrobnosti() {
  const { id } = useParams();// id recepta
  const [recept, setRecept] = useState(null);
  const [sestavine, setSestavine] = useState([]);
  //--- ZA OCENEVANJE ---
  const userId = sessionStorage.getItem('userId'); // dobimo id trenutnega uporabnika
  const [mojaOcena, setMojaOcena] = useState(0); // izbrana ocena (0 j brez ocene)
  const [hover, setHover] = useState(0);         // za hover efekt
  const [sporocilo, setSporocilo] = useState("");
  const [tipObvestila, setTipObvestila] = useState("success"); // 'success' ali 'error'
  const [jeAvtor, setJeAvtor] = useState(false); // Ali je trenutni uporabnik avtor?

  useEffect(() => {
    fetchRecept();
    // Povik za sastojkite
    api.get(`/sestavine/recept/${id}`)
      .then(response => setSestavine(response.data))
      .catch(err => console.error("Greska pri dobivanje na sostojki:", err));
  }, [id]);

  // ko dobimo recept in imamo userId, preverimo dodatne pogoje (avtor ali ocena)
  useEffect(() => {
    if (recept && userId) {
        if (recept.uporabnik && recept.uporabnik.id === parseInt(userId)) {
            setJeAvtor(true);
        } else {
          preveriMojoOceno();
        }
    }
  }, [recept, userId]);

  const fetchRecept = () => {
    api.get(`/recepti/${id}`)
      .then(res => setRecept(res.data))
      .catch(err => console.error(err));
  };

  // preverjanje ce je uporabnik ze ocenil ta recept
  const preveriMojoOceno = () => {
      api.get(`/ocene/recept/${id}/uporabnik/${userId}`)
        .then(res => {
            if (res.data > 0) {
                setMojaOcena(res.data); // nastavi zvezdice na obstoječo oceno
            }
        })
        .catch(err => console.error("Napaka pri preverjanju ocene", err));
  };

  // funkcija za prikaz toasta
  const prikaziObvestilo = (tekst, tip = "success") => {
    setSporocilo(tekst);
    setTipObvestila(tip);
    // samodejno skrij po 3 sekundah
    setTimeout(() => {
      setSporocilo("");
    }, 3000);
  };

  const oddajOceno = async () => {
    if (!userId) {
      setSporocilo("Za ocenjevanje morate biti prijavljeni.");
      return;
    }
    if (mojaOcena === 0) {
      setSporocilo("Prosim, izberite število zvezdic.");
      return;
    }
    if (jeAvtor) {
        setSporocilo("Svojega recepta ne morete oceniti.");
        return;
    }

    const zahtevaZaOceno = {
      vrednost: mojaOcena,
      uporabnikId: parseInt(userId) 
    };

    try {
      await api.post(`/ocene/recept/${id}`, zahtevaZaOceno);
      prikaziObvestilo("Hvala za vašo oceno!"); // Uspeh
      fetchRecept(); //refresh povprečne ocene
    } catch (error) {
      console.error("Napaka pri oddaji ocene:", error);
      if (error.response && error.response.data) {
          console.log("Backend error:", error.response.data);// Prikaz specifične napake iz backend-a i love
      }
      prikaziObvestilo("Napaka pri oddaji ocene.", "error");
    }
  };

  if (!recept) return <p className="loading-text">Nalaganje recepta...</p>;

  return (
    <div className="recept-podrobnosti">

      {/* TOAST OBVESTILO, se prikaze samo ce obstaja 'sporocilo' */}
      {sporocilo && (
        <div className={`toast-notification ${tipObvestila === "error" ? "error" : ""}`}>
          {sporocilo}
        </div>
      )}


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
      )}{!jeAvtor && (
      <div className="rating-wrapper">
          <div className="rating-stars">
            {[...Array(5)].map((star, index) => {
              const ratingValue = index + 1;
              return (
                <label key={index}>
                  <input 
                    type="radio" 
                    name="rating" 
                    value={ratingValue} 
                    onClick={() => setMojaOcena(ratingValue)}
                    style={{ display: 'none' }}
                  />
                  <span 
                    className="star" 
                    style={{ 
                      color: ratingValue <= (hover || mojaOcena) ? "#ffc107" : "#e4e5e9"
                    }}
                    onMouseEnter={() => setHover(ratingValue)}
                    onMouseLeave={() => setHover(0)}
                  >
                    &#9733;
                  </span>
                </label>
              );
            })}
          </div>
          
          <button className="btn-rate-submit" onClick={oddajOceno}>
            Oddaj oceno
          </button>
        </div>
      )}

      <hr style={{ margin: '30px 0' }} />
      <Komentarji receptId={id} />


      <IzvozButton recept={recept} sestavine={sestavine} />
    </div>
  );
}

export default ReceptPodrobnosti;
