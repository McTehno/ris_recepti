package um.si.feri.ris.vaje.app_za_recepti.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HranilneVrednosti {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double energija;
    private double bjelankovine;
    private double ogljikoviHidrati;
    private double mascobe;

    @ManyToOne
    @JoinColumn(name = "recept_id") // tocna kolona vo tabelata Recept sto go cuva id-to od korisnikot
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties("hranilneVrednosti") //izognemo se infinite loopu ker se znotraj recepta nahaja hranilne vrednosti
    private Recept recept;

}
