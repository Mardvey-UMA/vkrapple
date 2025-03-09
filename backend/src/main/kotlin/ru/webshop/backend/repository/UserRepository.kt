package ru.webshop.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.webshop.backend.entity.User

interface UserRepository: JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun findByTelegramId(telegramId: Long): User?
    fun existsByTelegramId(telegramId: Long): Boolean
}