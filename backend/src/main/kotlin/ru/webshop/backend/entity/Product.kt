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

    // Связи

    @ManyToOne
    @JoinColumn(name = "category_id")
    val category: Category,

    @OneToMany(mappedBy = "product")
    val photos: List<PhotoProduct> = mutableListOf(),

    @OneToMany(mappedBy = "product")
    val values: List<Value> = mutableListOf(),

    @OneToMany(mappedBy = "product")
    val orderProducts: List<OrderProduct> = mutableListOf(),

    @OneToMany(mappedBy = "product")
    val reviews: List<Review> = mutableListOf(),

    @OneToMany(mappedBy = "product")
    val carts: List<Cart> = mutableListOf(),

    @OneToMany(mappedBy = "product")
    val wishLists: List<WishList> = mutableListOf(),
    )
