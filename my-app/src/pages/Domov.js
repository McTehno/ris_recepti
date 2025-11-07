import React from "react";
import { Link } from "react-router-dom";

function Domov() {
  return (
    <div className="domov-container">
      <main>
        <section className="intro">
          <h2>Dobrodošli v naši aplikaciji za recepte!</h2>
          <p>
            Tukaj lahko enostavno shranjujete svoje najljubše recepte, dodajate
            nove sestavine in pregledujete vse recepte, ki ste jih ustvarili.
            Pridružite se nam in odkrijte svet okusov!
          </p>
        </section>

        <div className="cards-container">
          {/* Prva kartica */}
          <div className="card">
            <h5 className="card-title">Vsi recepti</h5>
            <p className="card-text">Pregled vseh svojih receptov.</p>
            <Link to="/vsi-recepti">
              <button className="btn btn-action">Poglej</button>
            </Link>
          </div>

          {/* Druga kartica */}
          <div className="card">
            <h5 className="card-title">Nov recept</h5>
            <p className="card-text">Ustvari nov recept in dodaj sestavine.</p>
            <Link to="/nov-recept">
              <button className="btn btn-action">Ustvari</button>
            </Link>
          </div>
        </div>
      </main>
    </div>
  );
}

export default Domov;
