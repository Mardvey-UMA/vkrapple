package ru.webshop.backend.dto

import java.math.BigDecimal

data class ProductCreateRequestDTO(
    val name: String,
    val price: BigDecimal,
    val balanceInStock: Long,
    val description: String?,
    val categoryId: Long,
    val attributes: Map<String, String>
)

