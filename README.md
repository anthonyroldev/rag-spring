# RAG API

A document question-answering API built with Spring Boot and RAG (Retrieval-Augmented Generation). 
Upload documents, ask questions, get answers grounded in your content.

---

## Architecture

### Ingestion Pipeline (Async)

```
POST /api/documents
  → Validate file
  → Persist metadata to DB (status: PENDING)
  → Upload to S3
  → Publish DocumentUploadedEvent
  → Return 202 Accepted

Background (event listener):
  → Download from S3
  → Extract text
  → Split into chunks
  → Generate embeddings via Ollama
  → Store vectors in pgvector
  → Update status to COMPLETED
```

### Query Pipeline (Sync)

```
POST /api/ask
  → Embed question via Ollama
  → Cosine similarity search against pgvector
  → Retrieve top-K relevant chunks
  → Build prompt (system message + context + question)
  → Call Ollama LLM
  → Return answer + source chunks
```

---

## Tech Stack

| Layer           | Technology                               |
|-----------------|------------------------------------------|
| Runtime         | Java 25 (Amazon Corretto 25)             |
| Framework       | Spring Boot 4.0.2                        |
| AI Integration  | Spring AI (Ollama + pgvector)            |
| Database        | PostgreSQL 18 + pgvector extension       |
| File Storage    | AWS S3                                   |
| Migrations      | Flyway                                   |
| ORM             | Spring Data JPA                          |
| LLM Inference   | Ollama (local)                           |
| Embeddings      | nomic-embed-text-v2-moe (768 dimensions) |
| Build           | Gradle (Kotlin DSL)                      |
| Infrastructure  | Docker + Docker Compose                  |

---

## Prerequisites

- Docker and Docker Compose
- AWS account with an S3 bucket
- AWS credentials (access key + secret key)

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/your-username/rag-api.git
cd rag-api
```

### 2. Configure environment variables

Copy the example environment file and fill in your values:

```bash
cp .env.example .env
```

Required variables:

```env
AWS_ACCESS_KEY_ID=your_access_key
AWS_SECRET_ACCESS_KEY=your_secret_key
AWS_REGION=eu-west-3
AWS_S3_BUCKET=your-bucket-name
```

### 3. Start the stack

```bash
docker compose up -d
```

This starts three services:
- `postgres` — PostgreSQL 18 with pgvector extension
- `ollama` — Local LLM inference server
- `api` — Spring Boot application

### 4. Pull Ollama models

The models must be pulled once after the Ollama container starts:

```bash
docker exec ollama ollama pull nomic-embed-text-v2-moe:latest
docker exec ollama ollama pull llama3.2:3b
```

### 5. Verify the stack is healthy

```bash
curl http://localhost:8080/actuator/health
```

Expected response:

```json
{"status": "UP"}
```

---

## Document Processing

Supported file types: `.txt`

File size limit: 10MB

Chunking strategy: fixed-size with overlap (configurable via `application.yml`)

```yaml
app:
  chunking:
    chunk-size: 800
    overlap: 400
```

---
