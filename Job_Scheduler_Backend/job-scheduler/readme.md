# 🛠️ Job Scheduler Backend

This is a robust and extensible backend system that allows you to **schedule and run jobs** (like `.jar` files or `npm` scripts) at a future time, either as a one-time, delayed, or recurring execution. The system supports Kafka messaging and binary storage in MinIO, with PostgreSQL/YugabyteDB as the persistent store.

---

## 🚀 Features

- ✅ Schedule immediate, one-time, delayed, or recurring jobs
- ✅ Upload and run `.jar` or `npm` projects from MinIO
- ✅ Kafka message publishing on job execution
- ✅ Recurring job management (HOURLY, DAILY, WEEKLY, MONTHLY)
- ✅ MinIO-based file uploads and downloads
- ✅ Live logs from job executions
- ✅ Swagger API documentation
- ✅ Docker-based setup for Kafka, MinIO, YugabyteDB
- ✅ Testcontainers integration for integration tests

---

## ⚙️ Tech Stack

| Layer      | Technology                          |
|------------|--------------------------------------|
| Language   | Java 17                              |
| Framework  | Spring Boot 3.4.x                    |
| DB         | YugabyteDB / PostgreSQL              |
| Messaging  | Apache Kafka                         |
| Storage    | MinIO                                |
| Scheduler  | Spring `@Scheduled` tasks            |
| Testing    | JUnit 5 + Testcontainers + AssertJ   |
| Docs       | Springdoc + Swagger UI               |
| Build Tool | Maven                                |

---

## 📦 API Endpoints

| Method | Path                       | Description                   |
|--------|----------------------------|-------------------------------|
| POST   | `/api/jobs`               | Create a new job              |
| GET    | `/api/jobs`               | List all jobs                 |
| GET    | `/api/jobs/{id}`          | Get job by ID                 |
| PATCH  | `/api/jobs/{id}/status`   | Update job status             |
| POST   | `/api/jobs/upload`        | Upload a binary to MinIO      |
| GET    | `/swagger-ui.html`        | API Documentation             |

---

## 🐳 Docker Setup

Run all required services:

```bash
docker-compose up
