
# Projekt: Aplikacija za recepte (RIS)
Ta rešitev omogoča uporabnikom enostavno ustvarjanje, iskanje, urejanje in brisanje lastnih receptov, vključno z dinamičnim upravljanjem sestavin. 
Celoten sistem deluje kot moderna digitalna kuharska knjiga, ki povezuje ljudje skozi recepte.

## Dokumentacija za razvijalce


Obstajata dva dela:

**1)/backend**
-src/main/java/um/si/feri/ris/vaje/app_za_recepti: Glavna izvorna koda Java.
  controllers: Vsebuje Spring REST krmilnike (npr. ReceptController, UporabnikController), ki definirajo API končne točke.
  models: Vsebuje JPA entitete (npr. Recept, Sestavina), ki predstavljajo tabele v bazi podatkov.
  dao: Vsebuje Spring Data JPA repozitorije (npr. ReceptRepository) za dostop do podatkov.
-pom.xml: Maven datoteka, ki upravlja z odvisnostmi in gradnjo projekta.
-src/main/resources/application.properties: Konfiguracijska datoteka za Spring Boot (vključno s povezavo do baze podatkov).

**2)/my-app**: Vsebuje sprednjo (client-side) kodo.
-public: Vsebuje statične datoteke in index.html.
-src: Glavna izvorna koda za React.
  pages: Vsebuje komponente, ki predstavljajo posamezne strani (npr. Domov.js, VsiRecepti.js).
  services/api.js: Vsebuje logiko za komunikacijo z zalednim API-jem (z uporabo knjižnice axios).
  App.js: Glavna komponenta aplikacije, ki skrbi za usmerjanje (routing).
-package.json: Datoteka, ki upravlja z odvisnostmi (npm) in skriptami za sprednji del.


## Uporabljena orodja, frameworki in različice:

**1)Backend**
-Java (Jezik)
-Spring Boot (REST API)
-Spring Data JPA (Dostop do podatkovne baze)
-Maven (Upravljanje odvisnosti in gradnja jave)
-MySQL (Docker podatkovna baza)

**2)Frontend**
-React (UI)
-React Router (Navigacija)
-Axios (backend komunikacija skozi HTTP)
-Node.js (Okolje in packet manager za JavaScript)

**3)Docker (zagon MySQL baze)**


## Standardi kodiranja:

**Java in Spring Boot** - Razredi PascalCase, metode in spremenljivke camelCase.

**React in JavaScript** - Komponente PascalCase, ostali JS camelCase.

Osnovni paket aplikacije je **um.si.feri.ris.vaje.app_za_recepti.** Znotraj tega paketa se nahaja:
**AppZReceptiApplication.java**: To je glavna vstopna točka (entry point) aplikacije. Ta datoteka vsebuje main metodo, ki zažene celotno Spring Boot aplikacijo.

Struktura je nato razdeljena na naslednje ključne podpakete:
**/models**: Ta paket vsebuje podatkovne modele (entitete). To so Java razredi (POJO), ki z anotacijami @Entity predstavljajo strukturo tabel v podatkovni bazi. Po naši konvenciji se imenujejo Ime.java (npr. Recept.java, Uporabnik.java in Sestavina.java).
**/dao (Data Access Object)**: Tukaj so definirani vmesniki (Repositories) za dostop do podatkov. Ti vmesniki, ki sledijo naši konvenciji ImeVmesnikaRepository.java (npr. ReceptRepository.java), razširjajo Spring Data JPA vmesnike (kot je CrudRepository) in s tem avtomatizirajo izvajanje SQL poizvedb.
/controllers: Ta paket vsebuje krmilnike (Controllers), ki definirajo REST API vmesnik naše aplikacije. Vsak krmilnik, poimenovan po vzorcu ImeController.java (npr. ReceptController.java), z anotacijami, kot je @RestController, sprejema HTTP zahteve, komunicira s poslovno logiko (ali neposredno z repozitoriji) in vrača odgovore odjemalcu (npr. v formatu JSON).


## Tehnologije in verzije

Potrebujete Docker, Npm (node.js), Java JDK 23 (ali visje) in Apache Maven.

**Koraki:**

**1)Zagon podatkovne baze (Docker)**
V terminalu zaženite naslednji ukaz, da ustvarite in zaženete Docker vsebnik z MySQL bazo:

docker run -d --name ris-mysql-db -p 3369:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=ris mysql:latest

**2)Namestitev odvisnosti za sprednji del (Frontend)**
Pomaknite se v mapo my-app in namestite potrebne npm pakete:

npm install

**3)Zagon zalednega dela (Backend)**
Odprite nov terminal. Pomaknite se v mapo backend in zaženite Spring Boot aplikacijo z Mavenom:

./mvnw.cmd spring-boot:run

**4)Zagon sprednjega dela (Frontend)**
V terminalu, kjer ste v mapi my-app (iz koraka 2), zaženite React razvojni strežnik:

npm start


## Za razvijalce

Če želite prispevati k projektu:
**1)Naredite Fork projekta.**

**2)Ustvarite novo vejo (Branch)**, kjer boste delali na projektu:

git checkout -b ime-vase-funkcionalnosti

**3)Svoje spremembe commit-ajte in dodajte (add), in potem potisnite (push) na svojo vejo**:

git add spremenjene-datoteke (ali . za vse)
git commit -m "jasno sporočilo o spremembi"
git push origin ime-vase-funkcionalnosti
 
**4)Naredite Pull Request na GitHub-u z opisom in razlago sprememb vaše veje (`ime-vase-nove-funkcionalnosti`) v glavno vejo originalnega projekta (main), potem pa čakajte na odgovor.**
