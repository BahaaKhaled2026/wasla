# Contributing to Wasla

This document describes how work happens in this repository. It exists because a
solo/small team under deadline pressure is exactly when engineering discipline
erodes first — these rules are here to prevent that.

## Core rules (non-negotiable)

These apply from the first commit, not "once things are production-hardened."

1. **One production-ready slice at a time.** Only one primary vertical slice is
   actively implemented. Finish its contract, schema, backend, UI, tests, telemetry,
   documentation, staging deployment, and rollback before starting the next one.
2. **`main` must always be releasable.** Short-lived branches, merged daily behind
   server-controlled feature flags. No long-running epic/batch branches.
3. **Contract before implementation.** Before writing a controller or screen that
   consumes real data, define: endpoint path/method, auth requirement, permission
   name, request/response schema, idempotency behavior, concurrency behavior, error
   codes, audit behavior, emitted events, and Arabic/English examples where
   user-visible content exists.
4. **Deny by default.** The server is authoritative. Hiding an action in the client
   does not authorize it.
5. **Never trust client-supplied identity context.** A client may send a `tenantId`
   in a route, but it may never assert its own roles, permissions, membership status,
   schema, or another user's identity.
6. **No cross-module table access.** A backend module owns its tables. Other modules
   use a published application interface, a purpose-limited query port, or an event
   projection — never a direct table read/write into another module's schema.
7. **No production data outside production.** Local, CI, and staging use synthetic
   Arabic/English fixtures only. Never copy real child, guardian, phone, attendance,
   chat, media, or location data into a lower environment.
8. **No secrets in source or `.env` history.** Local placeholder credentials may
   exist in an untracked local file only. Cloud credentials use workload
   identity/OIDC and Key Vault.
9. **Offline and retry states must be explicit.** Every client operation
   distinguishes: not started, loading, retryable failure, non-retryable denial,
   offline, pending server confirmation, completed. No UI shows "success" before the
   server has committed the operation.

## Branching

Pattern:
main
feat/vs-00-walking-skeleton
feat/vs-01-tenant-provisioning
feat/vs-02-auth-context
feat/vs-03-invitations
feat/vs-04-bulk-import
fix/<incident-or-defect>
chore/<dependency-or-infrastructure-change>

- Branch off `main`, keep the branch short-lived (days, not weeks).
- Merge behind a feature flag if the slice isn't fully done end-to-end yet.
- Do not create a long-running branch per batch (e.g. no `batch-1` branch).

## Pull requests

- Every change to `main` goes through a PR once branch protection is active.
- Use the PR template checklist (contract updated, migration expand-safe, tests
  added, telemetry reviewed, feature flag present if incomplete, docs/slice file
  updated).
- CI must pass (format, lint, unit tests, contract checks at minimum) before merge.
- Solo-developer note: self-approval is acceptable pre-team, but the PR review step
  itself is not skipped — it's a deliberate re-read before merge, not a rubber stamp.

## Dependency policy

- Every dependency version is pinned exactly — see
  `docs/adr/ADR-0002-runtime-version-baseline.md`.
- No floating tags (`latest`, unpinned ranges) for containers or infrastructure.
- Framework major-version upgrades require a new ADR.
- Security patch releases (e.g. Keycloak point releases) are applied promptly, even
  outside the normal upgrade cadence.
- Dependency bumps go through the normal PR process.

## Database migrations

- Migrations are expand-safe: a new migration must remain compatible with the
  _previous_ application revision (no destructive change deployed in the same step
  as the code that requires it).
- Do not depend on down-migrations for production rollback — use forward fixes.

## Testing expectations

Before a slice is considered done, it needs, as applicable:
unit tests, integration tests, contract tests, E2E tests, and negative-authorization
tests (proving denied access is actually denied, not just that allowed access works).

## Architecture Decision Records (ADRs)

Any decision that changes a pinned version, a module boundary, an architectural
pattern, or reverses a prior ADR gets a new ADR in `docs/adr/`, using
`docs/adr/TEMPLATE.md`. Decisions are not just made in PR descriptions or chat.

## Vertical slice documentation

Each vertical slice (VS-00, VS-01, ...) has a living document in
`docs/vertical-slices/`, created from `docs/vertical-slices/TEMPLATE.md` **before**
implementation starts, and updated whenever a decision changes during
implementation — not written retroactively after the slice is "done."

## Commit messages

Conventional, short, imperative mood:

feat: add tenant provisioning saga
fix: correct OTP expiry window
docs: add ADR-0002
chore: bump Terraform to 1.15.8

## When to stop and fix instead of building

Feature work stops immediately for: critical security findings, cross-tenant
isolation failures, backup/restore failures, or active production defects. These
take priority over any slice in progress.
