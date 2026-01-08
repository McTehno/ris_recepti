package um.si.feri.ris.vaje.app_za_recepti.mihajlo;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import um.si.feri.ris.vaje.app_za_recepti.controllers.OcenaController;
import um.si.feri.ris.vaje.app_za_recepti.dao.OcenaRepository;
import um.si.feri.ris.vaje.app_za_recepti.dao.ReceptRepository;
import um.si.feri.ris.vaje.app_za_recepti.dao.UporabnikRepository;
import um.si.feri.ris.vaje.app_za_recepti.models.HranilneVrednosti;
import um.si.feri.ris.vaje.app_za_recepti.models.Ocena;
import um.si.feri.ris.vaje.app_za_recepti.models.Recept;
import um.si.feri.ris.vaje.app_za_recepti.models.Uporabnik;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PridobiOcenoTest {

    @Autowired
    private OcenaController ocenaController;

    @Autowired
    private OcenaRepository ocenaRepository;

    @Autowired
    private ReceptRepository receptRepository;

    @Autowired
    private UporabnikRepository uporabnikRepository;

    private Uporabnik uporabnik;
    private Recept recept;

    @BeforeEach
    void setup() {
        uporabnik = new Uporabnik();
        uporabnik.setIme("Mihajlo2");
        uporabnik.setPriimek("Test2");
        uporabnik.setEnaslov("mihajlo2@test.com");
        uporabnik.setGeslo("geslo123");
        uporabnikRepository.save(uporabnik);

        recept = new Recept();
        recept.setIme("Mihajlov Recept 2");
        recept.setTip("Sladica");
        recept.setPriprava("Peci 20 min");
        recept.setPovprecnaOcena(0.0);

        HranilneVrednosti hv = new HranilneVrednosti();
        hv.setEnergija(300);
        hv.setBjelankovine(10);
        hv.setOgljikoviHidrati(40);
        hv.setMascobe(15);
        hv.setRecept(recept);
        recept.setHranilneVrednosti(hv);

        receptRepository.save(recept);
    }

    @Test
    void vrniMojoOcenoObstaja() {
        // Prvo ustvarimo oceno
        Ocena ocena = new Ocena();
        ocena.setUporabnik(uporabnik);
        ocena.setRecept(recept);
        ocena.setVrednost(4);
        ocenaRepository.save(ocena);

        ResponseEntity<Integer> response = ocenaController.vrniMojoOceno(recept.getId(), uporabnik.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(4, response.getBody());
    }

    @Test
    void vrniMojoOcenoNeObstaja() {
        // Ne ustvari ocene
        ResponseEntity<Integer> response = ocenaController.vrniMojoOceno(recept.getId(), uporabnik.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(0, response.getBody()); // 0 pomeni, da ni ocenjeno
    }

    @Test
    void vrniMojoOcenoNeobstojecRecept() {
        ResponseEntity<Integer> response = ocenaController.vrniMojoOceno(6969L, uporabnik.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(0, response.getBody());
    }
}
