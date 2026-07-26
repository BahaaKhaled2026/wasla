package com.wasla.api.infrastructure.id

import java.time.Instant

/**
 * Every mutable aggregate has version/created_at/updated_at (rule 8.8). Updates
 * require If-Match against `version`; this is the shared shape repositories and
 * API mappers build on so the convention can't silently be skipped per-entity.
 */
interface VersionedAggregate {
    val id: String
    val version: Long
    val createdAt: Instant
    val updatedAt: Instant
}
