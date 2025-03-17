package ru.webshop.backend.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import ru.webshop.backend.entity.User

interface UserRepository: JpaRepository<User, Long> {
    @EntityGraph(attributePaths = ["roles"])
    fun findByTelegramId(telegramId: Long): User?

    @EntityGraph(attributePaths = ["roles"])
    fun existsByTelegramId(telegramId: Long): Boolean
}