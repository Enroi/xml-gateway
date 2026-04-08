package ru.ibs.transform.xml.configs

import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
class SecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http.cors({ c ->
            val source = CorsConfigurationSource { _: HttpServletRequest? ->
                val config = CorsConfiguration()
                config.allowedOriginPatterns = mutableListOf("http://localhost:*")
                config.allowedMethods = mutableListOf("POST", "GET", "OPTIONS")
                config.allowedHeaders = mutableListOf("*")
                config
            }
            c.configurationSource(source)
        })
            .csrf({ c -> c.disable() })
            .authorizeHttpRequests({ c -> c.anyRequest().permitAll() })
            .build()
}
