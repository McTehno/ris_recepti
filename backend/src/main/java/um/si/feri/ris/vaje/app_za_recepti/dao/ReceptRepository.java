package um.si.feri.ris.vaje.app_za_recepti.dao;

import org.springframework.data.repository.CrudRepository;
import um.si.feri.ris.vaje.app_za_recepti.models.Recept;
import um.si.feri.ris.vaje.app_za_recepti.models.Uporabnik;

import java.util.List;

public interface ReceptRepository extends CrudRepository<Recept, Long> {
    List<Recept> findAll(); // za da dobijam lista anmesto iterable

    List<Recept> findByImeContaining(String ime);

    // NOVO: pridobivanje receptov po uporabnik
    // pridobivanje recptov po imena za ta konkretni uporabnik
    List<Recept> findByUporabnik(Uporabnik uporabnik);
    List<Recept> findByUporabnikAndImeContaining(Uporabnik uporabnik, String ime);

}
