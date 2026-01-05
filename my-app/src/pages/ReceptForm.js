import React, { use, useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

import api from "../services/api"; 

function ReceptForm() {
  const userId = sessionStorage.getItem('userId');
  const { id } = useParams(userId); //ako ima id togas edit, ako ne ustvari
  const navigate = useNavigate();

 
  const [recept, setRecept] = useState({
      ime: "",
      tip: "",
      priprava: "",
      st_porcij: 1,
      sestavine: [{ ime: "", kolicina: 0, enota: "" }],
      hranilneVrednosti: [
        { energija: 0, bjelankovine: 0, ogljikoviHidrati: 0, mascobe: 0 }
      ]
  });


  useEffect(() => {
  if (!userId) {
      navigate('/login');
      return;
    }
  }, [userId, navigate]);
  
  useEffect(() => {
    if (id) {
      // Ako e edit, povikaj postoecki podatoci
      api.get(`/recepti/${id}`)
        .then(res => {
          setRecept({
            ...res.data,
            sestavine: res.data.sestavine.length > 0 ? res.data.sestavine : [{ ime: "", kolicina: 0, enota: "" }]
          });
        })
        .catch(err => console.error(err));
    }
  }, [id]);

  //promena za input poleto
  const handleChange = (e, index) => {
    const { name, value } = e.target;  //name odd input elementot, value od input elementot //e.target e input elementot
    if (name === "sestavina" || name === "kolicina" || name === "enota") {
      const newSestavine = [...recept.sestavine];
      if (name === "sestavina") {
        newSestavine[index].ime = value;
      } else {
        newSestavine[index][name] = value;
      }
      setRecept({ ...recept, sestavine: newSestavine });
    } else {
      setRecept({ ...recept, [name]: value });
    }
  };
  const handleChangeHranilne = (e, index) => {
    const { name, value } = e.target;
    const novaHranilna = [...recept.hranilneVrednosti];
    novaHranilna[index][name] = Number(value); // сите вредности се броеви
    setRecept({ ...recept, hranilneVrednosti: novaHranilna });
  };

  const handleAddSestavina = () => {
    setRecept({ ...recept, sestavine: [...recept.sestavine, { ime: "", kolicina: 0, enota: "" }] });
  };

  // Funkcija za brisenje edna sostojka
const handleRemoveSestavina = (index) => {
  // Se ustvarja nova lista z sestavine, brez tisto ki jo brisemo
  const novaListaSestavini = recept.sestavine.filter((sestavina, i) => i !== index);
  
  // Go updejtam stejtot na receptite so novata lista na sostojki
  setRecept({ 
    ...recept, 
    sestavine: novaListaSestavini 
  });
};


  const handleSubmit = (e) => {
  e.preventDefault();
  const ocisceneSestavine = recept.sestavine.map(sestavina => {
      return {
        id: sestavina.id, // Id ohranimo za urejanje
        ime: sestavina.ime,
        kolicina: sestavina.kolicina,
        enota: sestavina.enota
        // ne vkljucujemo recept tukaj ker se dogaja se en infinite loop pri parse-anju
      };
    });
    if (ocisceneSestavine.length === 0) {
    alert("Sestavine so obvezni!");
    return; 
  }

  if (recept.st_porcij < 1) {
    alert("Število porcij mora biti vsaj 1!");
    return;
  }

  const receptZaPosiljanje = {
      ime: recept.ime,
      tip: recept.tip,
      priprava: recept.priprava,
      st_porcij: recept.st_porcij,
      sestavine: ocisceneSestavine,
      hranilneVrednosti: recept.hranilneVrednosti,
      uporabnik: { id: userId }
    };

  if (id) {
    //pri submit  da se promeni receptot i da ne vrati na moji recepti
     api.put(`/recepti/${id}`, receptZaPosiljanje)
      .then(() => navigate("/moji-recepti"))
      .catch(err => console.error(err));
  } else {
    // da se ustvari nov recept
    api.post("/recepti/post", receptZaPosiljanje)
      .then(() => navigate("/moji-recepti"))
      .catch(err => console.error(err));
  }
};


  return (
    <div className="recept-form">
      <h2>{id ? "Uredi recept" : "Nov recept"}</h2>
      <form onSubmit={handleSubmit}>
        <input type="text" name="ime" placeholder="Ime" value={recept.ime} onChange={handleChange} required />
        <input type="text" name="tip" placeholder="Tip" value={recept.tip} onChange={handleChange} required />
        <label>Stevilo porcij:</label>
        <input type="number" name="st_porcij" placeholder="Število porcij" value={recept.st_porcij} onChange={handleChange} min="1" required />
        <textarea name="priprava" placeholder="Priprava" value={recept.priprava} onChange={handleChange} />

        <h3>Sestavine</h3>
        {recept.sestavine.map((s, index) => (
          <div key={index} style={{ marginBottom: "5px" }}>
            <input
              type="text"
              name="sestavina"
              placeholder="Ime sestavine"
              value={s.ime}
              onChange={(e) => handleChange(e, index)}
              required
            />
            <input
              type="number"
              name="kolicina"
              placeholder="Količina"
              value={s.kolicina}
              onChange={(e) => handleChange(e, index)}
              required
            />
            <select
              name="enota"
              value={s.enota}
              onChange={(e) => handleChange(e, index)}
              required
            >
              <option value="">Izberi enoto</option>
              <option value="g">g</option>
              <option value="kg">kg</option>
              <option value="ml">ml</option>
              <option value="l">l</option>
              <option value="žlica">žlica</option>
              <option value="čajna žlička">čajna žlička</option>
              <option value="kos/i">kos/i</option>
            </select>

            <button type="button" onClick={() => handleRemoveSestavina(index)}>Izbrisi</button>
          </div>
        ))}
        <button type="button" onClick={handleAddSestavina}>Dodaj sestavina</button>

        <br /><br />

        <h3>Hranilne vrednosti</h3>
          <div style={{ marginBottom: "5px" }}>
            <label>Energija (kcal):</label>
            <input
              type="number"
              name="energija"
              placeholder="Energija"
              value={recept.hranilneVrednosti[0].energija}
              onChange={(e) => handleChangeHranilne(e, 0)}
              required
            />
          </div>

          <div style={{ marginBottom: "5px" }}>
            <label>Beljakovine (g):</label>
            <input
              type="number"
              name="bjelankovine"
              placeholder="Beljakovine"
              value={recept.hranilneVrednosti[0].bjelankovine}
              onChange={(e) => handleChangeHranilne(e, 0)}
              required
            />
          </div>

          <div style={{ marginBottom: "5px" }}>
            <label>Ogljikovi hidrati (g):</label>
            <input
              type="number"
              name="ogljikoviHidrati"
              placeholder="Ogljikovi hidrati"
              value={recept.hranilneVrednosti[0].ogljikoviHidrati}
              onChange={(e) => handleChangeHranilne(e, 0)}
              required
            />
          </div>

          <div style={{ marginBottom: "5px" }}>
            <label>Maščobe (g):</label>
            <input
                type="number"
                name="mascobe"
                placeholder="Maščobe"
                value={recept.hranilneVrednosti[0].mascobe}
                onChange={(e) => handleChangeHranilne(e, 0)}
                required
            />
          </div>



        <button type="submit">{id ? "Spremeni" : "Ustvari"}</button>
      </form>
    </div>
  );
}

export default ReceptForm;
