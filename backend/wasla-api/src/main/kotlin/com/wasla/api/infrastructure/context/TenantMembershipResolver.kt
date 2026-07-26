package com.wasla.api.infrastructure.context

/**
 * Resolves a verified tenant registry row and role/schema for a given global
 * user + requested tenant. Schema and role names are server-owned (rule 8.5) —
 * never derived directly from a client-supplied {tenantId} path segment.
 */
fun interface TenantMembershipResolver {
    fun resolve(globalUserId: String, requestedTenantId: String): TenantMembership
}

data class TenantMembership(
    val tenantId: String,
    val schemaName: String,
    val roleName: String,
    val tenantStatus: String,
)
