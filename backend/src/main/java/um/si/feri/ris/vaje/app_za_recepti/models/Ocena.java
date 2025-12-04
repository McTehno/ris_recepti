package um.si.feri.ris.vaje.app_za_recepti.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ocena {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Ocena od 1 do 5
    private int vrednost;

    // Povezava do recepta (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recept_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties("ocene") // Preprecimo neskončno zanko
    private Recept recept;

    // Povezava do uporabnika, ki je podal oceno
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uporabnik_id")
    @JsonIgnoreProperties("recepti")
    private Uporabnik uporabnik;
}