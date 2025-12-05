package um.si.feri.ris.vaje.app_za_recepti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
        Optional<Recept> receptOpt = receptRepository.findById(idRecepta);

        if (ocena.isPresent() && receptOpt.isPresent()) {
            Recept recept = receptOpt.get();
            recept.izracunajPovprecje();
            return ResponseEntity.ok(ocena.get().getVrednost());

        }
        return ResponseEntity.ok(0); // 0 pomeni, da se ni ocenjeno
    }

    @PostMapping("/recept/{idRecepta}")
    @Transactional // POMEMBNOOOOOO, zagotovi, da se vse zgodi v eni transakciji
    public ResponseEntity<Ocena> dodajOcenoReceptu(@PathVariable Long idRecepta, @RequestBody OcenaRequest request) {

        Optional<Recept> receptOpt = receptRepository.findById(idRecepta);
        Optional<Uporabnik> uporabnikOpt = uporabnikRepository.findById(request.uporabnikId);

        if (receptOpt.isPresent() && uporabnikOpt.isPresent()) {
            Recept recept = receptOpt.get();
            Uporabnik uporabnik = uporabnikOpt.get();

            // Ustvarimo objekt ocena
            Ocena novaOcena = new Ocena();
            novaOcena.setUporabnik(uporabnik);
            novaOcena.setVrednost(request.vrednost);

            recept.dodajNovoOceno(novaOcena);
            ocenaRepository.save(novaOcena);
            receptRepository.save(recept);

            return ResponseEntity.ok(novaOcena);
        }
        return ResponseEntity.notFound().build();
    }
}