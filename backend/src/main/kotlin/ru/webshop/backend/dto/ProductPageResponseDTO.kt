package ru.webshop.backend.dto

data class ProductPageResponseDTO(
    val products: List<ProductResponseDTO>,
    val currentPage: Int,
    val totalPages: Int,
    val totalProducts: Long
)
