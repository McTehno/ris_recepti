package um.si.feri.ris.vaje.app_za_recepti.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Uporabnik {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String ime;
    private String priimek;
    private String enaslov;
    private String geslo;

    @OneToMany(mappedBy = "uporabnik", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Recept> recepti;

}
