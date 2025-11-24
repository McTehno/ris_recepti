package um.si.feri.ris.vaje.app_za_recepti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import um.si.feri.ris.vaje.app_za_recepti.dao.UporabnikRepository;
import um.si.feri.ris.vaje.app_za_recepti.models.Uporabnik;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/uporabniki")

public class UporabnikController {

    @Autowired

    private UporabnikRepository uporabnikRepository;

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
}
