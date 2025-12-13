package um.si.feri.ris.vaje.app_za_recepti.masa;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import um.si.feri.ris.vaje.app_za_recepti.controllers.KomentarController;
import um.si.feri.ris.vaje.app_za_recepti.dao.KomentarRepository;
import um.si.feri.ris.vaje.app_za_recepti.dao.ReceptRepository;
import um.si.feri.ris.vaje.app_za_recepti.dao.UporabnikRepository;
import um.si.feri.ris.vaje.app_za_recepti.models.Komentar;
import um.si.feri.ris.vaje.app_za_recepti.models.Recept;
import um.si.feri.ris.vaje.app_za_recepti.models.Sestavina;
import um.si.feri.ris.vaje.app_za_recepti.models.Uporabnik;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class KomentiranjeReceptaTest {

    @Autowired
    KomentarRepository komentarRepository;

    @Autowired
    KomentarController komentarController;
    @Autowired
    UporabnikRepository uporabnikRepository;

    @Autowired
    ReceptRepository receptRepository;

    private  Uporabnik uporabnik;

    private Recept recept;
    private List<Sestavina> sestavine;

    @BeforeEach
     void ustvariUporabnikainRecept() {
        uporabnik = new Uporabnik();
        uporabnik.setIme("test1i");
        uporabnik.setPriimek("test12p");
        uporabnik.setEnaslov("test1@test.com");
        uporabnik.setGeslo("gtest1");
        uporabnikRepository.save(uporabnik);


        recept= new Recept();
        recept.setIme("test1Recept");
        recept.setTip("test1Tip");
        recept.setPriprava("test1Priprava");
        recept.setPovprecnaOcena(0.0);

        Sestavina sestavina = new Sestavina();
        sestavina.setIme("test1");
        sestavina.setRecept(recept);

       sestavine = new ArrayList<>();
        sestavine.add(sestavina);
        recept.setSestavine(sestavine);
        receptRepository.save(recept);
    }

    @Test
    void ustvariKomentarUspeh(){
        Komentar komentar = new Komentar();
        komentar.setVsebina("test1Vsebina");
        komentar.setUporabnik(uporabnik);
        KomentarController.KomentarRequest komentarRequest = new KomentarController.KomentarRequest(komentar.getVsebina(), uporabnik.getId());
        KomentarController.KomentarResponse res =komentarController.ustvariKomentar(recept.getId(), komentarRequest);

        assertNotNull(res);
        assertEquals("test1Vsebina", res.vsebina());
        assertEquals(uporabnik.getId(), res.uporabnikId());
        assertEquals(recept.getId(), res.receptId());
        assertTrue(komentarRepository.existsById(res.id()));
        //assertTrue(komentarRepository.findById(res.getId()).isPresent())
    }

    @Test
    void ustvariKomentarNiUporabnika(){
        KomentarController.KomentarRequest req =new KomentarController.KomentarRequest("test1Vsebina", 999L);

        KomentarController.KomentarResponse res =komentarController.ustvariKomentar(recept.getId(),req);
        assertNull(res);
    }

}
