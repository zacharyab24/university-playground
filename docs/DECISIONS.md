# Decisions

## Java EE over Spring Boot

Using Jakarta EE 10 (JAX-RS) deployed to Wildfly rather than Spring Boot. This is a deliberate learning choice to work with application server deployment (EAR/WAR/EJB) rather than embedded containers.

## EAR packaging with EJB + WAR

Split into `playground-ejb` (services, DTOs) and `playground-war` (REST resources) packaged together as an EAR. This separates business logic from HTTP concerns and reflects traditional Java EE layering.

## Self-hosted Nexus for private artifacts

Assignment modules are published to a private Nexus instance rather than a public repository. Keeps university work private while allowing proper dependency management.

## OpenAPI via Swagger annotations

API documentation is defined inline using `swagger-jaxrs2-jakarta` annotations rather than a separate spec file. Keeps docs co-located with the code they describe. The full spec is served at `GET /api/` using `JaxrsOpenApiContextBuilder` to scan all resource packages.

## Go + HTMX frontend in a separate repo

The planned frontend will be a Go service rendering HTMX templates that consumes the REST API. It lives in its own repository because:

* Different build toolchain (Go modules vs Maven)
* Different deployment target (standalone binary vs Wildfly EAR)
* The API is designed to be consumed by any client, not coupled to a specific frontend
* Follows the same pattern as assignment modules - separate repos, clear boundaries

## Concurrency endpoints run real threads

The semaphore, monitor, and wormhole simulations spawn actual Java threads per-request to faithfully demonstrate the concurrency patterns from the original assignments. This is intentional for educational accuracy, not production scalability.

## Data structures as stateless endpoints

Rather than building stateful CRUD endpoints with session management for linked lists/trees/tables, the data structure endpoints accept all data in the request, build the structure internally, run operations, and return results. This keeps the API stateless and consistent with all other endpoints.

## Crypto endpoints as building blocks

The network security endpoints expose individual cryptographic primitives (RSA, DH, CBC, HMAC) rather than a monolithic handshake endpoint. The frontend will orchestrate the multi-step handshake by calling these building blocks in sequence, displaying each step in a side-by-side client/server demo.

## TSP size limits

Dynamic programming TSP is capped at 20 nodes (O(2^n * n) complexity). Hill climbing is capped at 100 nodes with iteration/attempt limits. These prevent memory exhaustion and request timeouts.

## BigInteger serialisation as decimal strings

RSA and DH keys use BigInteger internally but are transmitted as decimal strings in JSON. This is verbose but unambiguous — base64 or hex would be more compact but adds encoding complexity for a demo API.

## Testing strategy

Unit tests in the EJB module verify service logic. Integration tests in the WAR module use RESTEasy embedded Undertow + RESTAssured to test real HTTP request/response cycles without a full application server. A crypto E2E test simulates the complete handshake flow across all endpoints.

## CI with GitHub Actions

CI runs `mvn verify` on PRs and pushes to main. Nexus credentials are stored as GitHub secrets and injected via a generated `settings.xml`. The Nexus instance uses HTTPS via Cloudflare tunnel.