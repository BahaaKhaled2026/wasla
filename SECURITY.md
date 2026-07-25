# Security Policy

## Reporting a vulnerability

Wasla is currently in pre-production development. If you discover a security
vulnerability, please report it privately rather than opening a public issue.

- **Contact:** <your-email-or-security-contact>
- **Response target:** acknowledgment within 3 business days
- **Scope:** the `wasla` monorepo (backend, mobile, web, infrastructure)

Do not include real user, child, guardian, or attendance data in any report —
use synthetic reproduction data only (see engineering rule 1.7).

## Supported versions

Pre-1.0: only the `main` branch is supported. There are no released versions yet.

## Disclosure process

1. Report received and triaged for severity (critical/high/normal/low).
2. Critical/high findings pause feature work on the affected slice until resolved
   (rule: "Stop feature work for critical security, tenancy, backup, or production
   defects").
3. Fix is developed on a `fix/<incident-or-defect>` branch per the branch pattern
   in `CONTRIBUTING.md`.
4. Fix is deployed; disclosure timing is decided case-by-case pre-launch.
