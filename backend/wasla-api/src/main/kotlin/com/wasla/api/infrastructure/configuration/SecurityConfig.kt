package com.wasla.api.infrastructure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain

/**
 * VS-00 baseline: explicitly permit only the health/status endpoints needed for
 * the walking skeleton. Deny-by-default (rule 1.4) for everything else - there
 * are no other endpoints yet, and real JWT-based authorization is wired in VS-02.
 */
@Configuration
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }
            authorizeHttpRequests {
                authorize("/actuator/health", permitAll)
                authorize("/actuator/health/**", permitAll)
                authorize("/api/v1/status", permitAll)
                authorize(anyRequest, denyAll)
            }
        }
        return http.build()
    }
}
