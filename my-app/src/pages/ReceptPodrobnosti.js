import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

import api from "../services/api";
import Komentarji from "./Komentarji";
import IzvozButton from "./IzvozButton"; 

function ReceptPodrobnosti() {
  const { id } = useParams();// id recepta
  const [recept, setRecept] = useState(null);
  const [sestavine, setSestavine] = useState([]);
  const [hranilneVrednosti, setHranilneVrednosti] = useState(null);

  //STEVILO PORCIJE
  const [porcije, setPorcije] = useState(recept?.st_porcij || 1);

  //Originalne sestavine iz zacetka
  const [sestavineOriginal, setSestavineOriginal] = useState([]);

  //Originalne hranilneVrednosti 
  const [hranilneVrednostiOriginal, setHranilneVOriginal] = useState(null);

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
      .then(response => {
        setSestavine(response.data); 
        setSestavineOriginal(response.data);
      })
      .catch(err => console.error("Greska pri dobivanje na sostojki:", err));
    
    // za hranilne vrednosti glede na id recepta
    api.get(`/hranilne-vrednosti/recept/${id}`)
      .then(response => {
        setHranilneVrednosti(response.data);
        setHranilneVOriginal(response.data);
      })
      .catch(err => console.error("Error fetching nutritional values: ", err));

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

    useEffect(() => {
      if (recept) {
        setPorcije(recept.st_porcij);
      }
    }, [recept]);


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

  //Handler za spreminjanje input polja za st porcije

const handlePorcijeChange = (e) => {
  const novaVrednost = Number(e.target.value);

  if (novaVrednost < 1)
     return; 

  
  const faktor = novaVrednost / recept.st_porcij;


  const noveSestavine = sestavineOriginal.map(s => ({
    ...s,
    kolicina: Math.round(s.kolicina * faktor) // цел број
  }));

  setPorcije(novaVrednost);
  setSestavine(noveSestavine);

  if (hranilneVrednostiOriginal) {//pogoj da se ne sesuje če se ne pridobi hranilnih vrednosti
    const noveHranilneVrednosti = {
      ...hranilneVrednostiOriginal, 
      energija: Math.round(hranilneVrednostiOriginal.energija * faktor),
      bjelankovine: Math.round(hranilneVrednostiOriginal.bjelankovine * faktor),
      ogljikoviHidrati: Math.round(hranilneVrednostiOriginal.ogljikoviHidrati * faktor),
      mascobe: Math.round(hranilneVrednostiOriginal.mascobe * faktor)
    };
    setHranilneVrednosti(noveHranilneVrednosti);
  }
  
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

      <label><strong>Število porcij:</strong></label>
      <input type="number" min="1" value={porcije} onChange={handlePorcijeChange}/>
      <p><strong>Ocena:</strong> {recept.povprecnaOcena ? recept.povprecnaOcena.toFixed(1) : "Brez ocen"}</p>
      <p><strong>Priprava:</strong> {recept.priprava}</p>
      <p><strong>Uporabnik:</strong>{recept.uporabnik ? `${recept.uporabnik.ime} ${recept.uporabnik.priimek}` : "Neznan avtor"}</p>

      <h3>Sestavine</h3>
      {sestavine.length > 0 ? (
        <ul>
          {sestavine.map(s => (
            <li key={s.id}>{s.ime}: {s.kolicina} {s.enota}</li>
          ))}
        </ul>
      ) : (
        <p>Nima sestavine za recept: {recept.ime}.</p>
      )}
      <h3>Hranilne vrednosti</h3>
      {hranilneVrednosti ? (
        <ul key={hranilneVrednosti.id}>
          <li>Energijska vrednost :{hranilneVrednosti.energija} (kcal)</li>
          <li>Beljakovine: {hranilneVrednosti.bjelankovine} (g)</li>
          <li>Ogljikovi hidrati: {hranilneVrednosti.ogljikoviHidrati} (g)</li>
          <li>Mascobe: {hranilneVrednosti.mascobe} (g)</li>
        </ul>
      ):(<p>Recept ({recept.ime}) nima vnesenih hranilnih vrednosti</p>)
      }
      {!jeAvtor && (
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


      <IzvozButton recept={recept} sestavine={sestavine} st_porcij={porcije} />
    </div>
  );
}

export default ReceptPodrobnosti;
