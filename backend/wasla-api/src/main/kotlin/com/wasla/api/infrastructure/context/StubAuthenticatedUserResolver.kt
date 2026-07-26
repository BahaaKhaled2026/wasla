package com.wasla.api.infrastructure.context

import org.springframework.stereotype.Component

/**
 * VS-00 placeholder. Real implementation lands in VS-02 (auth context) and
 * verifies a Keycloak-issued JWT rather than returning a fixed identity.
 * Deliberately loud about being a stub so it cannot be mistaken for production
 * behavior if this code is ever reached outside local/dev use.
 */
@Component
class StubAuthenticatedUserResolver : AuthenticatedUserResolver {
    override fun resolve(): AuthenticatedUser =
        AuthenticatedUser(
            globalUserId = "stub-user-vs00",
            keycloakSubject = "stub-subject-vs00",
            assuranceLevel = null,
        )
}
