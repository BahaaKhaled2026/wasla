# ADR-0003: Upgrade Spring Boot pin from 3.5.16 to 4.1.0

**Status:** Accepted
**Date:** 2026-07-25
**Slice/Area:** VS-00 (backend foundation)

## Context

ADR-0002 pinned Spring Boot 3.5.16, following the version baseline stated in the
original production engineering blueprint. Before any backend code was written,
`start.spring.io`'s current metadata was checked and Spring Boot 3.5.x is no longer
in Initializr's supported generation range (`compatibility range is >=4.0.0`).
Initializr's current default is `4.1.0.RELEASE`, with `4.0.7.RELEASE` as the other
available stable line.

Since no backend code exists yet on the 3.5.16 baseline, this is the correct moment
to make this change — before it costs a migration instead of a substitution.

## Decision

Pin **Spring Boot 4.1.0** (Active line) instead of 3.5.16. This supersedes only the
"Backend" row of ADR-0002's version table; all other pins in ADR-0002 remain
unchanged.

Consequences of the 3.x → 4.x jump to verify as backend work proceeds:

- Spring Framework 7 / Jakarta EE 11 baseline — check any dependency (e.g. jOOQ,
  Resilience4j, OpenTelemetry starter) for 4.x/Jakarta-EE-11 compatible releases
  before adding it.
- Minimum Java version for Spring Boot 4.x is Java 17+; our Java 21 LTS pin (ADR-0002)
  already satisfies this with margin.
- Kotlin 2.4.10 (ADR-0002) is compatible with Spring Boot 4.x; no change needed there.
- Re-verify Testcontainers, Flyway, and the PostgreSQL driver versions against Spring
  Boot 4.1.0's dependency management BOM rather than assuming the ADR-0002 table's
  versions still apply as-is — some may need bumping to versions the BOM manages.

## Consequences

- Backend starts on a currently-supported, actively maintained Spring Boot line
  instead of one already excluded from tooling.
- Slightly higher near-term research cost (verifying each dependency against the 4.x
  BOM) in exchange for avoiding a forced migration later.
- Any part of the source blueprint document that assumed Spring Boot 3.x-specific
  APIs will need re-checking as we implement each slice — flag it when encountered
  rather than assuming continuity.

## Alternatives considered

- **Stay on 3.5.16** — rejected: unsupported by current tooling before we've written
  a single line of code; would guarantee an early forced migration.
- **Pin 4.0.7 instead of 4.1.0** — considered; 4.1.0 is Initializr's stated default
  and the more actively maintained of the two, so it was preferred over the older
  stable line.
