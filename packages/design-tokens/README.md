# @wasla/design-tokens

Single semantic token source, generated to CSS custom properties, a TypeScript
module, and a Dart class — per rule 10.2. Never hand-edit anything under `build/`;
it's regenerated.

## Build

npm install
npm run build

Outputs:

- build/css/tokens.css — consumed by admin_web, control_plane_web
- build/ts/tokens.ts — consumed by admin_web, control_plane_web
- build/dart/wasla_tokens.dart — consumed by family_school_flutter, driver_flutter

## Adding or changing a token

Edit the relevant file under tokens/\*.json, then npm run build. Never add a token
outside the ten groups defined in the implementation guide §10.2 (surface, content,
brand, status, border, focus, spacing, radius, typography, motion, breakpoint)
without discussing it first — the fixed group list is what keeps all three
platforms semantically aligned.

## RTL / LTR rule (non-negotiable, rule 10.2)

These tokens describe _values_, not direction. Directionality is a _consumption_
rule enforced in each app's own component code, not baked into the tokens
themselves:

- **Web**: use CSS logical properties (`margin-inline-start`, `padding-inline-end`,
  `inset-inline-start`) — never `margin-left`/`margin-right` with a fixed side.
- **Flutter**: use `EdgeInsetsDirectional` and `AlignmentDirectional` — never
  `EdgeInsets.only(left: ...)` or `Alignment.centerLeft` for anything that should
  flip in RTL (Arabic).

A token like `spacing.4` is direction-agnostic on purpose — it's a magnitude, and
the _side_ it applies to is decided by the component using logical/directional
APIs at the call site.
