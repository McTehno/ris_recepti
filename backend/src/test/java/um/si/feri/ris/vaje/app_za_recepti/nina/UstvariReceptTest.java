package um.si.feri.ris.vaje.app_za_recepti.nina;

import org.junit.jupiter.api.*;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UstvariReceptTest {
    @Autowired
    private ReceptController receptController;

    @Autowired
    private ReceptRepository receptRepository;

    @Autowired
    private UporabnikRepository uporabnikRepository;

    private Uporabnik testUporabnik;
    private Recept testRecept;

    // Ustvarjanje testnega uporabnika pred vsemi testi
    @BeforeAll
    void createUser() {

        testUporabnik = new Uporabnik();
        testUporabnik.setIme("Nina");
        testUporabnik.setPriimek("Stevoska");
        testUporabnik.setGeslo("geslo123");
        testUporabnik = uporabnikRepository.save(testUporabnik);
    }

    // Ustvarjanje testnega recepta pred vsakim testom
    @BeforeEach
    void createTestRecept() {
        testRecept = new Recept();
        testRecept.setIme("Test Recept");
        testRecept.setTip("Sladok");
        testRecept.setPriprava("Priprava testnega recepta");
        testRecept.setUporabnik(testUporabnik);
        // Dodajanje dveh sestavin
        Sestavina s1 = new Sestavina();
        s1.setIme("Moka");
        s1.setRecept(testRecept);

        Sestavina s2 = new Sestavina();
        s2.setIme("Mleko");
        s2.setRecept(testRecept);

        List<Sestavina> sestavine = new ArrayList<>();
        sestavine.add(s1);
        sestavine.add(s2);
        testRecept.setSestavine(sestavine);
    }
    // Po vsakem testu izbrišemo testni recept, da baza ostane čista
    @AfterEach
    void deleteTestRecept() {
        if (testRecept != null && testRecept.getId() != null) {
            receptRepository.deleteById(testRecept.getId());
        }
    }
    // Po vseh testih izbrišemo testnega uporabnika
    @AfterAll
    void deleteTestUser() {
        if (testUporabnik != null && testUporabnik.getId() != null) {
            uporabnikRepository.deleteById(testUporabnik.getId());
        }
    }
    // Preverja, ali se ID dodeli ob ustvarjanju recepta
    @Test
    void testCreateRecept_IdAssigned() {
        Recept saved = receptController.createRecept(testRecept);
        assertNotNull(saved.getId());
    }
    // Preverja, ali se ime recepta pravilno shrani
    @Test
    void testCreateRecept_ImeCorrect() {
        Recept saved = receptController.createRecept(testRecept);
        assertEquals("Test Recept", saved.getIme());
    }
    // Preverja, ali je število sestavin pravilno shranjeno
    @Test
    void testCreateRecept_SestavineCount() {
        Recept saved = receptController.createRecept(testRecept);
        assertEquals(2, saved.getSestavine().size());
    }
    // Preverja, ali so sestavine pravilno povezane z receptom
    @Test
    void testCreateRecept_SestavineLinked() {
        Recept saved = receptController.createRecept(testRecept);
        assertEquals(saved, saved.getSestavine().get(0).getRecept());
        assertEquals(saved, saved.getSestavine().get(1).getRecept());
    }

    // Negativni scenarij: preverja, ali metoda ne dovoli ustvariti recepta brez sestavin
    @Test
    void testCreateRecept_BrezSestavin() {
        Recept r = new Recept();
        r.setIme("Test Recept");
        r.setTip("Sladica");
        r.setPriprava("Priprava recepta brez sestavin");
        r.setUporabnik(testUporabnik);
        r.setSestavine(new ArrayList<>());


        Recept saved = receptController.createRecept(r);

        assertNull(saved);
    }

}
