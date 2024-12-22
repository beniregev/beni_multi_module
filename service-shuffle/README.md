# Beni Spring Boot Microservices Demo Project
## Tech Stack

- Java 21 (Latest LTS version as of December 2024)
- Spring Boot 3.4.1
- Spring Web (for MVC)
- Spring DevTools
- Project Lombok
- Maven
- IntelliJ IDEA

## The microservices Application Includes
1. Parent Spring Boot application with a main class. I changed the _pom.xml_ to support a multimodule project.
2. The ***service-log*** microservice, Spring Boot application with a controller, service, and an exception handle classes.
3. The ***service-shuffle*** microservice, Spring Boot application with a controller, service, and an exception handle classes.

## Endpoints
Implemented Swagger with support for OpenAPI v3.0

### Microservice shuffle
- /v1/{size} ==> Create a shuffled array of integers without duplicates in complexity of O(n). Return the suitable HTTP Status code to the result of the API call.
- /v1/ping ==> Ping the service-shuffle server.
- /v1/ping-service => Ping the service-shuffle service layer.

Swagger UI supporting OpenAPI v3.0 URL is: `{protocol}://{host}:{port}/{path}/swagger-ui/index.html`
For example, for service-shuffle:
- protocol=http
- host-localhost
- port=8091 (`server.port` property in ***application.properties*** file)
- path=api/shuffle
- Swagger-UI URL will be: `http://localhost:8091/api/shuffle/swagger-ui/index.html`

### Microservice shuffle
- /v1/shuffledArray Log ==> the request body from `service-shuffle` and return suitable HTTP Status code to the result of the API call. The request body is `application/json` and must contain 3 properties: ***sending***, ***logLevel***, and ***message***. I added the _sending_ property to distinct the Sync API call from the Async one. There's no validation on the value as it's something I added and not in the scope of the demo.
- /v1/ping ==> Ping the service-shuffle server.
- /v1/ping-service ==> Ping the service-log service layer.

Swagger UI supporting OpenAPI v3.0 URL is: `{protocol}://{host}:{port}/{path}/swagger-ui/index.html`
For example, for service-shuffle:
- protocol=http
- host-localhost
- port=8092 (`server.port` property in ***application.properties*** file)
- path=api/log
- Swagger-UI URL will be: `http://localhost:8092/api/log/swagger-ui/index.html`

## Running And Testing The Microservices
Using IntelliJ IDEA in the "Run/Debug Configuration" I created:
- Maven `mvn clean install` entry for each Microservice.
- Run/Debug Application entry for each microservice.
- Compound entry to run all the microservices

I used the compound entry that I created to run the Microservices,
the browser for GET requests (`ping` and `ping-service`) and ***cURL***
to test the GET and POST requests.

    //  Ping servive-shuffle server and service
    curl -X GET http://localhost:8091/api/shuffle/v1/ping
    curl -X GET http://localhost:8091/api/shuffle/v1/ping-service
    
    //  Ping servive-log server and service
    curl -X GET http://localhost:8092/api/log/v1/ping
    curl -X GET http://localhost:8092/api/log/v1/ping-service

    //  The follwing 2 commands will be executed successfully
    curl -X POST http://localhost:8091/api/shuffle/v1/20
    curl -X POST http://localhost:8091/api/shuffle/v1/15

    //  The following command should result with an error:
    curl -X POST http://localhost:8091/api/shuffle/v1/1001  

I run the first 2 commands put a breakpoint in the LogController POST
request and changed the `body`:
- Empty the body left an empty string.
- Deleted the ***sending***, ***logLevel***, and ***message*** properties not at the same time and also at the same time.

## Objective Reached
- Spring Boot microservices application created with 2 microservices (service-shuffle, service-log).
- Microservice `service-shuffle` has a POST endpoint to generate a shuffled array of integers in the range of 1 to `size` of the array without duplicates
- Generating shuffled array of integers in performed with complexity of O(n) -- **BONUS ACHIEVED**.
- Microservice `service-log` has a POST endpoint to LOG an object received in the POST request body.
- Microservice `service-shuffle` makes a POST request call to service-log microservice to log the shuffled array.
- Microservice `service-shuffle` makes a POST request call the service-log microservice in asynchronously -- **BONUS ACHIEVED**.
- service-log adds a key-value to the object (JSON) that is returned to the client.

## Reference Documentation
For further Reference please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.1/maven-plugin)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.4.1/reference/using/devtools.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.1/reference/web/servlet.html)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the
parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

