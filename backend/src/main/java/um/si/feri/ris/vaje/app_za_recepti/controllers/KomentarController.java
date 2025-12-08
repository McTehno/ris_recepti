package um.si.feri.ris.vaje.app_za_recepti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import um.si.feri.ris.vaje.app_za_recepti.dao.KomentarRepository;
import um.si.feri.ris.vaje.app_za_recepti.dao.ReceptRepository;
import um.si.feri.ris.vaje.app_za_recepti.dao.UporabnikRepository;
import um.si.feri.ris.vaje.app_za_recepti.models.Komentar;
import um.si.feri.ris.vaje.app_za_recepti.models.Recept;
import um.si.feri.ris.vaje.app_za_recepti.models.Uporabnik;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class KomentarController {

    @Autowired
    private KomentarRepository komentarRepository;
    @Autowired
    private ReceptRepository receptRepository;

    @Autowired
    private UporabnikRepository uporabnikRepository;

    public static class KomentarRequest {
        public Long uporabnikId;
        public String vsebina;
    }
    public record KomentarResponse(
             Long id,
            String vsebina,

            Long uporabnikId,
            String ime,
            Long receptId
    ) {}

    @PostMapping("/recept/{receptId}/ustvariKom")
    public KomentarResponse ustvariKomentar(@PathVariable Long receptId, @RequestBody KomentarRequest request) {
        Optional<Uporabnik> uporabnik = uporabnikRepository.findById(request.uporabnikId);
        Optional<Recept> recept = receptRepository.findById(receptId);

        if (recept.isPresent() && uporabnik.isPresent()) {
            Komentar komentar = new Komentar();
            komentar.setUporabnik(uporabnik.get());
            komentar.setRecept(recept.get());
            komentar.setVsebina(request.vsebina);

            //komentarRepository.save(komentar);
             Komentar shranjenKomentar =komentarRepository.save(komentar);
            return new KomentarResponse(shranjenKomentar.getId(), shranjenKomentar.getVsebina(), shranjenKomentar.getUporabnik().getId(), shranjenKomentar.getUporabnik().getIme(),shranjenKomentar.getRecept().getId());
        }
        return null;
    }
    @DeleteMapping("/recept/{receptId}/izbrisi/{idKom}")
    public void izbrisiKomentar(@PathVariable Long receptId, @PathVariable Long idKom) {
       boolean obstajaRecept = receptRepository.existsById(receptId);
       boolean obstajaKom = receptRepository.existsById(idKom);
       if (obstajaRecept && obstajaKom) {
           komentarRepository.deleteById(idKom);
       }

    }
    @GetMapping("/recept/{idRecepta}/komentarji")
    public List<KomentarResponse> komentarji(@PathVariable Long idRecepta) {
       Optional<Recept> recept = receptRepository.findById(idRecepta);

       if (recept.isPresent()) {
           List<KomentarResponse> komentarResponses = new ArrayList<>();
           List<Komentar> komentarji =komentarRepository.findByRecept(recept.get());
           komentarji.forEach(komentar -> komentarResponses.add(new KomentarResponse(komentar.getId(), komentar.getVsebina(), komentar.getUporabnik().getId(), komentar.getUporabnik().getIme(), komentar.getRecept().getId())));
            return komentarResponses;
       }
       return List.of();

    }
}
