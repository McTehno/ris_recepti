package um.si.feri.ris.vaje.app_za_recepti.dao;


import org.springframework.data.repository.CrudRepository;
import um.si.feri.ris.vaje.app_za_recepti.models.Komentar;
import um.si.feri.ris.vaje.app_za_recepti.models.Recept;

import java.util.List;
import java.util.Optional;

public interface KomentarRepository extends CrudRepository<Komentar, String> {
    List<Komentar> findByRecept(Recept recept);
}

