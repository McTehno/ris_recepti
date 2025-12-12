package um.si.feri.ris.vaje.app_za_recepti.controllers;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import um.si.feri.ris.vaje.app_za_recepti.dao.ReceptRepository;
import um.si.feri.ris.vaje.app_za_recepti.dao.UporabnikRepository;
import um.si.feri.ris.vaje.app_za_recepti.models.Recept;
import um.si.feri.ris.vaje.app_za_recepti.models.Sestavina;
import um.si.feri.ris.vaje.app_za_recepti.models.Uporabnik;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/recepti")
public class ReceptController {

    @Autowired
    ReceptRepository receptRepository;

    @Autowired
    UporabnikRepository uporabnikRepository;

    // pridobivanje vseh receptov
    @GetMapping
    public List<Recept> getAllRecepti() {
        return (List<Recept>) receptRepository.findAll();
    }

    // pridobivanje vseh receptov po ime
    @GetMapping("/ime/{ime}")
    public List<Recept> getAllRecepti(@PathVariable String ime) {
        return (List<Recept>) receptRepository.findByImeContaining(ime);
    }

    // ustvarjanje novega recepta
    @PostMapping("/post")
    public Recept createRecept(@RequestBody Recept recept) {
        // dodano da se recept ne more ustvariti brez sestavine
        if (recept.getSestavine() == null || recept.getSestavine().isEmpty()) {
            return null;
        }

        for (Sestavina s : recept.getSestavine()) {
            s.setRecept(recept);
        }
        return receptRepository.save(recept);
    }


    // Podrobnosti enega recepta
    @GetMapping("/{id}")
    public Recept getReceptById(@PathVariable Long id) {
        Optional<Recept> recept = receptRepository.findById(id);
        return recept.orElse(null);
    }
    @PutMapping("/{id}")
    @Transactional
    public Recept updateRecept(@PathVariable Long id, @RequestBody Recept noviPodatki) {
        //naogajne na receptot preku id
        Optional<Recept> receptOptional = receptRepository.findById(id);

        if (receptOptional.isPresent()) {
            Recept recept = receptOptional.get();
            recept.setIme(noviPodatki.getIme());
            recept.setPriprava(noviPodatki.getPriprava());
            recept.setTip(noviPodatki.getTip());

            //ako sostojki ne se podadeni da se inicijaliziret
            if (recept.getSestavine() == null) {
                recept.setSestavine(new ArrayList<>());
            } else {
                //gi brise postoeckite
                recept.getSestavine().clear();
            }
            //sekoja sostojka se povrzuva so recept, i na receptot se dodava
            if (noviPodatki.getSestavine() != null) {
                for (Sestavina s : noviPodatki.getSestavine()) {
                    s.setRecept(recept);
                    recept.getSestavine().add(s);
                }
            }
            //go zacuvuva receptot
            return receptRepository.save(recept);
        } else {
            return null;
        }
    }


    // Brisanje recepta po id
    @DeleteMapping("/{id}")
    public String deleteRecept(@PathVariable Long id) {
        if (receptRepository.existsById(id)) {
            receptRepository.deleteById(id);
            return "Recept z ID " + id + " je bil izbrisan.";
        } else {
            return "Recept z ID " + id + " ne obstaja.";
        }
    }

    //Pridobivanje receptov uporabnika
    @GetMapping("/uporabnik/{id}")
    public List<Recept> getReceptiUporabnika(@PathVariable Long id) {
        Optional<Uporabnik> uporabnikOpt = uporabnikRepository.findById(id);

        if (uporabnikOpt.isPresent()) {
            Uporabnik uporabnik = uporabnikOpt.get();
            return receptRepository.findByUporabnik(uporabnik);
        } else {
            return List.of();
        }
    }
    //Pridobivanje receptov uporabnika po imenu recepta
    @GetMapping("/uporabnik/{id}/ime/{ime}")
    public List<Recept> getReceptiUporabnikaPoImenu(@PathVariable Long id, @PathVariable String ime) {
        return uporabnikRepository.findById(id)
                .map(u -> receptRepository.findByUporabnikAndImeContaining(u, ime))
                .orElse(List.of());
    }


}