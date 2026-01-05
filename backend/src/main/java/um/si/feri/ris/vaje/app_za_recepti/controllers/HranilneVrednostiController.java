package um.si.feri.ris.vaje.app_za_recepti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import um.si.feri.ris.vaje.app_za_recepti.dao.HranilneVrednostiRepository;
import um.si.feri.ris.vaje.app_za_recepti.dao.ReceptRepository;
import um.si.feri.ris.vaje.app_za_recepti.models.HranilneVrednosti;
import um.si.feri.ris.vaje.app_za_recepti.models.Recept;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/hranilne-vrednosti")
public class HranilneVrednostiController {

    @Autowired
    private HranilneVrednostiRepository hranilneVrednostiRepository;

    @Autowired
    private ReceptRepository receptRepository;

    // READ – сите
    @GetMapping
    public List<HranilneVrednosti> getAll() {
        return (List<HranilneVrednosti>) hranilneVrednostiRepository.findAll();
    }

    // READ – една
    @GetMapping("/{id}")
    public HranilneVrednosti getById(@PathVariable Long id) {
        return hranilneVrednostiRepository.findById(id).orElse(null);
    }

    // CREATE
    @PostMapping
    public HranilneVrednosti create(@RequestBody HranilneVrednosti hv) {
        return hranilneVrednostiRepository.save(hv);
    }

    // UPDATE
    @PutMapping("/{id}")
    public HranilneVrednosti update(@PathVariable Long id, @RequestBody HranilneVrednosti noviPodatki) {
        Optional<HranilneVrednosti> optional = hranilneVrednostiRepository.findById(id);

        if (optional.isPresent()) {
            HranilneVrednosti hv = optional.get();
            hv.setEnergija(noviPodatki.getEnergija());
            hv.setBjelankovine(noviPodatki.getBjelankovine());
            hv.setOgljikoviHidrati(noviPodatki.getOgljikoviHidrati());
            hv.setMascobe(noviPodatki.getMascobe());
            return hranilneVrednostiRepository.save(hv);
        }
        return null;
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        if (hranilneVrednostiRepository.existsById(id)) {
            hranilneVrednostiRepository.deleteById(id);
            return "Hranilne vrednosti z ID " + id + " so izbrisane.";
        }
        return "Hranilne vrednosti ne obstajajo.";
    }

    // READ – сите за рецепт
    @GetMapping("/recept/{receptId}")
    public List<HranilneVrednosti> getByRecept(@PathVariable Long receptId) {
        return hranilneVrednostiRepository.findByReceptId(receptId);
    }

    // CREATE – додавање на рецепт
    @PostMapping("/recept/{receptId}")
    public HranilneVrednosti addToRecept(
            @PathVariable Long receptId,
            @RequestBody HranilneVrednosti hv) {

        Optional<Recept> receptOptional = receptRepository.findById(receptId);

        if (receptOptional.isPresent()) {
            hv.setRecept(receptOptional.get());
            return hranilneVrednostiRepository.save(hv);
        }
        return null;
    }
}
