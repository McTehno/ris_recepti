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

    // GET vse mozne
    @GetMapping
    public List<HranilneVrednosti> getAll() {
        return (List<HranilneVrednosti>) hranilneVrednostiRepository.findAll();
    }

    // GET
    @GetMapping("/{id}")
    public HranilneVrednosti getById(@PathVariable Long id) {
        return hranilneVrednostiRepository.findByReceptId(id);
    }

    // CREATE –ustvarjam novi
    @PostMapping
    public HranilneVrednosti create(@RequestBody HranilneVrednosti hv) {
        return hranilneVrednostiRepository.save(hv);
    }

    // UPDATE
    @PutMapping("/{id}")
    public HranilneVrednosti update(@PathVariable Long id, @RequestBody HranilneVrednosti noviPodatki) {
        return hranilneVrednostiRepository.findById(id)
                .map(hv -> {
                    hv.setEnergija(noviPodatki.getEnergija());
                    hv.setBjelankovine(noviPodatki.getBjelankovine());
                    hv.setOgljikoviHidrati(noviPodatki.getOgljikoviHidrati());
                    hv.setMascobe(noviPodatki.getMascobe());
                    return hranilneVrednostiRepository.save(hv);
                })
                .orElse(null);
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

    // GET za recept po id
    @GetMapping("/recept/{receptId}")
    public HranilneVrednosti getByRecept(@PathVariable Long receptId) {

        return hranilneVrednostiRepository.findByReceptId(receptId);
    }

    // CREATE
    @PostMapping("/recept/{receptId}")
    public HranilneVrednosti createForRecept(
            @PathVariable Long receptId,
            @RequestBody HranilneVrednosti hv) {

        Optional<Recept> receptOptional = receptRepository.findById(receptId);

        if (receptOptional.isPresent()) {
            Recept recept = receptOptional.get();

            // ce ze ima je napaka
            if (recept.getHranilneVrednosti() != null) {
                return null;
            }

            hv.setRecept(recept);
            recept.setHranilneVrednosti(hv);
            return hranilneVrednostiRepository.save(hv);
        }
        return null;
    }

    // UPDATE – za posebni recept azuriranje
    @PutMapping("/recept/{receptId}")
    public HranilneVrednosti updateForRecept(
            @PathVariable Long receptId,
            @RequestBody HranilneVrednosti hv) {

        Optional<Recept> receptOptional = receptRepository.findById(receptId);

        if (receptOptional.isPresent()) {
            Recept recept = receptOptional.get();
            HranilneVrednosti existingHV = recept.getHranilneVrednosti();

            if (existingHV != null) {
                existingHV.setEnergija(hv.getEnergija());
                existingHV.setBjelankovine(hv.getBjelankovine());
                existingHV.setOgljikoviHidrati(hv.getOgljikoviHidrati());
                existingHV.setMascobe(hv.getMascobe());
                return hranilneVrednostiRepository.save(existingHV);
            } else {
                return null;
            }
        }
        return null;
    }

}
