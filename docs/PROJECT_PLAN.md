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

### Endpoints - Operating Systems (COMP2240)

* [x] Scheduling - FCFS, SPN, PP, PRR (`POST /api/scheduler`)
* [x] Concurrency - Semaphore simulation (`POST /api/multithreading/semaphore`)
* [x] Concurrency - Monitor simulation (`POST /api/multithreading/monitor`)
* [x] Concurrency - Wormhole simulation (`POST /api/multithreading/wormhole`)
* [ ] Paging - page replacement algorithms (`POST /api/paging`)

### Endpoints - Data Structures (SENG1120)

* [ ] LinkedList endpoints
* [ ] Stack endpoints
* [ ] BSTree endpoints

### Endpoints - Algorithms (COMP2230)

* [ ] TSP / pathfinding endpoints

### Endpoints - Networking (SENG2250)

* [ ] Security / handshake endpoints

---

## Phase 4: Docker Deployment

* [ ] Create Dockerfile (Wildfly base image)
* [ ] Copy EAR into deployments directory
* [ ] Build Docker image
* [ ] Run container locally
* [ ] Verify endpoints via curl/Postman

---

## Phase 5: Frontend (Go + HTMX)

* [ ] Create separate repository for Go frontend
* [ ] Set up Go module with HTMX templating
* [ ] Implement pages consuming the REST API
* [ ] Add visualisations (scheduling, concurrency, paging)

---

## Phase 6: Validation

* [ ] Test each endpoint manually
* [ ] Validate error handling
* [ ] Ensure consistent API structure
* [ ] Confirm all modules resolve from Nexus (no local installs)

---

## Stretch Goals

* [ ] Add authentication layer
* [ ] Add CI/CD pipelines