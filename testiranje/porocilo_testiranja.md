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


---

## 4. Zaključek
- Vsi testi so uspešno izvedeni.
- Testi so potrdili stabilnost funkcionalnosti ustvarjanja, brisanja, komentiranja in ocenjevanja receptov.
- Dokumentacija testov bo koristna za prihodnje vzdrževanje kode.
