package com.wasla.api.infrastructure.context

import org.springframework.stereotype.Component

/**
 * VS-00 placeholder. Real implementation lands in VS-01 (tenant provisioning)
 * and queries the tenant registry table in wasla_control — which does not exist
 * yet, since no migrations have been written.
 */
@Component
class StubTenantMembershipResolver : TenantMembershipResolver {
    override fun resolve(globalUserId: String, requestedTenantId: String): TenantMembership =
        TenantMembership(
            tenantId = requestedTenantId,
            schemaName = "public",
            roleName = "wasla",
            tenantStatus = "stub",
        )
}
