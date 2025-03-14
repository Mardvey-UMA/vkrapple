package ru.webshop.backend.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "product")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "product_name", nullable = false)
    var name: String,

    @Column(name = "product_price", nullable = false)
    var price: Double,

    @Column(name = "balance_in_stock", nullable = false)
    val balanceInStock: Long,

    @Column(name = "article_number", nullable = false)
    val articleNumber: Long,

    @Column(name = "rating", nullable = true)
    var rating: Double,

    @Column(name = "description", nullable = true)
    var description: String,

    @Column(name = "number_of_orders")
    var numberOfOrders: Int = 0,


    )
