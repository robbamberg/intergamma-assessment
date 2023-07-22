package com.robbamberg.intergammaassessment

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

/**
 * This file is only there for demo purposes!
 * It is disabling both CSRF and Basic Auth on all requests and should never be used in production
 * File needs to be removed if this ever needs to be production ready
 */

@Configuration
class SecurityConfiguration {
    @Bean
    @Throws(Exception::class)
    @Suppress("DEPRECATION")
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .httpBasic().disable()
            .csrf().disable()
            .build()
    }
}