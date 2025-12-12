# Poročilo o testiranju

## 1. Opis testov

### 1.1 UstvariReceptTest
- **Kaj testira:** preverja ustvarjanje recepta, pravilno dodelitev ID-ja, ime recepta, število in povezavo sestavin.
- **Zakaj je pomembno:** zagotavlja, da se recepti pravilno shranijo v bazo in da so sestavine povezane s pravim receptom.

### 1.2 BrisanjeReceptTest
- **Kaj testira:** preverja brisanje recepta, uspešno sporočilo ob brisanju, ter obravnavo brisanja recepta pri poseg z neobstoječih ID-jev.
- **Zakaj je pomembno:** zagotavlja, da funkcija za brisanje receptov deluje pravilno in ne pusti nezaželenih zapisov v bazi.

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
