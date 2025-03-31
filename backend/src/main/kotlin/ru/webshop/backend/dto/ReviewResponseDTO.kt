package ru.webshop.backend.dto

import java.math.BigDecimal

data class ReviewResponseDTO (
    val id: Long,
    val rating: Int,
    val text: String,
)
