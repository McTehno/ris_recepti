package um.si.feri.ris.vaje.app_za_recepti.dao;

import org.springframework.data.repository.CrudRepository;
import um.si.feri.ris.vaje.app_za_recepti.models.Sestavina;

import java.util.List;

public interface SestavinaRepository extends CrudRepository<Sestavina, Long> {
    List<Sestavina> findByReceptId(Long receptId); // site sostojki na eden recept
}
