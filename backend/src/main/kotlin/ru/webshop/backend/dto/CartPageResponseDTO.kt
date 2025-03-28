package ru.webshop.backend.dto

data class CartPageResponseDTO(
    val items: List<CartItemDTO>,
    val currentPage: Int,
    val totalPages: Int,
    val totalItems: Long
)
