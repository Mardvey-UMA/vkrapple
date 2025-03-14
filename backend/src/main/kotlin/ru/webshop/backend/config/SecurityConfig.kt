package ru.webshop.backend.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter
import ru.webshop.backend.filters.TelegramAuthFilter
import ru.webshop.backend.filters.TelegramHeaderFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val objectMapper: ObjectMapper,
    @Value("\${telegram.bot.token}")
    private val botToken: String,
    private val telegramHeaderFilter: TelegramHeaderFilter
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .addFilterBefore(
                TelegramAuthFilter(objectMapper, botToken),
                AnonymousAuthenticationFilter::class.java
            )
            .addFilterAfter(telegramHeaderFilter, TelegramAuthFilter::class.java)
            .authorizeHttpRequests { auth ->
                auth.anyRequest().authenticated()
            }
            .exceptionHandling { exceptions ->
                exceptions.authenticationEntryPoint { request, response, _ ->
                    response.sendError(401, "Unauthorized")
                }
            }
            .csrf { it.disable() }
            .cors { }
        return http.build()
    }
}