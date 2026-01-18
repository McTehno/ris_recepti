package um.si.feri.ris.vaje.app_za_recepti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import um.si.feri.ris.vaje.app_za_recepti.dao.UporabnikRepository;
import um.si.feri.ris.vaje.app_za_recepti.models.Uporabnik;


import um.si.feri.ris.vaje.app_za_recepti.dao.ReceptRepository;
import um.si.feri.ris.vaje.app_za_recepti.models.Recept;
import java.util.Set;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/uporabniki")

public class UporabnikController {

    @Autowired

    private UporabnikRepository uporabnikRepository;

    //Dodam za vseckanje
    @Autowired
    private ReceptRepository receptRepository;


    // Kreiram korisnik i vrakam Korisnik koj e kreiran
    @PostMapping("/post")
    public Uporabnik createUporabnik(@RequestBody Uporabnik uporabnik) {

        return uporabnikRepository.save(uporabnik);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Uporabnik loginPodatki) {
        // Poiščemo uporabnika po e-naslovu
        Optional<Uporabnik> uporabnik = uporabnikRepository.findByEnaslov(loginPodatki.getEnaslov());

        if (uporabnik.isPresent()) {
            // Preverimo geslo
            if (uporabnik.get().getGeslo().equals(loginPodatki.getGeslo())) {
                return ResponseEntity.ok(uporabnik.get());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Napačno geslo");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Uporabnik ne obstaja");
        }
    }

    // Gi pridobivam site korisnici vo List
    @GetMapping
    public List<Uporabnik> getAllUporabniki() {
        return (List<Uporabnik>) uporabnikRepository.findAll();
    }

    // Dobivam korisnik po id
    @GetMapping("/{id}")
    public Optional<Uporabnik> getUporabnikById(@PathVariable Long id) {
        return uporabnikRepository.findById(id);
    }
    //PRomena na korisnikot spored id i objekt korisnik so promeneti atributi
    @PutMapping("/{id}")
    public Uporabnik updateUporabnik(@PathVariable Long id, @RequestBody Uporabnik uporabnikDetails) {
        return uporabnikRepository.findById(id).map(uporabnik -> {
            uporabnik.setIme(uporabnikDetails.getIme());
            uporabnik.setPriimek(uporabnikDetails.getPriimek());
            uporabnik.setEnaslov(uporabnikDetails.getEnaslov());
            uporabnik.setGeslo(uporabnikDetails.getGeslo());
            return uporabnikRepository.save(uporabnik);
        }).orElseGet(() -> {
            uporabnikDetails.setId(id);
            return uporabnikRepository.save(uporabnikDetails);
        });
    }

    // Brisenje po id
    @DeleteMapping("/{id}")
    public void deleteUporabnik(@PathVariable Long id) {
        uporabnikRepository.deleteById(id);
    }


    // Like recept
    @PostMapping("/{userId}/like/{receptId}")
    public ResponseEntity<String> likeRecept(@PathVariable Long userId,
                                             @PathVariable Long receptId) {

        Optional<Uporabnik> uporabnikOpt = uporabnikRepository.findById(userId);
        if (uporabnikOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Uporabnik ne obstaja");
        }

        Optional<Recept> receptOpt = receptRepository.findById(receptId);
        if (receptOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recept ne obstaja");
        }

        Uporabnik u = uporabnikOpt.get();
        Recept r = receptOpt.get();

        u.getLikedRecepti().add(r);
        uporabnikRepository.save(u);

        return ResponseEntity.ok("Recept dodan v seznam priljubljenih");
    }
    //Unlike recept
    @DeleteMapping("/{userId}/like/{receptId}")
    public ResponseEntity<String> unlikeRecept(@PathVariable Long userId,
                                               @PathVariable Long receptId) {

        Optional<Uporabnik> uporabnikOpt = uporabnikRepository.findById(userId);
        if (uporabnikOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Uporabnik ne obstaja");
        }

        Optional<Recept> receptOpt = receptRepository.findById(receptId);
        if (receptOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recept ne obstaja");
        }

        Uporabnik u = uporabnikOpt.get();
        Recept r = receptOpt.get();

        if (!u.getLikedRecepti().contains(r)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Recept ni v seznam priljubljenih");
        }

        u.getLikedRecepti().remove(r);
        uporabnikRepository.save(u);

        return ResponseEntity.ok("Recept odstranjen iz seznama priljubljenih");
    }
    //pridobitev vseh priljubljenih receptov uporabnika
    @GetMapping("/{userId}/liked")
    public ResponseEntity<?> getLikedRecepti(@PathVariable Long userId) {
        Optional<Uporabnik> uporabnikOpt = uporabnikRepository.findById(userId);
        if (uporabnikOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Uporabnik ne obstaja");
        }

        Set<Recept> likedRecepti = uporabnikOpt.get().getLikedRecepti();

        if (likedRecepti.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        return ResponseEntity.ok(likedRecepti);
    }


}
