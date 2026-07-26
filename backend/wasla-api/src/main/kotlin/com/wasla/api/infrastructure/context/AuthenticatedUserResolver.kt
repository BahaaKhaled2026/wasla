package com.wasla.api.infrastructure.context

/** Resolves the authenticated global user for the current request. */
fun interface AuthenticatedUserResolver {
    fun resolve(): AuthenticatedUser
}

data class AuthenticatedUser(
    val globalUserId: String,
    val keycloakSubject: String,
    val assuranceLevel: String?,
)
