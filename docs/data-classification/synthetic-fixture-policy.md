# Synthetic Fixture Data Policy

## Rule

Local, CI, and staging environments use **synthetic Arabic/English fixture data
only**. Real child, guardian, phone, attendance, chat, media, or location data must
never be copied into any environment other than production (engineering rule 1.7).

This applies even for debugging a production defect — reproduce with synthetic data
that shares the defect's shape, not with an export of the real record.

## Fixture requirements

- Every fixture set includes both Arabic and English variants of user-facing text
  (names, school names, addresses) to exercise RTL/LTR and bilingual code paths.
- Phone numbers use a reserved non-routable test range (defined per SMS provider
  sandbox — do not use real, possibly-reassignable numbers).
- Fixture tenant names are clearly synthetic (e.g. prefixed `Test School —`,
  `مدرسة تجريبية —`) so a synthetic tenant can never be mistaken for a real one in
  any environment, including production.
- No fixture may reuse a real person's name, phone number, or photo, even if
  "anonymized" — generate synthetic identities from scratch.

## Location

- `tests/fixtures/` — versioned, reviewed fixture data used by automated tests.
- Staging seed data is generated from the same fixture generators used by tests, not
  hand-maintained separately, to avoid drift.

## Enforcement

- Any PR introducing new fixture data is reviewed against this policy as part of the
  PR checklist.
- CI must never have credentials to any production data store — this is enforced at
  the infrastructure level (Stage C), not just by policy.
