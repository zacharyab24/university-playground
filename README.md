# University Playground

> This repository contains the **playground-server** (the Java EE / JAX-RS facade). Assignment modules live in their own repositories and are consumed via a private Nexus instance. See [`docs/`](docs/) for architecture, decisions, and the project plan.

A backend-focused project that exposes refactored university assignments as REST APIs.

The goal is to take standalone academic code and treat it like production systems:

* Modularised as Maven artifacts
* Published to a private artifact repository (Nexus)
* Consumed by a unified API layer (Java EE + JAX-RS)
* Deployed via Docker + Wildfly

This project is intentionally backend-only. No database or frontend is included at this stage.

---

## Components

### 1. Assignment Modules (Separate Repos)

Each assignment is refactored into a standalone Maven project:

* `seng1120-datastructures`
* `comp2240-algorithms`
* `comp2230-algorithms`
* `seng2250-networking`

Each module:

* Builds as a JAR
* Publishes to a private Nexus repository
* Exposes clean, reusable APIs internally (no UI / CLI)

---

### 2. Artifact Repository (Nexus)

A self-hosted Nexus instance is used to:

* Store private Maven artifacts
* Avoid exposing university work publicly
* Provide a single dependency source for the server

---

### 3. Playground Server

A Java EE (JAX-RS) application that:

* Depends on all assignment modules
* Exposes their functionality via REST endpoints
* Acts as a facade over heterogeneous code

---

### 4. Deployment

* Packaged as a WAR
* Deployed to Wildfly
* Containerised using Docker
* Runs on self-hosted infrastructure

---

## Goals

* Learn how to expose legacy / academic code as services
* Practice modular architecture and dependency management
* Apply real-world backend patterns (facade, adapter, service boundaries)
* Build a portfolio project grounded in actual engineering problems

---

## License

All rights reserved. This project is **not** open source and is **not** licensed for reuse, redistribution, or modification. The source is visible for portfolio purposes only.
