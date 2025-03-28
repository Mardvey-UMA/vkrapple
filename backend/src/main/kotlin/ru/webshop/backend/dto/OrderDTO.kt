package ru.webshop.backend.dto

import ru.webshop.backend.enums.OrderStatus
import ru.webshop.backend.enums.PaymentMethods
import java.math.BigDecimal
import java.time.Instant

data class OrderDTO(
    val id: Long,
    val status: OrderStatus,
    val orderAmount: BigDecimal,
    val paymentMethod: PaymentMethods,
    val orderAddress: String,
    val expectedDate: Instant,
    val createdAt: Instant,
    val items: List<OrderItemDTO>
)
