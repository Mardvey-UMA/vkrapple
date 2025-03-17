package ru.webshop.backend.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(
    name = "product")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "product_name", nullable = false)
    var name: String,

    @Column(name = "product_price", nullable = false)
    var price: BigDecimal,

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

    // Связи

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    val category: Category,

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    val photos: List<PhotoProduct> = mutableListOf(),

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    val values: List<Value> = mutableListOf(),

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    val orderProducts: List<OrderProduct> = mutableListOf(),

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    val reviews: List<Review> = mutableListOf(),

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    val carts: List<Cart> = mutableListOf(),

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    val wishLists: List<WishList> = mutableListOf(),
    )
