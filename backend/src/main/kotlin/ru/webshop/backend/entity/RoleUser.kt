package ru.webshop.backend.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
data class RoleUser (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
)
