# Architecture

## Overview

The system follows a modular architecture:

```
[ Assignment Modules ]
        ↓
     (JARs)
        ↓
   [ Nexus Repository ]
        ↓
[ Playground Server (Java EE) ]
        ↓
     REST API
```

---

## Key Patterns

### Facade Pattern

The playground server provides a unified API over multiple independent modules.

### Adapter Pattern

Each assignment’s internal logic is adapted into a consistent external interface.

### Separation of Concerns

* Modules: pure logic
* Server: API layer
* Nexus: artifact storage

---

## Design Constraints

* No database
* No frontend (initially)
* All state handled in-memory or per-request
* Modules must remain independently usable

---

## Trade-offs

* Some APIs may feel unnatural (assignments weren’t designed as services)
* State management may be awkward for certain data structures
* Extra abstraction layer adds overhead

These are intentional and part of the learning goal.
