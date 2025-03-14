package ru.webshop.backend.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import ru.webshop.backend.enums.Roles

@Entity
@EntityListeners(AuditingEntityListener::class)
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "role_name")
    val roleName: Roles,

    @Column(name = "description")
    val description: String,
)
