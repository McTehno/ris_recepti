package um.si.feri.ris.vaje.app_za_recepti.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Sestavina {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String ime;

    @ManyToOne
    @JoinColumn(name = "recept_id") // tocna kolona vo tabelata Recept sto go cuva id-to od korisnikot
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Recept recept;

}
