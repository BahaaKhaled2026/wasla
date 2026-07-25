# ADR-0001: Modular monolith with three backend deployables

**Status:** Accepted
**Date:** 2026-07-25
**Slice/Area:** cross-cutting

## Context

Wasla's backend covers many bounded contexts (identity/tenant, academics/custody,
learning/AI, transport, engagement, notifications/media, billing/entitlements,
privacy/audit, analytics). A microservice-per-domain architecture would multiply
operational surface area (deployments, networking, observability, on-call) far beyond
what a solo developer can safely run in production. A single undivided monolith would
risk uncontrolled coupling between domains as the codebase grows.

## Decision

Structure the backend as a **modular monolith**: one Gradle multi-project build
containing independently-owned modules under `backend/modules/*`, each with hexagonal
internal boundaries (`domain/`, `application/`, `infrastructure/`, `api/`). Modules do
not access each other's database tables directly (see engineering rule 1.6); they
communicate through published application interfaces, query ports, or event
projections.

Ship exactly **three backend deployables**:

- `wasla-api` — the synchronous request-serving application
- `wasla-worker` — asynchronous jobs, sagas, and the transactional outbox consumer
- `location-service` — an isolated service for transport/location workloads

Client-facing surfaces are **four deployables**: `family_school_flutter`,
`driver_flutter`, `admin_web`, `control_plane_web` — the control plane is a distinct
authorization boundary from the school Admin console and is never merged into it.

## Consequences

- Module boundaries must be enforced by convention and code review, not by network
  isolation — this requires discipline (rule 1.6) but avoids distributed-systems
  operational cost.
- A single deploy of `wasla-api` ships all modules together; a bug in one module can
  still crash the whole API process. Mitigated by staged rollout, feature flags, and
  the deployment order in §79.
- Splitting a module into its own service later remains possible because hexagonal
  boundaries already isolate domain logic from persistence and transport.
- `location-service` is separated up front because transport/location has different
  scaling and latency characteristics than the rest of the platform.

## Alternatives considered

- **Microservices per bounded context** — rejected: infeasible operational load for
  one full-time developer (Kubernetes, per-service CI/CD, service mesh, cross-service
  tracing).
- **Single undivided monolith with no module boundaries** — rejected: high risk of
  domain coupling (e.g. billing code reaching into academic records tables) with no
  structural guardrail.
