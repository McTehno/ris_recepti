# Poročilo o testiranju

## 1. Opis testov

### 1.1 UstvariReceptTest
- **Kaj testira:**
  - **testCreateRecept_IdAssigned:** Preverja, ali se ID pravilno dodeli ob ustvarjanju recepta.
  - **testCreateRecept_ImeCorrect:** Preverja, ali se ime recepta pravilno shrani.
  - **testCreateRecept_SestavineCount:** Preverja, ali so se vse sestavine shranili.
  - **testCreateRecept_SestavineLinked:** Preverja, ali so sestavine pravilno povezane z receptom.
  - **testCreateRecept_BrezSestavin:** Negativni test; preverja, da metoda ne dovoli ustvariti recepta brez sestavin.

- **Zakaj je pomembno:** zagotavlja, da se recepti pravilno shranijo v bazo in da so sestavine povezane s pravim receptom.

### 1.2 BrisanjeReceptTest
- **Kaj testira:**
  - **testDeleteRecept_SuccessMessage:** Preverja, ali metoda pravilno vrne sporočilo ob uspešnem brisanju recepta.
  - **testDeleteRecept_Success:** Preverja, ali je recept po klicu metode dejansko odstranjen iz baze.
  - **testDeleteRecept_NotFound:** Preverja, da metoda pravilno ravna z neobstoječim ID-jem in vrne ustrezno sporočilo.
- **Zakaj je pomembno:** Zagotavlja, da funkcija za brisanje receptov deluje pravilno, se lahko izbriše samo obstoječi recept in ne pusti nezaželenih zapisov v bazi. Prav tako metoda vrne ustrezno sporočilo tudi, če ID ne obstaja.

### 1.3 KomentiranjeReceptaTest
- **Kaj testira:**
  - **ustvariKomentar_Obstaja:** Preverja, da se komentar uspešno ustvari, da vsebuje dodeljen id in da komentar dejansko obstaja v bazi.
  - **ustvariKomentar_VsebinainRelacije:** Preverja, da se vsebina komentarja pravilno shrani in da so v odgovoru pravilno nastavljeni uporabnikId ter receptId.
  - **ustvariKomentar_BrezVsebine:** Preverja, da kontroler zavrne komentar brez vsebine (prazno besedilo / presledki) z napako BAD_REQUEST, ter da se komentar ne shrani v bazo.
  - **ustvariKomentar_NiUporabnika:** Preverja, da ob neobstoječem uporabniku (neveljaven uporabnikId) kontroler vrne napako NOT_FOUND, ter da se komentar ne shrani.
  - **ustvariKomentar_NiRecepta** Preverja, da ob neobstoječem receptu (neveljaven receptId) kontroler vrne napako NOT_FOUND, ter da se komentar ne shrani v bazo.
  - **ustvariKomentar_NullRequest** Preverja da ob null requestu kontroller vrne napako BAD_REQUEST, ter da se komentar ne shrani v bazo.
- **Zakaj je pomembno:** zagotavlja, da se komentarji ustvarjajo pravilno, da so povezani s pravilnim uporabnikom in receptom, ter da backend pravilno validira vhodne podatke in ne omogoča shranjevanja neveljavnih komentarjev.

### 1.4 OcenjevanjeReceptaTest
- **Kaj testira:**
  - **dodajOcenoUspesno:** Preverja, ali se ocena uspešno doda receptu in poveže z uporabnikom.
  - **posodobiOcenoUspesno:** Preverja, ali se obstoječa ocena posodobi, če uporabnik ponovno oceni isti recept.
  - **dodajOcenoNeuspesnoNiUporabnika:** Preverja, da se ocena ne shrani, če uporabnik ne obstaja (vrne 404).
- **Zakaj je pomembno:** Zagotavlja, da lahko uporabniki ocenjujejo recepte, da se ocene pravilno shranjujejo in posodabljajo ter da sistem preprečuje ocenjevanje s strani neobstoječih uporabnikov.

### 1.5 PridobiOcenoTest
- **Kaj testira:**
  - **vrniMojoOcenoObstaja:** Preverja, ali metoda vrne pravilno oceno, če je uporabnik recept že ocenil.
  - **vrniMojoOcenoNeObstaja:** Preverja, ali metoda vrne 0, če uporabnik recepta še ni ocenil.
  - **vrniMojoOcenoNeobstojecRecept:** Preverja, ali metoda vrne 0, če recept ne obstaja.
- **Zakaj je pomembno:** Omogoča uporabniku vpogled v svojo oceno za določen recept in pravilno obravnava primere, ko ocena ali recept ne obstajajo.
---

## 2. Člani skupine in odgovornosti
| Ime člana      | Odgovornost                                      |
|----------------|-------------------------------------------------|
| Nina Stevoska  | Testiranje ustvarjanje in brisanje receptov |
| Maša Oblonšek | Testiranje komentiranje recepta            |
| Mihajlo Dejanovič | Testiranje ocenjevanje recepta            |

---

## 3. Analiza uspešnosti testov
- **Ustvarjanje recepta:** testi so pokazali, da se recept ustrezno ustvari pri pravilnih vnosih, da se ID shrani pravilno, ime je pravilno in sestavine so pravilno povezane z receptom. Zagotovili smo tudi, da se recept ne more ustvariti brez vsaj ene sestavine.
Pri prvem testiranju je test padel, ker smo dovolili, da se recept ustvari brez sestavin. Kodo smo popravili z ustreznim preverjanjem, da recept vsebuje vsaj eno sestavino, preden se shrani, da ne bi nastajali nepotrebni zapisi v bazi.

- **Brisanje recepta:** testi so pokazali, da je možno izbrisati samo obstoječi recept in da metoda vrne ustrezno sporočilo o brisanju. Tudi ob poskusu brisanja recepta z neobstoječim ID-jem metoda vrne ustrezno obvestilo. 

- **Komentiranje recepta:** pri pripravi testov ustvarjanja komentarjev testi sprva niso padali, so pa pokazali pomanjkljivosti v implementaciji kontrolerja za ustvarjanje komentarjev: pri napačnih podatkih je vračal null in ni jasno ločil med napakami (neobstoječ recept/uporabnik, neveljavna vsebina).Kontroler smo izboljšali tako, da zdaj ob napakah vrne ResponseStatusException z ustreznimi statusi in preverja tudi isBlank() za vsebino. Teste komentiranja smo prilagodili novi logiki in dodali preverjanje count(), da ob napaki ne pride do shranjevanja komentarja v bazo.

- **Ocenjevanje recepta:** Testi so potrdili, da funkcionalnost ocenjevanja deluje pravilno. Uspešno smo preverili dodajanje novih ocen, posodabljanje obstoječih ocen (kjer se preveri, da en uporabnik ne more imeti več ocen za isti recept) in pridobivanje ocene za določenega uporabnika. Tudi smo pokrili druge primere, kot so ocenjevanje s strani neobstoječega uporabnika ali pridobivanje ocene za neobstoječ recept, kjer sistem pravilno vrne ustrezne statusne kode ali privzete vrednosti.
---

## 4. Zaključek
- Vsi testi so uspešno izvedeni.
- Testi so potrdili stabilnost funkcionalnosti ustvarjanja, brisanja, komentiranja in ocenjevanja receptov.
- Dokumentacija testov bo koristna za prihodnje vzdrževanje kode.
