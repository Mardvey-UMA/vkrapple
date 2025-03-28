package ru.webshop.backend.dto

import java.math.BigDecimal
import java.time.Instant

data class WishListItemDTO(
    val id: Long,
    val articleNumber: Long,
    val productName: String,
    val price: BigDecimal,
    val addDate: Instant
)
