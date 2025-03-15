package ru.webshop.backend.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "value")
data class Value(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "value", nullable = false)
    val value: String,

    // Связи

    @ManyToOne
    @JoinColumn(name = "attribute_id")
    val attribute: Attribute,

    @ManyToOne
    @JoinColumn(name = "product_id")
    val product: Product,
)
