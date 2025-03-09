package ru.webshop.backend.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.webshop.backend.entity.User
import ru.webshop.backend.repository.UserRepository

@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    override fun createUser(telegramId: Long, chatId: Long, username: String?): User {
        if (userRepository.existsByTelegramId(telegramId)) {
            throw IllegalArgumentException("User with telegramId $telegramId already exists")
        }

        val user = User(
            username = username,
            telegramId = telegramId,
            chatId = chatId
        )

        return userRepository.save(user)
    }

    override fun findByTelegramId(telegramId: Long): User? {
        return userRepository.findByTelegramId(telegramId)
    }

    override fun existsByTelegramId(telegramId: Long): Boolean {
        return userRepository.existsByTelegramId(telegramId)
    }
}