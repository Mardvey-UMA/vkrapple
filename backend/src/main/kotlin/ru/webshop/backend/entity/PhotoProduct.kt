package ru.webshop.backend.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(
    name = "photo_product",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uc_photoproduct_product_index",
            columnNames = ["product_id", "index_number"]
        )
    ])
data class PhotoProduct (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "index_number", unique = true, nullable = false)
    var indexNumber: Int = 0,

    // Связи

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: Product,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    val photo: Photo
)