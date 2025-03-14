package ru.webshop.backend.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Table(
    name = "_user",
    indexes = [
        Index(name = "idx_telegram_id", columnList = "telegram_id"),
        Index(name = "idx_username", columnList = "username")
    ])
@EntityListeners(AuditingEntityListener::class)
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(length = 64, nullable = true, unique = true)
    var username: String? = null,

    @Column(name = "telegram_id", nullable = false, unique = true)
    val telegramId: Long,

    @Column(name = "chat_id", nullable = false, unique = true)
    val chatId: Long,

    @Column(name = "phone_number", nullable = true, unique = true)
    val phoneNumber: String? = null,

    @Column(name = "first_name", nullable = true, unique = true)
    val firstName: String? = null,

    @Column(name = "last_name", nullable = true, unique = true)
    val lastName: String? = null,

    @Column(name = "email", nullable = true, unique = true)
    val email: String? = null,

    @Column(name = "address", nullable = true)
    val address: String? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()
)