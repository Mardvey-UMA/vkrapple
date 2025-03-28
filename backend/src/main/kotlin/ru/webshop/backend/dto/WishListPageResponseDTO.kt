package ru.webshop.backend.dto

data class WishListPageResponseDTO(
    val items: List<WishListItemDTO>,
    val currentPage: Int,
    val totalPages: Int,
    val totalItems: Long
)
