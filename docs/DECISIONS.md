# Decisions

## Java EE over Spring Boot

Using Jakarta EE 10 (JAX-RS) deployed to Wildfly rather than Spring Boot. This is a deliberate learning choice to work with application server deployment (EAR/WAR/EJB) rather than embedded containers.

## EAR packaging with EJB + WAR

Split into `playground-ejb` (services, DTOs) and `playground-war` (REST resources) packaged together as an EAR. This separates business logic from HTTP concerns and reflects traditional Java EE layering.

## Self-hosted Nexus for private artifacts

Assignment modules are published to a private Nexus instance rather than a public repository. Keeps university work private while allowing proper dependency management.

## OpenAPI via Swagger annotations

API documentation is defined inline using `swagger-jaxrs2-jakarta` annotations rather than a separate spec file. Keeps docs co-located with the code they describe.

## Go + HTMX frontend in a separate repo

The planned frontend will be a Go service rendering HTMX templates that consumes the REST API. It lives in its own repository because:

* Different build toolchain (Go modules vs Maven)
* Different deployment target (standalone binary vs Wildfly EAR)
* The API is designed to be consumed by any client, not coupled to a specific frontend
* Follows the same pattern as assignment modules - separate repos, clear boundaries

## Concurrency endpoints run real threads

The semaphore, monitor, and wormhole simulations spawn actual Java threads per-request to faithfully demonstrate the concurrency patterns from the original assignments. This is intentional for educational accuracy, not production scalability.