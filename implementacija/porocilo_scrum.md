## Scrum poročilo o napredku projekta 

## Osnovni podatki
- **Projekt:** Moji Recepti
- **Metodologija:** Scrum
- **Število iteracij:** 1 (en sprint)
- **Orodje za sledenje dela:** GitHub Projects
- **Vrsta table:** Kanban (BackLog/ Ready / In Progress / In Review / Done)

> Opomba: Vaja 9 je bila izvedena v eni iteraciji zaradi omejenega obsega. Kljub temu smo sledili Scrum metodologiji in ohranili vse ključne Scrum elemente (cilj sprinta, backlog, review in retrospektivo). Kanban tabla je bila uporabljena kot operativno orodje za izvajanje sprinta.

---

## Opis
Cilj je bil razširiti funkcionalnost receptov z možnostjo določanja števila porcij ter omogočiti dinamični preračun količin sestavin in pravilen izvoz recepta v PDF obliki. Projekt je bil izveden iterativno v okviru enega sprinta.

---

## Product Backlog
Na začetku projekta smo oblikovali Product Backlog, ki je vseboval naslednjo uporabniško zahtevo:

**User Story:**  
Kot uporabnik želim možnost prilagoditve količin sestavin glede na število porcij, da lahko skuham točno toliko, kot potrebujem.

---

## Sprint planiranje

### Cilj sprinta
Cilj sprinta je bil implementirati podporo za število porcij (st_porcije) skozi celoten sistem – od podatkovne baze do uporabniškega vmesnika in PDF izvoza.

Pri sprint planiranju smo za naloge ocenili koliko časa bo potrebna za njeno izvedbo z metodo Planning Poker. 

### Definition of Done
Naloga je zaključena, ko:
- je funkcionalnost implementirana,
- je naloga povezana z GitHub,
- je pregledana v okviru *In Review*,
- je uspešno prestavljena v *Done* na GitHub tabli.

---

## Sprint Backlog – opis nalog

### Backend naloge

**Dodajanje atributa `st_porcij` v tabelo Recept**
Namen naloge je bil razširiti tabelo Recept z atributom `st_porcij`, ki določa privzeto število porcij recepta.

**Dodajanje atributov `kolicina` in `enota` v podatkovno bazo**
V podatkovno bazo sta bila dodana atributa `kolicina` in `enota`, ki omogočata natančno določanje količin sestavin pri receptih.


### Frontend naloge

**Sprememba komponente `ReceptPodrobnosti.js` za izpis PDF-a s točnimi količinami**
Implementirana je bila logika, ki ob generiranju PDF-a izpiše izračunane količine sestavin glede na trenutni vnos števila porcij.

**Dodajanje vnosnega polja za število porcij znotraj `ReceptPodrobnosti.js`**
V komponento je bilo dodano vnosno polje, ki privzeto prikazuje vrednost iz atributa `st_porcij`. Vnos omogoča spreminjanje porcij in osvežitev izračuna količin sestavin.

**Dodajanje vnosnega polja za število porcij znotraj `ReceptForm.js`**
V obrazec za ustvarjanje ali urejanje recepta je bilo dodano novo vnosno polje, ki omogoča nastavitev privzetega števila porcij.

**Dodajanje vnosnih polj `količina` in `enota` v `ReceptForm.js`**
Obrazec je bil nadgrajen z možnostjo vnosa količine sestavine in enote merske enote (npr. g, ml, žlica).

**Dodajanje dinamičnega izračuna količine sestavin znotraj `ReceptPodrobnosti.js`**
Logika izračuna je bila implementirana tako, da se ob spremembi števila porcij avtomatsko preračunajo količine sestavin in posodobijo prikazane vrednosti.

## Izvedba sprinta
Naloge (taski) so se premikale med stolpci *Backlog*, *Ready*, *In Progress*, *In Review* in *Done* na GitHub Project tabli, kar je omogočalo stalno spremljanje napredka.

---

## Sprint Review
Ob zaključku sprinta smo pregledali:
- uspešno implementirane backend spremembe,
- uspešno implementirane frontend frontend naloge

---

## Sprint Retrospektiva

**Kaj je šlo dobro:**
- Jasno definiran cilj sprinta
- Dobra razdelitev nalog med frontend in backend
- Pregledno sledenje nalogam na GitHub tabli

**Kaj bi izboljšali:**
- Bolj zgodnja uskladitev podatkovnega modela in UI pričakovanj

**Akcijski koraki:**
- V prihodnje pred implementacijo pripraviti kratek dogovor o strukturi podatkov (atributi in primeri vrednosti)

---

## Zaključek
Čeprav je projekt obsegal le eno iteracijo, je bil izveden skladno s Scrum metodologijo. Proces dela, razdelitev nalog in napredek so jasno vidni tako v tem poročilu kot na GitHub Project tabli.
