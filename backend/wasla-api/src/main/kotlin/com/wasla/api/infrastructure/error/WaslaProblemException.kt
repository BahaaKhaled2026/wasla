package com.wasla.api.infrastructure.error

/**
 * Base for exceptions that map directly to a client-safe ProblemDetail. Anything
 * NOT extending this (a raw SQLException, NullPointerException, etc.) is treated
 * as unexpected and mapped to a generic safe 500 by the global handler - its real
 * message never reaches the client (rule 8.7: never expose SQL/stack traces).
 */
abstract class WaslaProblemException(
    val problemType: String,
    val problemCode: String,
    val httpStatus: Int,
    val safeDetail: String,
    val retryable: Boolean = false,
    val fields: List<ProblemDetail.FieldError> = emptyList(),
    cause: Throwable? = null,
) : RuntimeException(safeDetail, cause)
