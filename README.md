# University Playground

> This repository contains the **playground-server** (the Java EE / JAX-RS facade). Assignment modules live in their own repositories and are consumed via a private Nexus instance. See [`docs/`](docs/) for architecture, decisions, and the project plan.

A backend-focused project that exposes refactored university assignments as REST APIs.

The goal is to take standalone academic code and treat it like production systems:

* Modularised as Maven artifacts
* Published to a private artifact repository (Nexus)
* Consumed by a unified API layer (Java EE + JAX-RS)
* Deployed via Docker + Wildfly
* Documented with OpenAPI 3 (Swagger)
* Tested with JUnit 5, AssertJ, and RESTAssured

---

## Project Structure

```
playground-parent (pom)
├── playground-ejb          Service layer (business logic, DTOs)
├── playground-war          REST endpoints (JAX-RS resources, OpenAPI)
└── playground-ear          Packages EJB + WAR for Wildfly deployment
```

---

## Components

### 1. Assignment Modules (Separate Repos)

Each assignment is refactored into a standalone Maven project:

* `seng1120` - Data structures (linked list, BSTree, hash table)
* `comp2240` - OS algorithms (scheduling, concurrency, paging)
* `comp2230` - Graph algorithms (TSP, maze solving)
* `seng2250` - Network security (RSA, DH, AES-CBC, HMAC)
* More to come in future

Each module:

* Builds as a JAR
* Publishes to a private Nexus repository
* Exposes clean, reusable APIs internally (no UI / CLI)

---

### 2. Artifact Repository (Nexus)

A self-hosted Nexus instance at `nexus.zacbower.com` is used to:

* Store private Maven artifacts
* Avoid exposing university work publicly
* Provide a single dependency source for the server

---

### 3. Playground Server

A Java EE (Jakarta EE 10) application that:

* Depends on all assignment modules
* Exposes their functionality via REST endpoints
* Acts as a facade over heterogeneous code
* Documents APIs via OpenAPI 3 / Swagger annotations
* Serves the OpenAPI spec at `GET /api/`

---

### 4. Deployment

* Packaged as an EAR (EJB + WAR)
* Deployed to Wildfly 34
* Containerised using Docker
* Runs on self-hosted infrastructure
* CI via GitHub Actions

---

## API Endpoints

### OpenAPI Spec

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/` | OpenAPI 3 JSON specification |

### Operating Systems (COMP2240)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/scheduler` | Run FCFS, SPN, PP, and PRR scheduling algorithms |
| POST | `/api/multithreading/semaphore` | Run semaphore-based concurrency simulation |
| POST | `/api/multithreading/monitor` | Run monitor-based (synchronized) concurrency simulation |
| POST | `/api/multithreading/wormhole` | Run wormhole traveller simulation |
| POST | `/api/paging` | Run Fixed-Local and Variable-Global paging simulations |

### Algorithms (COMP2230)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/tsp` | Solve TSP with Dynamic Programming or Hill Climbing |
| POST | `/api/maze/generate` | Generate a random maze using DFS |
| POST | `/api/maze/solve` | Solve a maze with BFS and DFS |
| POST | `/api/maze/verify` | Verify maze structure and solution paths |

### Data Structures (SENG1120)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/tolls/report` | Merge toll booth records and generate daily report |
| POST | `/api/inventory/stats` | Calculate inventory statistics using BSTree or HTable |
| POST | `/api/inventory/benchmark` | Benchmark add/remove cycles on BSTree or HTable |

### Network Security (SENG2250)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/crypto/rsa/generate` | Generate RSA key pair |
| POST | `/api/crypto/rsa/encrypt` | Encrypt with RSA public key |
| POST | `/api/crypto/rsa/decrypt` | Decrypt with RSA private key |
| POST | `/api/crypto/rsa/sign` | Sign a message with RSA |
| POST | `/api/crypto/rsa/verify` | Verify an RSA signature |
| POST | `/api/crypto/dh/generate` | Generate Diffie-Hellman key pair |
| POST | `/api/crypto/dh/session` | Derive shared session key |
| POST | `/api/crypto/cbc/generate-key` | Generate AES key and IV |
| POST | `/api/crypto/cbc/encrypt` | Encrypt with AES-CBC |
| POST | `/api/crypto/cbc/decrypt` | Decrypt with AES-CBC |
| POST | `/api/crypto/hmac` | Compute HMAC-SHA256 |

---

## Goals

* Learn how to expose legacy / academic code as services
* Practice modular architecture and dependency management
* Apply real-world backend patterns (facade, adapter, service boundaries)
* Build a portfolio project grounded in actual engineering problems

---

## License

All rights reserved. This project is **not** open source and is **not** licensed for reuse, redistribution, or modification. The source is visible for portfolio purposes only.