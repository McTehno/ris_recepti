package um.si.feri.ris.vaje.app_za_recepti.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Komentar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String vsebina;

    @ManyToOne
    Uporabnik uporabnik;
    @ManyToOne
    Recept recept;
}