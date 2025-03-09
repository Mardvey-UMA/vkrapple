package ru.webshop.backend.dto

import ru.webshop.backend.entity.User
import java.time.Instant

data class UserDTO(
    val id: Long,
    val username: String,
    val telegramId: Long,
    val createdAt: Instant
)
//{
//    companion object {
//        fun fromEntity(user: User): UserDTO = UserDTO(
//            id = user.id,
//            username = user.username,
//            telegramId = user.telegramId,
//            createdAt = user.createdAt
//        )
//    }
//}
