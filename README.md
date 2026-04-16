# Bank Transference API

REST API for a bank transfer system.

## Stack

- Java 21
- Spring Boot 4.0.3
- Spring Security + JWT (jjwt)
- Spring Data JPA + MySQL 8
- Spring WebFlux (WebClient for external calls)
- Springdoc OpenAPI (Swagger UI)
- Lombok
- Testcontainers + JUnit 5 + H2 (tests)
- Docker Compose

## Features

- Customer registration (PF and PJ)
- Authentication with credentials (email/identifier + password)
- Balance consultation and management
- Transfers between clients with authorization via external API (`https://util.devi.tools/api`)
- Notification of transfers

## Project structure

```
src/main/java/com/example/bank_api/
├── boundary/ # Controllers and contracts (request/response DTOs)
├── config/ # Settings (WebClient, exception handler, security)
├──domain/
│ ├── model/ # JPA Entities
│ └── service/ # Business rules (customer, balance, transfer)
└── below/ 
├── gateway/ # Integration with external APIs (authorization, notification) 
└── repository/ # Spring Data Repositories
```