# ADR-0002: Runtime and toolchain version baseline

**Status:** Accepted
**Date:** 2026-07-25
**Slice/Area:** cross-cutting

## Context

Floating versions (`latest` tags, unpinned SDKs) cause non-reproducible builds and
silent breakage between local, CI, and staging environments. This is unacceptable for
a production-shaped delivery pipeline from the first commit (rule 1.2, "main must
remain releasable").

## Decision

Pin every runtime and tool to an exact version, enforced via committed version files
and lockfiles:

| Area              | Version                      | Pinning mechanism                         |
| ----------------- | ---------------------------- | ----------------------------------------- |
| JVM               | Java 21 LTS                  | Gradle toolchain + container base digest  |
| Kotlin            | 2.4.10                       | Gradle version catalog                    |
| Backend framework | Spring Boot 3.5.16           | Gradle plugin/BOM                         |
| Build             | Gradle Wrapper               | Committed wrapper + checksum verification |
| Identity          | Keycloak 26.7.x              | Exact patch + image digest                |
| Mobile            | Flutter 3.44.6 / Dart 3.12.2 | FVM pin + committed config                |
| Web runtime       | Node.js 24 LTS               | `.node-version`                           |
| Web framework     | Next.js 16.2.11 (Active LTS) | Exact package version + lockfile          |
| Package manager   | pnpm 10.x                    | `packageManager` field, exact version     |
| Infrastructure    | Terraform 1.15.8             | Version constraint + lockfile             |
| Database          | PostgreSQL 17 + PostGIS      | Matched across local/CI/staging/Azure     |

No container image may use a floating tag such as `latest`. Upgrades to major
framework versions (e.g. Spring Boot major line) require a new ADR, not an ad-hoc bump.

## Consequences

- Every environment (dev laptop, CI runner, staging, production) runs identical
  versions, eliminating an entire class of "works on my machine" defects.
- Security patch upgrades (e.g. Keycloak point releases) are still expected promptly,
  just via a deliberate, committed version bump rather than a floating tag.
- Slightly more upfront ceremony per dependency bump (a version-file edit + lockfile
  regen), which is the accepted tradeoff for reproducibility.

## Alternatives considered

- **Floating/`latest` tags** — rejected: non-reproducible, silent breakage risk.
- **Version ranges (e.g. `^3.5.0`)** — rejected for infrastructure and frameworks;
  acceptable only where the doc explicitly allows it (none currently).
