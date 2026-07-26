package com.wasla.api.infrastructure.context

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.web.context.annotation.RequestScope

/**
 * Mutable holder, request-scoped by Spring. Exactly one RequestContext is set per
 * request, early in the filter chain (see WaslaRequestContextFilter), and read-only
 * from that point on for the life of the request.
 *
 * Open class/members: required for CGLIB to generate the request-scoped proxy
 * (TARGET_CLASS mode) — Kotlin classes and members are final by default.
 */
open class RequestContextHolder {
    private var backingContext: RequestContext? = null

    open fun get(): RequestContext? = backingContext

    open fun set(context: RequestContext) {
        check(backingContext == null) { "RequestContext already set for this request" }
        backingContext = context
    }

    open fun require(): RequestContext =
        checkNotNull(backingContext) { "RequestContext has not been set yet for this request" }
}

@Configuration
class RequestContextHolderConfig {
    @Bean
    @RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    fun requestContextHolder(): RequestContextHolder = RequestContextHolder()
}
