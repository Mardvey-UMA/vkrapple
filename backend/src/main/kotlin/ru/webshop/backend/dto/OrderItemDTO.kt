package ru.webshop.backend.dto

import java.math.BigDecimal

data class OrderItemDTO(
    val articleNumber: Long,
    val productName: String,
    val quantity: Long,
    val price: BigDecimal
)
