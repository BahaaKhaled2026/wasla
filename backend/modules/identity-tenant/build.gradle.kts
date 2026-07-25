// identity-tenant module — hexagonal layout.
// Domain layer intentionally has zero Spring dependency (see repo rule 4.1).
// Application/infrastructure/api layers gain scoped dependencies as VS-01 needs them.

dependencies {
    // Dependencies added incrementally as each hexagonal layer is implemented.
}
