package ru.webshop.backend.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "product")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "product_name", nullable = false)
    var name: String,

    @Column(name = "product_price", nullable = false)
    var price: BigDecimal,

    @Column(name = "balance_in_stock", nullable = false)
    val balanceInStock: Long,

    @Column(name = "article_number", nullable = false, unique = true)
    val articleNumber: Long = 0,

    @Column(name = "rating", nullable = true)
    var rating: Double,

    @Column(name = "description", nullable = true)
    var description: String,

    @Column(name = "number_of_orders")
    var numberOfOrders: Int = 0,

    // Связи

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    val category: Category,

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    val photos: List<PhotoProduct> = mutableListOf(),

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    val values: List<Value> = mutableListOf(),

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    val orderProducts: List<OrderProduct> = mutableListOf(),

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    val reviews: List<Review> = mutableListOf(),

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    val carts: List<Cart> = mutableListOf(),

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    val wishLists: List<WishList> = mutableListOf(),
    )
