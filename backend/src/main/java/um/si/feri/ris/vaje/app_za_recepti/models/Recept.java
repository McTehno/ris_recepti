package um.si.feri.ris.vaje.app_za_recepti.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Recept {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String ime;
    private LocalDate datumUstvarjanja;
    private String tip;
    private double povprecnaOcena;
    @Lob //za cuvanje tekst
    private String priprava;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uporabnik_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties("recepti") //da se izognemo infinite loopu ker uporabnik vsebuje objekt tipa recept
    private Uporabnik uporabnik;


    @PrePersist  //za pri dodavanje vo bazata da se zacuva avtomatski datumot
    protected void onCreate() {
        this.datumUstvarjanja = LocalDate.now();
    }

    @OneToMany(mappedBy = "recept", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("recepti") //da se izognemo infinite loopu ker sestavina vsebuje objekt tipa recept
    private List<Sestavina> sestavine;

    @OneToMany(mappedBy = "recept", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("recept") // Prepreci zanko nazaj na recept
    private List<Ocena> ocene;

    // --- POSLOVNA LOGIKA ---
    public void izracunajPovprecje() {
        if (this.ocene == null || this.ocene.isEmpty()) {
            this.povprecnaOcena = 0.0;
            return;
        }
        double vsota = 0;
        for (Ocena o : this.ocene) {
            vsota += o.getVrednost();
        }
        this.povprecnaOcena = vsota / this.ocene.size();
    }

    public void dodajNovoOceno(Ocena novaOcena) {
        if (this.ocene == null) {
            this.ocene = new ArrayList<>();
        }

        // 1. dodamo oceno v seznam na receptu (v ramu)
        this.ocene.add(novaOcena);

        // 2. povezemo oceno z receptom (pomembno za bazo)
        novaOcena.setRecept(this);

        // 3. takoj preracuna novo povprecje
        izracunajPovprecje();
    }
}

