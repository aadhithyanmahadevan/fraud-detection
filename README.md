# Real-Time Fraud Detection System

A production-grade backend system that detects fraudulent transactions in real-time using a modern Java tech stack.

## Tech Stack

- **Java 21** — Core language
- **Spring Boot 3.5** — Application framework
- **Apache Kafka** — Async transaction event streaming
- **PostgreSQL** — Primary database
- **Redis** — Cache layer
- **Docker** — Containerized infrastructure
- **JWT** — Stateless authentication
- **Flyway** — Database migrations
- **Swagger/OpenAPI** — API documentation

## Fraud Detection Rules

| Rule | Trigger | Severity |
|------|---------|----------|
| HIGH_AMOUNT | Transaction > ₹1,00,000 | HIGH |
| VELOCITY_BREACH | > 5 transactions in 10 mins | CRITICAL |
| SELF_TRANSFER | Sender == Receiver | MEDIUM |

## Architecture
## Getting Started

### Prerequisites
- Java 21
- Maven
- Docker

### Run the application

```bash
# Start infrastructure
docker compose up -d

# Run the app
mvn spring-boot:run
```

### API Documentation
Open `http://localhost:8080/swagger-ui.html`

## API Endpoints

| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /api/auth/register | Public |
| POST | /api/auth/login | Public |
| POST | /api/transactions | USER |
| GET | /api/alerts | ADMIN/ANALYST |
| PATCH | /api/alerts/{id}/status | ADMIN/ANALYST |
