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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OcenjevanjeReceptaTest {

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
        uporabnik.setIme("Mihajlo");
        uporabnik.setPriimek("Test");
        uporabnik.setEnaslov("mihajlo@test.com");
        uporabnik.setGeslo("geslo123");
        uporabnikRepository.save(uporabnik);

        recept = new Recept();
        recept.setIme("Mihajlo Recept");
        recept.setTip("Glavna jed");
        recept.setPriprava("Kuhaj malo");
        recept.setPovprecnaOcena(0.0);

        HranilneVrednosti hv = new HranilneVrednosti();
        hv.setEnergija(400);
        hv.setBjelankovine(20);
        hv.setOgljikoviHidrati(30);
        hv.setMascobe(10);
        hv.setRecept(recept);
        recept.setHranilneVrednosti(hv);

        receptRepository.save(recept);
    }

    @Test
    void dodajOcenoUspesno() {
        OcenaController.OcenaRequest request = new OcenaController.OcenaRequest();
        request.vrednost = 5;
        request.uporabnikId = uporabnik.getId();

        ResponseEntity<Ocena> response = ocenaController.dodajOcenoReceptu(recept.getId(), request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().getVrednost());
        assertEquals(uporabnik.getId(), response.getBody().getUporabnik().getId());
        
        // preveri v bazi
        Optional<Ocena> shranjenaOcena = ocenaRepository.findByReceptIdAndUporabnikId(recept.getId(), uporabnik.getId());
        assertTrue(shranjenaOcena.isPresent());
        assertEquals(5, shranjenaOcena.get().getVrednost());
    }

    @Test
    void posodobiOcenoUspesno() {
        // prva ocena
        OcenaController.OcenaRequest request1 = new OcenaController.OcenaRequest();
        request1.vrednost = 3;
        request1.uporabnikId = uporabnik.getId();
        ocenaController.dodajOcenoReceptu(recept.getId(), request1);

        // posodobitev ocene
        OcenaController.OcenaRequest request2 = new OcenaController.OcenaRequest();
        request2.vrednost = 5;
        request2.uporabnikId = uporabnik.getId();
        ResponseEntity<Ocena> response = ocenaController.dodajOcenoReceptu(recept.getId(), request2);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(5, response.getBody().getVrednost());

        // preveri, da je v bazi samo ena ocena in da je posodobljena
        Optional<Ocena> shranjenaOcena = ocenaRepository.findByReceptIdAndUporabnikId(recept.getId(), uporabnik.getId());
        assertTrue(shranjenaOcena.isPresent());
        assertEquals(5, shranjenaOcena.get().getVrednost());
    }

    @Test
    void dodajOcenoNeuspesnoNiUporabnika() {
        OcenaController.OcenaRequest request = new OcenaController.OcenaRequest();
        request.vrednost = 4;
        request.uporabnikId = 6969L; // neobstojec ID

        ResponseEntity<Ocena> response = ocenaController.dodajOcenoReceptu(recept.getId(), request);

        assertEquals(404, response.getStatusCode().value());
    }
}
