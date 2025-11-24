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
    private int ocena;
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

}
