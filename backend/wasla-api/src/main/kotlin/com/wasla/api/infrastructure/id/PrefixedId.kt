package com.wasla.api.infrastructure.id

import com.github.f4b6a3.uuid.UuidCreator

/**
 * Generates API-facing IDs per rule 8.8: internally a UUIDv7 (time-ordered,
 * unlike UUIDv4 - better index locality), externally prefixed by resource type
 * so IDs are self-describing in logs, URLs, and support tickets
 * (e.g. "ten_01926a3c-...", "usr_01926a3c-...").
 */
object PrefixedId {
    fun generate(prefix: String): String {
        require(prefix.isNotBlank()) { "ID prefix must not be blank" }
        return "${prefix}_${UuidCreator.getTimeOrderedEpoch()}"
    }
}

/** Standard resource prefixes used across the platform (rule 8.8). */
object ResourcePrefix {
    const val TENANT = "ten"
    const val USER = "usr"
    const val MEMBERSHIP = "mem"
    const val INVITATION = "inv"
    const val IMPORT_JOB = "imp"
}
