package com.wasla.api.infrastructure.outbox

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

/**
 * Writes an outbox row within the caller's existing transaction — per rule 8.9,
 * "write domain outbox and audit outbox" happens as part of the same transaction
 * as the repository work, never as a separate commit. Callers pass the JdbcTemplate
 * bound to whichever transaction is already active (control or tenant).
 */
class OutboxWriter {
    fun write(jdbcTemplate: NamedParameterJdbcTemplate, event: OutboxEvent) {
        val params = MapSqlParameterSource()
            .addValue("eventId", event.eventId)
            .addValue("aggregateType", event.aggregateType)
            .addValue("aggregateId", event.aggregateId)
            .addValue("eventType", event.eventType)
            .addValue("eventVersion", event.eventVersion)
            .addValue("payload", event.payloadJson)
            .addValue("correlationId", event.correlationId)
            .addValue("causationId", event.causationId)

        jdbcTemplate.update(
            """
            INSERT INTO outbox_event
                (event_id, aggregate_type, aggregate_id, event_type, event_version,
                 payload, correlation_id, causation_id)
            VALUES
                (:eventId, :aggregateType, :aggregateId, :eventType, :eventVersion,
                 :payload::jsonb, :correlationId, :causationId)
            """.trimIndent(),
            params,
        )
    }
}
