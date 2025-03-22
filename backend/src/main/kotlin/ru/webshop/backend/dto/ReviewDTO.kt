package ru.webshop.backend.dto

import java.time.Instant

data class ReviewDTO(
    val id: Long?,
    val rating: Int,
    val text: String,
    val createdAt: Instant?,
    val userName: String?
)
