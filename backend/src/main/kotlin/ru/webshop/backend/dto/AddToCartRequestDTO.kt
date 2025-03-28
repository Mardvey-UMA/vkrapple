package ru.webshop.backend.dto

data class AddToCartRequestDTO(
    val articleNumber: Long,
    val quantity: Int
)
