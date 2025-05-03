package ru.webshop.backend.config

import jakarta.transaction.Transactional
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import ru.webshop.backend.entity.Role
import ru.webshop.backend.entity.User
import ru.webshop.backend.repository.RoleRepository
import ru.webshop.backend.repository.UserRepository

@Configuration
class AdminConfiguration(
    private val userDetailsService: UserDetailsService,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Bean
    fun daoAuthenticationProvider(
        passwordEncoder: PasswordEncoder
    ): AuthenticationProvider =
        DaoAuthenticationProvider().apply {
            setUserDetailsService(userDetailsService)
            setPasswordEncoder(passwordEncoder)
        }

    @Bean
    @Transactional
    fun initAdmin(
    ) = CommandLineRunner {
        if (!userRepository.existsByLogin("admin")) {
            val adminRole = roleRepository.findRoleByRoleName("ADMIN")
                ?: roleRepository.save(Role(roleName = "ADMIN", description = "Admin role"))

            userRepository.save(
                User(
                    telegramId = -1,
                    chatId = -1,
                    enabled = true,
                    accountLocked = false,
                    login = "admin",
                    passwordHash = passwordEncoder.encode("admin"),
                    roles = mutableSetOf(adminRole)
                )
            )
        }
    }
}