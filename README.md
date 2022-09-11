# Quarkus Redis Job Queue Demo

This projects demonstrates how to implement a job queue with the Redis data source API.

## Build

```shell
mvn clean package
```

## Run

You need three terminals.

In the first terminal, run:

```shell
cd supes-application
mvn quarkus:dev
```

Then, hit `h` and then `c` and copy the `quarkus.redis.hosts` address.

In the second and third terminals, run:

```shell
cd fight-simulator
java -Dsimulator-name=A -Dquarkus.redis.hosts=... -jar target/quarkus-app/quarkus-run.jar
```

Change the `quarkus.redis.hosts` value with the one copied above.

Open a browser to http://localhost:8080 and submit the fights.