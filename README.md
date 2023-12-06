# SimplSrecka

## Mikrostoritev: Transaction

### Funkcionalnosti te mikrostoritve:
* Izplačilo denarja
* Nalaganje denarja
* Spreminjanje stanja


## Zagon in Testiranje

### Prerequisites

```bash
docker run -d --name pg-lottery-ticket -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=lottery-ticket -p 5432:5432 postgres:13
```

### Build and run commands
```bash
mvn clean package
cd api/target
java -jar image-catalog-api-1.0.0-SNAPSHOT.jar
```

