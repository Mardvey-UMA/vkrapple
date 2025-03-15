package ru.webshop.backend.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import ru.webshop.backend.enums.Roles

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "role")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "role_name")
    val roleName: Roles,

    @Column(name = "description")
    val description: String,

    // Связи

        // Роли

        @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "roles")
        @JsonIgnore
        private var users: MutableList<User> = mutableListOf(),
)
