package com.wasla.api.infrastructure.context

import java.util.Locale

/**
 * Immutable per-request context, per rule 8.6. Populated once per request from
 * verified sources only (JWT claims, server-resolved tenant membership, trusted
 * headers) — never trusted directly from unverified client input.
 */
data class RequestContext(
    val requestId: String,
    val correlationId: String,
    val traceId: String?,
    val globalUserId: String,
    val keycloakSubject: String,
    val sessionId: String?,
    val deviceId: String?,
    val tenantId: String?,
    val activeRolePreference: String?,
    val locale: Locale,
    val assuranceLevel: String?,
    val sourceApplication: String,
    val clientVersion: String?,
) {
    /**
     * Deliberately no toString() override exposing raw fields by default beyond
     * what data class generates — see loggableSummary() for the log-safe subset.
     * (rule 8.6: no raw access tokens or phone numbers in logs — this class never
     * holds either, so this note documents the invariant, not a fix for it.)
     */
    fun loggableSummary(): String =
        "RequestContext(requestId=$requestId, correlationId=$correlationId, " +
            "globalUserId=$globalUserId, tenantId=$tenantId, sourceApplication=$sourceApplication)"
}
