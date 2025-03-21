# Dokumentatsioon
Rakenduse algusest lõpuni arenduseks kulus kokku umbes 15 tundi. Abivahendina kasutasin ChatGPT tasuta versiooni, ning koodijupid, mis on ChatGPT pool genereeritud, on kommentaariga "Generated by ChatGPT".

## Mõtted ja otsused enne arendamise algust
Kuna hetkel on minu suureks nõrkuseks front-end arendus, siis otsustasin panna alustuseks rõhku REST API kirjutamisele. API meetodid on nähtavad failis FlightRestController. 
API kirjutamisel võtsin palju eeskuju oma ühest varasemast projektist "finance-tracker", mis on nähtav minu GitHubis.
Otsisin alguses internetist avalikku API-t, mida kasutada, kuid ainus võimalus, mis leidsin pakkus 100 API päringut kuus, seega otsustasin luua ise enda API.

## Kasutatud tehnoloogiad
- Projekti tööriistaks otsustain kasutada Mavenit, sest sellega on mul varem kogemust ja paistab pealtnäha lihtsam, kui Gradle.
- Andmebaasi lahenduseks valisin H2 Database, sest see on kõige lihtsam antud rakenduse jaoks ja ei nõua välist andmebaasi ühendust.
- Front-end jaoks kasutasin Thymeleaf, sest see sobib hästi Java Spring projektideks ning on lihtne kasutada.

## Katsumused ja probleemid
- Kõige keerulisem minu jaoks oli front-end poole kirjutamine, sest mul puudub varasem kogemus Thymeleaf-iga ja teiste raamistikega. Sel põhjusel on ka rakenduse visuaalne pool väga algeline ning vajas väga palju ChatGPT abi.
- Keeruliseks osutus ka istmete valimine ja soovitamine. Otsustasin standardiseerida istmekonfiguratsiooni järgmiselt:
1. Lennukites on üks vahekäik ja sellest mõlemale poole jääb kindel arv istmeid, mida saab vabalt valida (formaadis 5-5, 6-6, jne)
2. Igas istmeplaanis on kindel arv ridu. (klassi FlightService meetodis generateSeating() saab muuta ridade arvu, vaikimisi 20 rida)
3. Istmeplaani hoitakse String[ridade arv][istmete arv reas] andmestruktuuris, kus igal istmel on vastav kood. Iga rida eristab täht ning tähele järgneb istme number reas, näiteks esimese rea esimene iste on A1, teise rea kolmas iste B3, jne.
- Ideaalis oleksin soovinud kasutada välist API-t andmete kogumiseks, mis oleks lubanud mul keskenduda täielikult rakenduse funktsionaalsusele ja visuaalsele poolele. 100 päringust oleks kahjuks väheks jäänud.
- Pärast API kirjutamist avastasin, et Thymeleaf jaoks ei olnud tarvis kirjutada REST API-t, aga sellegipoolest oli palju abi FlightService klassist, mida mõlemad (FlightRestController ja FlightViewController) kasutasid.
- Minu lahenduse juures oleks olnud keeruline lisada istekohtadele erinevaid klasse. Arvan, et selle jaoks oleks olnud mõistlik luua eraldi andmebaasi olem SEATING_PLAN ja ühendada see olemiga FLIGHT.
- Ümberistumise toetamist oleksin saanud lisada, kui kasutada reaalseid andmeid välise API kaudu. Enda lahenduse juures ilmselt oleksin pidanud lähteandmetesse (data.sql) lisama ise selliseid lende, mida oleks saanud ühendada ümberistumistega. Sellisel juhul oleks olnud võimalik lisada otsingufunktsioon, kus kasutaja saab otsida lende ühest lennujaamast teise.
- Konteinerisatsiooni kogemus mul hetkel kahjuks puudus, mistõttu ei osanud ma ka rakendust Dockeri konteinerisse paigutada.
