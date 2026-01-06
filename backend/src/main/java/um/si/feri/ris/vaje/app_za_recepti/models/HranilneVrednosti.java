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

    @OneToOne
    @JoinColumn(name = "recept_id")
    @JsonIgnoreProperties("hranilneVrednosti")
    private Recept recept;

}
