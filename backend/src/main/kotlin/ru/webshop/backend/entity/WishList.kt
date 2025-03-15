package ru.webshop.backend.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "wish_list")
data class WishList(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "add_date", nullable = false, updatable = false)
    val addDate: Instant = Instant.now(),

    // Связи

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: Product,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_user_id")
    val user: User
)
