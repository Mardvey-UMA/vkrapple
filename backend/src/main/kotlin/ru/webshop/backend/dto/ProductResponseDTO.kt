package ru.webshop.backend.dto

import java.math.BigDecimal

data class ProductResponseDTO(
    val id: Long,
    val articleNumber: Long,
    val name: String,
    val price: BigDecimal,
    val rating: Double,
    val attributes: List<ProductAttributeDTO>,
    val categoryId: Long,
    val categoryName: String,
    val photos: List<String>,
    val reviews: List<ReviewDTO>
)
