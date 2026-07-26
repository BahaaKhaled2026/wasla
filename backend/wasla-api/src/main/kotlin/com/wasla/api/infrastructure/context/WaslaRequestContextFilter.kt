package com.wasla.api.infrastructure.context

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.Locale
import java.util.UUID

@Component
class WaslaRequestContextFilter(
    private val requestContextHolder: RequestContextHolder,
    private val authenticatedUserResolver: AuthenticatedUserResolver,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val user = authenticatedUserResolver.resolve()
        val requestId = "req_${UUID.randomUUID()}"

        val context = RequestContext(
            requestId = requestId,
            correlationId = request.getHeader("X-Correlation-Id") ?: requestId,
            traceId = request.getHeader("traceparent"),
            globalUserId = user.globalUserId,
            keycloakSubject = user.keycloakSubject,
            sessionId = request.getHeader("X-Session-Id"),
            deviceId = request.getHeader("X-Device-Id"),
            tenantId = request.getHeader("X-Tenant-Id"),
            activeRolePreference = request.getHeader("X-Active-Role-Preference"),
            locale = request.locale ?: Locale.ENGLISH,
            assuranceLevel = user.assuranceLevel,
            sourceApplication = request.getHeader("X-Source-Application") ?: "unknown",
            clientVersion = request.getHeader("X-Client-Version"),
        )

        requestContextHolder.set(context)
        response.setHeader("X-Request-Id", requestId)
        filterChain.doFilter(request, response)
    }
}
