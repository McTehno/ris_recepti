package um.si.feri.ris.vaje.app_za_recepti.controllers;


import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import um.si.feri.ris.vaje.app_za_recepti.dao.ReceptRepository;
import um.si.feri.ris.vaje.app_za_recepti.dao.SestavinaRepository;
import um.si.feri.ris.vaje.app_za_recepti.models.Recept;
import um.si.feri.ris.vaje.app_za_recepti.models.Sestavina;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/api/sestavine")
public class SestavinaController {

    @Autowired
    private SestavinaRepository sestavinaRepository;

    @Autowired
    private ReceptRepository receptRepository;

    // Pridobivanje vseh
    @GetMapping
    public List<Sestavina> getAllSestavine() {
        return (List<Sestavina>) sestavinaRepository.findAll();
    }
    // Pridobivanje ene
    @GetMapping("/{id}")
    public Sestavina getSestavinaById(@PathVariable Long id) {
        Optional<Sestavina> sestavina = sestavinaRepository.findById(id);
        return sestavina.orElse(null);
    }

    // Ustvarjanje nove
    @PostMapping("/post")
    public Sestavina createSestavina(@RequestBody Sestavina sestavina) {
        return sestavinaRepository.save(sestavina);
    }


    // Spreminjanje obstojece
    @PutMapping("/{id}")
    public Sestavina updateSestavina(@PathVariable Long id, @RequestBody Sestavina noviPodatki) {
        Optional<Sestavina> sestavinaOptional = sestavinaRepository.findById(id);
        if (sestavinaOptional.isPresent()) {
            Sestavina sestavina = sestavinaOptional.get();
            sestavina.setIme(noviPodatki.getIme());
            sestavina.setRecept(noviPodatki.getRecept());
            return sestavinaRepository.save(sestavina);
        } else {
            return null;
        }
    }

    // Brisanje obstojece
    @DeleteMapping("/{id}")
    public String deleteSestavina(@PathVariable Long id) {
        if (sestavinaRepository.existsById(id)) {
            sestavinaRepository.deleteById(id);
            return "Sestavina z ID " + id + " je bila izbrisana.";
        } else {
            return "Sestavina z ID " + id + " ne obstaja.";
        }
    }

    // READ - сите состојки за даден рецепт
    @GetMapping("/recept/{receptId}")
    public List<Sestavina> getAllSestavineByRecept(@PathVariable Long receptId) {
        return sestavinaRepository.findByReceptId(receptId);
    }
    // CREATE - додавање состојка на конкретен рецепт
    @PostMapping("/recept/{receptId}")
    public Sestavina addSestavinaToRecept(@PathVariable Long receptId, @RequestBody Sestavina sestavina) {

        Optional<Recept> receptOptional = receptRepository.findById(receptId);
        if (receptOptional.isPresent()) {
            sestavina.setRecept(receptOptional.get());
            return sestavinaRepository.save(sestavina);
        } else {
            return null;
        }
    }
}
