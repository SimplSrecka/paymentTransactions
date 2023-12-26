## Transaction

### Funkcionalnosti te mikrostoritve:
* Pridobi vse transakcije
* Pridobi vse transakcije izbranega uporabnika
* Dodaj novo transakcijo


#### API dokumentacija je na voljo [tukaj](http://52.226.192.46/transaction/openapi)
#### API Swagger (UI) je na voljo [tukaj](http://52.226.192.46/transaction/api-specs/ui/?url=http://52.226.192.46/transaction/openapi&oauth2RedirectUrl=http://52.226.192.46/transaction/api-specs/ui/oauth2-redirect.html)

## Zagon in Testiranje

### Predpogoj je PostgreSQL baza

```bash
docker run -d --name pg-lottery-ticket -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=lottery-ticket -p 5432:5432 postgres:13
```

### Sestavljanje in zagon ukazov
```bash
mvn clean package
cd api/target
java -jar transaction-api-1.0.0-SNAPSHOT.jar
```
Aplikacija je dostopna na naslovu: localhost:8082