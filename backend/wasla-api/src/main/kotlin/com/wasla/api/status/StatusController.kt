package com.wasla.api.status

import com.wasla.api.infrastructure.context.RequestContextHolder
import io.micrometer.observation.Observation
import io.micrometer.observation.ObservationRegistry
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

data class StatusResponse(
    val requestId: String,
    val buildRevision: String,
    val serverTime: Instant,
    val health: HealthSummary,
)

data class HealthSummary(
    val status: String,
    val components: Map<String, String>,
)

/**
 * Fulfils rule 6 (VS-00 observable outcome): requestId, buildRevision, serverTime,
 * and health with a real database test query against both datasources - each
 * wrapped in its own Observation span (rule 6: "a trace connects browser/mobile
 * request, gateway, API, database test query, and response").
 */
@RestController
@RequestMapping("/api/v1")
class StatusController(
    private val requestContextHolder: RequestContextHolder,
    private val observationRegistry: ObservationRegistry,
    @Qualifier("controlJdbcTemplate") private val controlJdbcTemplate: NamedParameterJdbcTemplate,
    @Qualifier("tenantJdbcTemplate") private val tenantJdbcTemplate: NamedParameterJdbcTemplate,
    @Value("\${wasla.build-revision:local-dev}") private val buildRevision: String,
) {
    @GetMapping("/status")
    fun status(): StatusResponse {
        val components = linkedMapOf(
            "controlDatabase" to checkDatabase("control", controlJdbcTemplate),
            "tenantDatabase" to checkDatabase("tenant", tenantJdbcTemplate),
        )
        val overall = if (components.values.all { it == "UP" }) "UP" else "DOWN"

        return StatusResponse(
            requestId = requestContextHolder.get()?.requestId ?: "req_unknown",
            buildRevision = buildRevision,
            serverTime = Instant.now(),
            health = HealthSummary(status = overall, components = components),
        )
    }

    private fun checkDatabase(name: String, jdbcTemplate: NamedParameterJdbcTemplate): String {
        var result = "DOWN"
        Observation.createNotStarted("db.status_check", observationRegistry)
            .lowCardinalityKeyValue("db.name", name)
            .observe {
                result = try {
                    jdbcTemplate.jdbcTemplate.queryForObject("SELECT 1", Int::class.java)
                    "UP"
                } catch (ex: Exception) {
                    "DOWN"
                }
            }
        return result
    }
}
