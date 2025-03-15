package ru.webshop.backend.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import ru.webshop.backend.enums.OrderStatus
import ru.webshop.backend.enums.PaymentMethods
import java.math.BigDecimal
import java.time.Instant

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(
    name = "_order",
    indexes = [
        Index(name = "idx_order_user", columnList = "user_id"),
        Index(name = "idx_order_created", columnList = "created_at"),
        Index(name = "idx_order_status", columnList = "status")
    ])
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "status", nullable = false)
    var status: OrderStatus,

    @Column(name = "order_amount", nullable = false)
    val orderAmount: BigDecimal,

    @Column(name = "payment_method", nullable = false)
    val paymentMethod: PaymentMethods,

    @Column(name = "order_address", nullable = false)
    val orderAddress: String,

    @Column(name = "expected_date", nullable = false)
    var expectedDate: Instant,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),

    // Связи

    @OneToMany(mappedBy = "_order")
    val orderProducts: MutableList<OrderProduct> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_user_id")
    val user: User,
)
