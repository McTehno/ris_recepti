package um.si.feri.ris.vaje.app_za_recepti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import um.si.feri.ris.vaje.app_za_recepti.dao.ReceptRepository;
import um.si.feri.ris.vaje.app_za_recepti.dao.UporabnikRepository;
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

    // DTO (Data Transfer Object) za prejem podatkov iz frontenda
    // To je notranji razred samo za prenos podatkov
    public static class OcenaRequest {
        public int vrednost; // 1-5
        public Long uporabnikId; // ID uporabnika, ki ocenjuje
    }

    /**
     * POST metoda za dodajanje ocene receptu.
     * URL: /api/ocene/recept/{idRecepta}
     * Body: { "vrednost": 5, "uporabnikId": 1 }
     */
    @PostMapping("/recept/{idRecepta}")
    public Recept dodajOcenoReceptu(@PathVariable Long idRecepta, @RequestBody OcenaRequest request) {

        // 1. Poiščemo recept
        Optional<Recept> receptOpt = receptRepository.findById(idRecepta);
        // 2. Poiščemo uporabnika
        Optional<Uporabnik> uporabnikOpt = uporabnikRepository.findById(request.uporabnikId);

        if (receptOpt.isPresent() && uporabnikOpt.isPresent()) {
            Recept recept = receptOpt.get();
            Uporabnik uporabnik = uporabnikOpt.get();

            // Validacija ocene (da je med 1 in 5)
            if (request.vrednost < 1 || request.vrednost > 5) {
                throw new IllegalArgumentException("Ocena mora biti med 1 in 5");
            }

            // 3. Uporabimo metodo znotraj modela Recept (kot zahtevano)
            // Ta metoda ustvari objekt Ocena, ga doda v seznam in preračuna povprečje
            recept.dodajOceno(request.vrednost, uporabnik);

            // 4. Shranimo recept.
            // Zaradi CascadeType.ALL na seznamu 'ocene' se bo v bazo shranila tudi nova Ocena!
            return receptRepository.save(recept);
        }

        return null; // Ali vrneš ustrezen HTTP error status
    }
}