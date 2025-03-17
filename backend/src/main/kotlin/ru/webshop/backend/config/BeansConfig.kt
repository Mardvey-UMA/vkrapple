package ru.webshop.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService

@Configuration
class BeansConfig (
    val userDetailsService: UserDetailsService
){

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        return object : AuthenticationProvider {
            override fun authenticate(authentication: Authentication): Authentication {
                val user = userDetailsService.loadUserByUsername(authentication.name)
                return UsernamePasswordAuthenticationToken(
                    user, null, user.authorities
                )
            }

            override fun supports(authentication: Class<*>): Boolean {
                return authentication == UsernamePasswordAuthenticationToken::class.java
            }
        }
    }
    @Bean
    fun authenticationManager(
        config: AuthenticationConfiguration
    ) : AuthenticationManager =
        config.getAuthenticationManager()
}