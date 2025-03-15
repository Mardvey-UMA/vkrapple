package ru.webshop.backend.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "_user_role")
data class RoleUser (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    // Связи

    @ManyToOne
    @JoinColumn(name = "role_id")
    val role: Role,

    @ManyToOne
    @JoinColumn(name = "_user_id")
    val user: User,
)
