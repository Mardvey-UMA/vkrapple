package ru.webshop.backend.dto

import io.swagger.v3.oas.annotations.media.Schema

data class CartPageResponseDTO(
    @Schema(description = "Список товаров в корзине")
    val items: List<CartItemDTO>,

    @Schema(description = "Текущая страница", example = "1")
    val currentPage: Int,

    @Schema(description = "Всего страниц", example = "3")
    val totalPages: Int,

    @Schema(description = "Общее количество товаров", example = "25")
    val totalItems: Long
)