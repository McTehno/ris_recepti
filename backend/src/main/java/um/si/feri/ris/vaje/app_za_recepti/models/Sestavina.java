package um.si.feri.ris.vaje.app_za_recepti.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Sestavina {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String ime;
    private double kolicina;
    private String enota;

    @ManyToOne
    @JoinColumn(name = "recept_id") // tocna kolona vo tabelata Recept sto go cuva id-to od korisnikot
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties("sestavine") //izognemo se infinite loopu ker se znotraj recepta nahaja sestavina
    @ToString.Exclude//prespreci zapis neskoncne zankepri testih
    @EqualsAndHashCode.Exclude
    private Recept recept;

}
