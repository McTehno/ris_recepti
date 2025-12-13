package um.si.feri.ris.vaje.app_za_recepti.nina;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import um.si.feri.ris.vaje.app_za_recepti.controllers.ReceptController;
import um.si.feri.ris.vaje.app_za_recepti.dao.ReceptRepository;
import um.si.feri.ris.vaje.app_za_recepti.dao.UporabnikRepository;
import um.si.feri.ris.vaje.app_za_recepti.models.Recept;
import um.si.feri.ris.vaje.app_za_recepti.models.Sestavina;
import um.si.feri.ris.vaje.app_za_recepti.models.Uporabnik;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
public class BrisanjeReceptTest {

    @Autowired
    private ReceptController receptController;

    @Autowired
    private ReceptRepository receptRepository;

    @Autowired
    private UporabnikRepository uporabnikRepository;

    private Recept newRecept;
    private Uporabnik u;

    // Ustvarjanje testnega uporabnika pred vsemi testi
    @BeforeAll
    void createUser() {
        u = new Uporabnik();
        u.setIme("Nina");
        u.setPriimek("Stevoska");
        u.setGeslo("geslo123");
        u = uporabnikRepository.save(u);
    }
    // Ustvarjanje testnega recepta pred vsakim testom
    @BeforeEach
    void createRecept() {
        newRecept = new Recept();
        newRecept.setIme("Test Recept");
        newRecept.setTip("Sladica");
        newRecept.setPriprava("Priprava testnega recepta.");
        newRecept.setUporabnik(u);
        newRecept.setPovprecnaOcena(0.0);

        // Dodajanje ene sestavine
        Sestavina s = new Sestavina();
        s.setIme("Moka");
        s.setRecept(newRecept);

        List<Sestavina> sestavine = new ArrayList<>();
        sestavine.add(s);
        newRecept.setSestavine(sestavine);

        newRecept.setOcene(new ArrayList<>());

        newRecept = receptRepository.save(newRecept);
    }
    @AfterAll
    void deleteTestUser() {
        if (u != null && u.getId() != null) {
            uporabnikRepository.deleteById(u.getId());
        }
    }
    // Test preverja, ali metoda pravilno vrne sporočilo o uspešnem brisanju recepta
    @Test
    void testDeleteRecept_SuccessMessage() {
        String response = receptController.deleteRecept(newRecept.getId());
        assertEquals("Recept z ID " + newRecept.getId() + " je bil izbrisan.", response);

    }
    // Test preverja, ali je recept po klicu metode dejansko odstranjen iz baze
    @Test
    void testDeleteRecept_Success() {
        String response = receptController.deleteRecept(newRecept.getId());

        assertFalse(receptRepository.existsById(newRecept.getId()));
    }

    // Test preverja, ali metoda pravilno ravna z neobstoječim ID-jem in vrne ustrezno sporočilo
    @Test
    void testDeleteRecept_NotFound() {
        long nonExistingId = 999L;
        String response = receptController.deleteRecept(nonExistingId);
        assertEquals("Recept z ID " + nonExistingId + " ne obstaja.", response);
    }
}