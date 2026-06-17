# 🛡️ Real-Time Fraud Detection System

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen?style=for-the-badge&logo=springboot)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-3.9-black?style=for-the-badge&logo=apachekafka)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge&logo=postgresql)
![Redis](https://img.shields.io/badge/Redis-7.2-red?style=for-the-badge&logo=redis)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker)
![JWT](https://img.shields.io/badge/JWT-Auth-black?style=for-the-badge&logo=jsonwebtokens)

> A production-grade backend system that detects fraudulent transactions in real-time using event-driven architecture.

---

## 🏗️ Architecture
---

## ⚡ Tech Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 21 | Core language |
| Spring Boot | 3.5 | Application framework |
| Spring Security | 6.x | JWT-based auth |
| Apache Kafka | 3.9 | Event streaming |
| PostgreSQL | 16 | Primary database |
| Redis | 7.2 | Cache layer |
| Flyway | 11.x | DB migrations |
| Docker | Latest | Infrastructure |
| Swagger/OpenAPI | 3.x | API docs |

---

## 🚨 Fraud Detection Rules

| Rule | Trigger | Severity |
|------|---------|----------|
| 🔴 HIGH_AMOUNT | Transaction > ₹1,00,000 | HIGH |
| 🚫 VELOCITY_BREACH | > 5 transactions in 10 mins | CRITICAL |
| ⚠️ SELF_TRANSFER | Sender == Receiver | MEDIUM |

---

## 🚀 Getting Started

### Prerequisites
- Java 21+
- Maven 3.8+
- Docker Desktop

### Run the application

```bash
# Clone the repository
git clone https://github.com/aadhithyanmahadevan/fraud-detection.git
cd fraud-detection

# Start infrastructure (PostgreSQL, Redis, Kafka, Zookeeper)
docker compose up -d

# Run the application
mvn spring-boot:run
```

### 📖 API Documentation
Open your browser and go to:
---

## 🔐 API Endpoints

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| POST | `/api/auth/register` | Public | Register new user |
| POST | `/api/auth/login` | Public | Login and get JWT |
| POST | `/api/transactions` | USER | Submit transaction |
| GET | `/api/transactions` | USER | Get all transactions |
| GET | `/api/alerts` | ADMIN/ANALYST | Get all fraud alerts |
| PATCH | `/api/alerts/{id}/status` | ADMIN/ANALYST | Update alert status |

---

## 📁 Project Structure
---

## 👨‍💻 Author

**Aadhithyan Mahadevan**  
[![GitHub](https://img.shields.io/badge/GitHub-aadhithyanmahadevan-black?style=flat&logo=github)](https://github.com/aadhithyanmahadevan)

---

⭐ If you found this project useful, please give it a star!
