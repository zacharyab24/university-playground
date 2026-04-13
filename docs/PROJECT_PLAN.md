# Project Plan

## Phase 1: Convert Assignments to Maven Modules

### Per Project (SENG1120, COMP2240, COMP2230, SENG2250)

* [ ] Create `pom.xml`
* [ ] Move code to `src/main/java/...`
* [ ] Define proper package structure (`com.zacharyab24.<module>`)
* [ ] Ensure project compiles with `mvn clean install`
* [ ] (Optional) Add basic unit tests
* [ ] Remove any CLI / main-method coupling if needed
* [ ] Identify clean entry points for functionality

**Outcome:** Each project builds as a clean JAR.

---

## Phase 2: Nexus Setup

* [ ] Run Nexus via Docker
* [ ] Log in and change default credentials
* [ ] Create repositories:

    * `maven-releases`
    * `maven-snapshots`
* [ ] Configure each project’s `pom.xml`:

    * `distributionManagement`
* [ ] Run `mvn deploy` for each module
* [ ] Verify artifacts exist in Nexus UI

**Outcome:** All modules are privately hosted and retrievable.

---

## Phase 3: Playground Server (Java EE)

### Setup

* [ ] Create new Maven project (`playground-server`)
* [ ] Set packaging to `war`
* [ ] Add dependencies to all modules
* [ ] Add Nexus repository to `pom.xml`

---

### JAX-RS Configuration

* [ ] Create `Application` class
* [ ] Configure base path `/api`

---

### Controllers (Facade Layer)

#### Data Structures

* [ ] LinkedList endpoints
* [ ] Stack endpoints
* [ ] BSTree endpoints

#### Algorithms

* [ ] Scheduling endpoints (COMP2240)
* [ ] TSP / pathfinding endpoints (COMP2230)

#### Networking

* [ ] Security / handshake endpoints (SENG2250)

---

### Design Rules

* Do NOT expose raw internal classes directly
* Use request/response DTOs
* Keep endpoints focused on “what it does”, not “how it works”

---

## Phase 4: Docker Deployment

* [ ] Create Dockerfile (Wildfly base image)
* [ ] Copy WAR into deployments directory
* [ ] Build Docker image
* [ ] Run container locally
* [ ] Verify endpoints via curl/Postman

---

## Phase 5: Validation

* [ ] Test each endpoint manually
* [ ] Validate error handling
* [ ] Ensure consistent API structure
* [ ] Confirm all modules resolve from Nexus (no local installs)

---

## Stretch Goals (Later)

* [ ] Add frontend playground UI
* [ ] Add visualisations (TSP, scheduling)
* [ ] Add authentication layer
* [ ] Add CI/CD pipelines
