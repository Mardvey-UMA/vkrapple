package ru.webshop.backend.security

import jakarta.transaction.Transactional
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.webshop.backend.repository.UserRepository

@Service
class UserDetailServiceImpl(
    private val userRepository: UserRepository
): UserDetailsService {

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(loginOrTelegramId: String): UserDetails {
        val user = if (loginOrTelegramId.all { it.isDigit() }) {
            userRepository.findByTelegramId(loginOrTelegramId.toLong())
        } else {
            userRepository.findByLogin(loginOrTelegramId)
        } ?: throw UsernameNotFoundException("User not found")

        return user
    }
}