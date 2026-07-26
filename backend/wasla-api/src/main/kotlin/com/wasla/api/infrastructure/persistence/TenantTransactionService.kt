package com.wasla.api.infrastructure.persistence

import com.wasla.api.infrastructure.context.TenantMembership
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import javax.sql.DataSource

/**
 * Single infrastructure service responsible for tenant transaction initialization,
 * per rule 8.5. Every write against the tenant database goes through this — never
 * a bare tenantJdbcTemplate call from application code.
 *
 * Sequence: begin transaction -> SET LOCAL role/search_path/app.tenant_id, all
 * three server-resolved -> execute the given block -> commit (or roll back on
 * exception, standard Spring transaction semantics).
 */
@Service
class TenantTransactionService(
    private val tenantTransactionTemplate: TransactionTemplate,
    @org.springframework.beans.factory.annotation.Qualifier("tenantJdbcTemplate")
    private val tenantJdbcTemplate: NamedParameterJdbcTemplate,
) {
    fun <T> execute(membership: TenantMembership, work: (NamedParameterJdbcTemplate) -> T): T =
        tenantTransactionTemplate.execute {
            tenantJdbcTemplate.jdbcTemplate.execute(
                "SET LOCAL ROLE ${quoteIdentifier(membership.roleName)}",
            )
            tenantJdbcTemplate.jdbcTemplate.execute(
                "SET LOCAL search_path = ${quoteIdentifier(membership.schemaName)}, platform_shared",
            )
            tenantJdbcTemplate.jdbcTemplate.execute(
                "SET LOCAL app.tenant_id = '${membership.tenantId.replace("'", "''")}'",
            )
            work(tenantJdbcTemplate)
        } ?: error("Tenant transaction returned null unexpectedly")

    /**
     * Identifiers here are server-resolved (never raw client input), but we still
     * quote defensively rather than trust that invariant silently — a schema/role
     * name containing a double-quote would otherwise be a self-inflicted SQL
     * injection via our own registry data.
     */
    private fun quoteIdentifier(identifier: String): String =
        "\"${identifier.replace("\"", "\"\"")}\""
}
