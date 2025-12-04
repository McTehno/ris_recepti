package um.si.feri.ris.vaje.app_za_recepti.dao;

import org.springframework.data.repository.CrudRepository;
import um.si.feri.ris.vaje.app_za_recepti.models.Ocena;
import um.si.feri.ris.vaje.app_za_recepti.models.Recept;
import java.util.List;

public interface OcenaRepository extends CrudRepository<Ocena, Long> {
    List<Ocena> findByRecept(Recept recept);
}