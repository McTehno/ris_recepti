import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

import api from "../services/api"; 

function ReceptForm() {
  const { id } = useParams(); //ako ima id togas edit, ako ne ustvari
  const navigate = useNavigate();

  const [recept, setRecept] = useState({
    ime: "",
    tip: "",
    ocena: 10,  //ko implementiram ocenjevanje bo to tudi spremenjeno v ""
    priprava: "",
    sestavine: [{ ime: "" }]
  });

  useEffect(() => {
    if (id) {
      // Ako e edit, povikaj postoecki podatoci
      api.get(`/recepti/${id}`)
        .then(res => {
          setRecept({
            ...res.data,
            sestavine: res.data.sestavine.length > 0 ? res.data.sestavine : [{ ime: "" }]
          });
        })
        .catch(err => console.error(err));
    }
  }, [id]);

  //promena za input poleto
  const handleChange = (e, index) => {
    const { name, value } = e.target;  //name odd input elementot, value od input elementot //e.target e input elementot
    //OCENA, ke promenis ko ke dodajs ocenuvanje
    if (name === "ocena") return;
    if (name === "sestavina") {
      const newSestavine = [...recept.sestavine];
      newSestavine[index].ime = value;
      setRecept({ ...recept, sestavine: newSestavine });
    } else {
      setRecept({ ...recept, [name]: value });
    }
  };

  const handleAddSestavina = () => {
    setRecept({ ...recept, sestavine: [...recept.sestavine, { ime: "" }] });
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
  if (id) {
    //pri submit  da se promeni receptot i da ne vrati na moji recepti
     api.put(`/recepti/${id}`, recept)
      .then(() => navigate("/moji-recepti"))
      .catch(err => console.error(err));
  } else {
    // da se ustvari nov recept
    api.post("/recepti/post", recept)
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
         {/* Skrita ocena vedno 10 */}
        <input type="hidden" name="ocena" value={recept.ocena} />
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
            <button type="button" onClick={() => handleRemoveSestavina(index)}>Izbrisi</button>
          </div>
        ))}
        <button type="button" onClick={handleAddSestavina}>Dodaj sestavina</button>

        <br /><br />
        <button type="submit">{id ? "Spremeni" : "Ustvari"}</button>
      </form>
    </div>
  );
}

export default ReceptForm;
