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
    override fun loadUserByUsername(telegramId: String): UserDetails {
        val lltelegramId = telegramId.toLong()
        val user = userRepository.findByTelegramId(lltelegramId)
            ?: throw UsernameNotFoundException("User with email $telegramId not found")
        return user
    }
}