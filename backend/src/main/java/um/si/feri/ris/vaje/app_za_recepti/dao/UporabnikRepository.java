package um.si.feri.ris.vaje.app_za_recepti.dao;

import org.springframework.data.repository.CrudRepository;
import um.si.feri.ris.vaje.app_za_recepti.models.Uporabnik;

import java.util.Optional;

public interface UporabnikRepository extends CrudRepository<Uporabnik, Long> {
    Optional<Uporabnik> findByEnaslov(String enaslov);
}
