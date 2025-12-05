package um.si.feri.ris.vaje.app_za_recepti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import um.si.feri.ris.vaje.app_za_recepti.dao.ReceptRepository;
import um.si.feri.ris.vaje.app_za_recepti.dao.UporabnikRepository;
import um.si.feri.ris.vaje.app_za_recepti.dao.OcenaRepository;
import um.si.feri.ris.vaje.app_za_recepti.models.Ocena;
import um.si.feri.ris.vaje.app_za_recepti.models.Recept;
import um.si.feri.ris.vaje.app_za_recepti.models.Uporabnik;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/ocene")
public class OcenaController {

    @Autowired
    private ReceptRepository receptRepository;

    @Autowired
    private UporabnikRepository uporabnikRepository;

    @Autowired
    private OcenaRepository ocenaRepository;

    // DTO (Data Transfer Object) za prejem podatkov iz frontenda
    // To je notranji razred samo za prenos podatkov
    public static class OcenaRequest {
        public int vrednost; // 1-5
        public Long uporabnikId; // ID uporabnika, ki ocenjuje
    }

    @GetMapping("/recept/{idRecepta}/uporabnik/{idUporabnika}")
    public ResponseEntity<Integer> vrniMojoOceno(@PathVariable Long idRecepta, @PathVariable Long idUporabnika) {
        Optional<Ocena> ocena = ocenaRepository.findByReceptIdAndUporabnikId(idRecepta, idUporabnika);
        if (ocena.isPresent()) {
            return ResponseEntity.ok(ocena.get().getVrednost());
        }
        return ResponseEntity.ok(0); // 0 pomeni, da se ni ocenjeno
    }

    @PostMapping("/recept/{idRecepta}")
    public ResponseEntity<Ocena> dodajOcenoReceptu(@PathVariable Long idRecepta, @RequestBody OcenaRequest request) {

        // 1. Poiščemo recept
        Optional<Recept> receptOpt = receptRepository.findById(idRecepta);
        // 2. Poiščemo uporabnika
        Optional<Uporabnik> uporabnikOpt = uporabnikRepository.findById(request.uporabnikId);

        if (receptOpt.isPresent() && uporabnikOpt.isPresent()) {
            Recept recept = receptOpt.get();
            Uporabnik uporabnik = uporabnikOpt.get();

            // 3. Preverjamo obstoj ocene
            Optional<Ocena> obstoecaOcena = ocenaRepository.findByReceptIdAndUporabnikId(idRecepta, request.uporabnikId);

            // 4. Pripravljamo oceno za shranjevanje
            Ocena ocenaZaShranit;
            if (obstoecaOcena.isPresent()) {
                ocenaZaShranit = obstoecaOcena.get();
                ocenaZaShranit.setVrednost(request.vrednost);
            } else {
                // USTVARJANJE NOVE (INSERT)
                ocenaZaShranit = new Ocena();
                ocenaZaShranit.setRecept(recept);
                ocenaZaShranit.setUporabnik(uporabnik);
                ocenaZaShranit.setVrednost(request.vrednost);
            }

            // 5. Shranjevanje/Posodabljanje ocene
            ocenaRepository.save(ocenaZaShranit);

            return ResponseEntity.ok(ocenaZaShranit);
        }

        return null;
    }
}