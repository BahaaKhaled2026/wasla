package com.wasla.api.infrastructure.outbox

import java.time.Instant
import java.util.UUID

/** Mirrors the outbox_event table shape exactly (rule 8.9). */
data class OutboxEvent(
    val eventId: UUID,
    val aggregateType: String,
    val aggregateId: String,
    val eventType: String,
    val eventVersion: Int,
    val payloadJson: String,
    val correlationId: UUID,
    val causationId: UUID?,
)
