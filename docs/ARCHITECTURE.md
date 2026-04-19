# Architecture

## Overview

The system follows a modular architecture with clear separation between assignment logic, service orchestration, and API exposure:

```
[ Assignment Modules ]          (separate repos)
        |
     (JARs via Nexus)
        |
[ playground-ejb ]              service layer, DTOs, business logic
[ playground-war ]              JAX-RS resources, OpenAPI annotations
        |
[ playground-ear ]              packages EJB + WAR
        |
[ Wildfly 34 / Docker ]        runtime
        |
     REST API
        |
[ Go + HTMX Frontend ]         (planned, separate repo)
```

---

## Module Structure

### playground-ejb

Contains service classes and DTOs. This is where assignment modules are called and their results are mapped into API-friendly response objects.

* `os.scheduler` - Scheduling algorithm service (FCFS, SPN, PP, PRR)
* `os.concurrency` - Concurrency simulations (semaphore, monitor, wormhole)
* `os.paging` - Paging algorithm service (in progress)

### playground-war

Contains JAX-RS resource classes with OpenAPI annotations. Handles HTTP concerns (request parsing, response codes, error handling) and delegates to EJB services.

### playground-ear

Packages the EJB and WAR into a single deployable EAR for Wildfly.

---

## Key Patterns

### Facade Pattern

The playground server provides a unified API over multiple independent assignment modules.

### Adapter Pattern

Each assignment's internal logic is adapted into a consistent external interface via request/response DTOs.

### Factory Pattern

Concurrency simulations use `SchedulerFactory` (a functional interface) to inject the concurrency strategy (semaphore vs monitor) without the service knowing the concrete type.

### Separation of Concerns

* **Assignment modules**: pure logic, no API awareness
* **EJB layer**: orchestration, mapping between library types and DTOs
* **WAR layer**: HTTP/REST concerns, OpenAPI documentation
* **Nexus**: artifact storage and distribution

---

## Design Constraints

* No database - all state is handled in-memory or per-request
* The API is strictly RESTful - no server-side rendering
* Modules must remain independently usable outside of this project
* Assignment modules are consumed as versioned dependencies, not source copies

---

## Planned Frontend

A separate Go + HTMX application will consume the REST API to provide a browser-based UI. This lives in its own repository to maintain the clean separation between the API and its consumers.

---

## Trade-offs

* Some APIs may feel unnatural (assignments weren't designed as services)
* State management may be awkward for certain data structures
* Extra abstraction layer adds overhead
* Concurrency endpoints run real threads per-request (not suitable for high traffic)

These are intentional and part of the learning goal.