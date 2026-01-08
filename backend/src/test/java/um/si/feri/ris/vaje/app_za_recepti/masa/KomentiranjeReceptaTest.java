package um.si.feri.ris.vaje.app_za_recepti.masa;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import um.si.feri.ris.vaje.app_za_recepti.controllers.KomentarController;
import um.si.feri.ris.vaje.app_za_recepti.dao.KomentarRepository;
import um.si.feri.ris.vaje.app_za_recepti.dao.ReceptRepository;
import um.si.feri.ris.vaje.app_za_recepti.dao.UporabnikRepository;
import um.si.feri.ris.vaje.app_za_recepti.models.HranilneVrednosti;
import um.si.feri.ris.vaje.app_za_recepti.models.Recept;
import um.si.feri.ris.vaje.app_za_recepti.models.Sestavina;
import um.si.feri.ris.vaje.app_za_recepti.models.Uporabnik;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

@Transactional
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

    //Ustvari testnega uporabnika in recept pred vsakim testom
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

        HranilneVrednosti hv = new HranilneVrednosti();
        hv.setEnergija(500);
        hv.setBjelankovine(30);
        hv.setOgljikoviHidrati(50);
        hv.setMascobe(20);
        hv.setRecept(recept);
        recept.setHranilneVrednosti(hv);

        Sestavina sestavina = new Sestavina();
        sestavina.setIme("test1");
        sestavina.setRecept(recept);

       sestavine = new ArrayList<>();
        sestavine.add(sestavina);
        recept.setSestavine(sestavine);
        receptRepository.save(recept);

    }

    //preveri da se komentar uspešno ustvari, odgovor vsebuje id, komentar obstaja v bazi
    @Test
    void ustvariKomentar_Obstaja(){

        KomentarController.KomentarRequest komentarRequest = new KomentarController.KomentarRequest("test1vsebina", uporabnik.getId());


        KomentarController.KomentarResponse res =komentarController.ustvariKomentar(recept.getId(), komentarRequest);

        assertNotNull(res);
        assertNotNull(res.id());
        assertTrue(komentarRepository.existsById(res.id()));

    }
    //preveri pravilno shranjevanje komentarja in da odgovor vsebuje pravilne info
    @Test
    void ustvariKomentar_VsebinainRelacije(){


        KomentarController.KomentarRequest komentarRequest = new KomentarController.KomentarRequest("test2Vsebina", uporabnik.getId());
        KomentarController.KomentarResponse res =komentarController.ustvariKomentar(recept.getId(), komentarRequest);

        assertNotNull(res);
        assertEquals("test2Vsebina", res.vsebina());
        assertEquals(uporabnik.getId(), res.uporabnikId());
        assertEquals(recept.getId(), res.receptId());

    }
    //Negativni test: preveri, da kontroler zavrne komentar z “prazno” vsebino, ter da se komentar ne shrani v bazo
    @Test
    void ustvariKomentar_BrezVsebine(){
        Long countBefore = komentarRepository.count();

        KomentarController.KomentarRequest komentarRequest = new KomentarController.KomentarRequest("  ", uporabnik.getId());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> komentarController.ustvariKomentar(recept.getId(), komentarRequest));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("vsebina komentarja je obvezna", ex.getReason());
        assertEquals(countBefore, komentarRepository.count());

    }
    //Negativni test: preveri da ob neobstoječem uporabniku kontroler vrne napako NOT_FOUND, ter da se komentar ne shrani
    @Test
    void ustvariKomentar_NiUporabnika(){

        KomentarController.KomentarRequest req =new KomentarController.KomentarRequest("test4Vsebina", 99999L);

        Long countBefore = komentarRepository.count();
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> komentarController.ustvariKomentar(recept.getId(), req));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Uporabnik ne obstaja", ex.getReason());
        assertEquals(countBefore, komentarRepository.count());
    }
    //Negativni test; preveri, da ob neobstoječem receptu kontroler vrne napako NOT_FOUND, ter da se komentar ne shrani
    @Test
    void ustvariKomentar_NiRecepta() {
        Long countBefore = komentarRepository.count();
        KomentarController.KomentarRequest req =new KomentarController.KomentarRequest("testvsebina", uporabnik.getId());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> komentarController.ustvariKomentar(9999L, req));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Recept ne obstaja", ex.getReason());
        assertEquals(countBefore, komentarRepository.count());
    }


    //Negativni test: preveri, da ob null requestu kontroler vrne napako BAD_REQUEST, ter da se komentar ne shrani
    @Test
    void ustvariKomentar_NullRequest() {
        long countBefore = komentarRepository.count();

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> komentarController.ustvariKomentar(recept.getId(), null));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals(countBefore, komentarRepository.count());
    }

}
