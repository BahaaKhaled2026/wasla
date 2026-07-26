package com.wasla.api.infrastructure.error

/**
 * application/problem+json shape per rule 8.7. Every error field here is
 * deliberately safe to show a client — no SQL, stack traces, Keycloak internals,
 * or cross-tenant existence leaks are ever placed in `detail`.
 */
data class ProblemDetail(
    val type: String,
    val title: String,
    val status: Int,
    val code: String,
    val detail: String,
    val instance: String,
    val requestId: String,
    val retryable: Boolean,
    val fields: List<FieldError> = emptyList(),
) {
    data class FieldError(
        val field: String,
        val code: String,
        val message: String,
    )
}
