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

### Microservice shuffle

### Microservice shuffle

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.1/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.1/maven-plugin/build-image.html)
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

