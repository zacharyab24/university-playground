# Project Plan

## Phase 1: Convert Assignments to Maven Modules

### Per Project (SENG1120, COMP2240, COMP2230, SENG2250)

* [x] Create `pom.xml`
* [x] Move code to `src/main/java/...`
* [x] Define proper package structure (`com.zacharyab24.<module>`)
* [x] Ensure project compiles with `mvn clean install`
* [ ] (Optional) Add basic unit tests
* [x] Remove any CLI / main-method coupling if needed
* [x] Identify clean entry points for functionality

**Outcome:** Each project builds as a clean JAR.

---

## Phase 2: Nexus Setup

* [x] Run Nexus via Docker
* [x] Log in and change default credentials
* [x] Create repositories:
    * `maven-releases`
    * `maven-snapshots`
* [x] Configure each project's `pom.xml`:
    * `distributionManagement`
* [x] Run `mvn deploy` for each module
* [x] Verify artifacts exist in Nexus UI

**Outcome:** All modules are privately hosted and retrievable.

---

## Phase 3: Playground Server (Java EE)

### Setup

* [x] Create Maven multi-module project (parent + EJB + WAR + EAR)
* [x] Add dependencies to assignment modules via Nexus
* [x] Configure OpenAPI / Swagger annotations
* [x] Add OpenAPI spec endpoint at `GET /api/`

### Endpoints - Operating Systems (COMP2240)

* [x] Scheduling - FCFS, SPN, PP, PRR (`POST /api/scheduler`)
* [x] Concurrency - Semaphore simulation (`POST /api/multithreading/semaphore`)
* [x] Concurrency - Monitor simulation (`POST /api/multithreading/monitor`)
* [x] Concurrency - Wormhole simulation (`POST /api/multithreading/wormhole`)
* [x] Paging - page replacement algorithms (`POST /api/paging`)

### Endpoints - Algorithms (COMP2230)

* [x] TSP - Dynamic Programming and Hill Climbing (`POST /api/tsp`)
* [x] Maze - Generate (`POST /api/maze/generate`)
* [x] Maze - Solve with BFS and DFS (`POST /api/maze/solve`)
* [x] Maze - Verify structure and solutions (`POST /api/maze/verify`)

### Endpoints - Data Structures (SENG1120)

* [x] Toll report - merge, deduplicate, count, income (`POST /api/tolls/report`)
* [x] Inventory stats - BSTree and HTable (`POST /api/inventory/stats`)
* [x] Inventory benchmark - add/remove cycles (`POST /api/inventory/benchmark`)

### Endpoints - Network Security (SENG2250)

* [x] RSA - key generation, encrypt, decrypt, sign, verify (`/api/crypto/rsa/*`)
* [x] Diffie-Hellman - key generation, session key derivation (`/api/crypto/dh/*`)
* [x] AES-CBC - key generation, encrypt, decrypt (`/api/crypto/cbc/*`)
* [x] HMAC-SHA256 (`POST /api/crypto/hmac`)

---

## Phase 4: Testing

* [x] Add JUnit 5 + AssertJ for EJB unit tests
* [x] Add RESTEasy embedded Undertow + RESTAssured for WAR integration tests
* [x] Crypto end-to-end test (full handshake flow)
* [x] CI via GitHub Actions

---

## Phase 5: Docker Deployment

* [ ] Create Dockerfile (Wildfly base image)
* [ ] Copy EAR into deployments directory
* [ ] Build Docker image
* [ ] Run container locally
* [ ] Verify endpoints via curl/Postman

---

## Phase 6: Frontend (Go + HTMX)

* [ ] Create separate repository for Go frontend
* [ ] Set up Go module with HTMX templating
* [ ] Implement pages consuming the REST API
* [ ] Add visualisations (scheduling, concurrency, paging)
* [ ] Side-by-side crypto handshake demo

---

## Phase 7: Validation

* [ ] Test each endpoint manually
* [ ] Validate error handling
* [ ] Ensure consistent API structure
* [ ] Confirm all modules resolve from Nexus (no local installs)

---

## Stretch Goals

* [ ] Add authentication layer
* [ ] Add CI/CD pipelines for deployment