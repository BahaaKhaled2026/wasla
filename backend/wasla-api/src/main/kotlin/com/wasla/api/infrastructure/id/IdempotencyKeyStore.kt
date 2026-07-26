package com.wasla.api.infrastructure.id

/**
 * Backs the required Idempotency-Key header on mutations (rule 8.8/9.3). Real
 * Redis-backed implementation lands with the first mutating endpoint (VS-01);
 * this interface establishes the contract now so API-layer code can be written
 * against it without waiting.
 */
interface IdempotencyKeyStore {
    /** Returns the previously stored response for this key, if any. */
    fun get(idempotencyKey: String): StoredResponse?

    /** Stores a response the first time a given key is used. */
    fun put(idempotencyKey: String, response: StoredResponse)

    data class StoredResponse(
        val statusCode: Int,
        val body: String,
        val contentType: String,
    )
}
