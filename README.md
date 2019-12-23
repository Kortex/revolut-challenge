## Revolut Technical Challenge Application

This simple project is meant to fulfil the requirements set by the **Backend Engineer** technical challenge.

As mentioned in the respective document its purpose is to be a simple application that allows for transferring
funds between bank accounts.

The technology stack of the project consists of the project consists of the following components:

* RedHat's [Quarkus](https://quarkus.io/) framework
* [PostgreSQL](https://www.postgresql.org/) as the database provider
* [JUnit5](https://junit.org/junit5/) as a testing framework

Usage of complicated and heavy frameworks (such as Hibernate) has been avoided in order to keep the code simple and 
lightweight. A good example of this is the fact that database interaction is achieved through plain JDBC.

## Getting started

### Prerequisites

The following are assumed to be in place for running, developing and testing the application:

* [AdoptOpenJDK's](https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=openj9) Java JDK (version 11.0.5+10) preferably with the OpenJ9 JVM.
* Local Postgres installation or a running Postgres container (one can be fetched from [here](https://hub.docker.com/_/postgres)).
* A database along with a user having the name and credentials as these can be under the `application.properties` file.

### Building, running and testing the project

To build the project just the run the following command using the Maven wrapper:

`./mvnw clean compile`

Running the project also involves using the Maven wrapper and can be done with the following command:

`./mvnw quarkus:dev`

- This will effectively start the project's web server binding to `http://localhost:8080`
- Note that the first time the application runs `Flyway` will perform a schema creation and will seed data.

Testing the project can be done by using the following command:

`./mvnw test`

### Creating Dockerized versions of the application.

The project offers the option to easily create dockerized versions of the application, using both a standard JVM as well
as the a GraalVM native image.

#### Creating a JVM based docker image:

To create a JVM based docker image the following steps need to be followed:

* Build and package the project using

```./mvnw package```

* Creating the docker image using:

```docker build -f src/main/docker/Dockerfile.jvm -t quarkus/revolut-challenge-jvm .```

* Spin off a docker container using:

```docker run -i --rm -p 8080:8080 quarkus/revolut-challenge-jvm```

**NOTE**

- Additional configuration may be required to run a dockerized version of the project (such as Docker network bridging etc).

### Using the application

To use the application just start the server. To do so an issue and convenient way is to build the docker image and use 
the `docker-compose` file found under `/src/main/docker`. The steps to do so are:

* From the project's root directory run:

```shell script
./mvnw clean compile test verify package  
```

* The build the image using:

```shell script
docker build -f src/main/docker/Dockerfile -t quarkus/revolut-challenge-jvm .
```

* Finally, perform the following to start the containers in daemon mode:

```shell script
cd src/main/docker
docker-compose up -d
```

You can then perform calls using cURL with a command similar to: 

```shell script
curl -X POST "http://localhost:8080/accounts/transfer" \
-H  "accept: */*" -H  "Content-Type: application/json" \
-d "{\"fromAccount\":\"42a238e1-a020-4ef2-a6dd-ab590f35ef12\",\"toAccount\":\"7c74d1d9-99f7-48b5-85b6-1397902dde17\",\"amount\":4000}"
```

To get a full overview of the API offered by the application you can visit the following link:

`http://localhost:8080/accounts-api`

Note that the above is only available when the application runs with the `dev` profile