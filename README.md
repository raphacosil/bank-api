# Bank API

API REST de um sistema de transferências bancárias.

## Stack

- Java 21
- Spring Boot 4.0.3
- Spring Security + JWT (jjwt)
- Spring Data JPA + MySQL 8
- Spring WebFlux (WebClient para chamadas externas)
- Springdoc OpenAPI (Swagger UI)
- Lombok
- Testcontainers + JUnit 5 + H2 (testes)
- Docker Compose

## Funcionalidades

- Cadastro de clientes (PF e PJ)
- Autenticação com credenciais (email/identificador + senha)
- Consulta e gestão de saldo
- Transferências entre clientes com autorização via API externa (`https://util.devi.tools/api`)
- Notificação de transferências

## Estrutura do projeto

```
src/main/java/com/example/bank_api/
├── boundary/          # Controllers e contratos (DTOs de request/response)
├── config/            # Configurações (WebClient, exception handler, security)
├── domain/
│   ├── model/         # Entidades JPA
│   └── service/       # Regras de negócio (customer, balance, transference)
└── infra/
    ├── gateway/       # Integração com APIs externas (autorização, notificação)
    └── repository/    # Repositórios Spring Data
```