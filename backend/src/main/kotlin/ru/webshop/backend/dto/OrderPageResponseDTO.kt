package ru.webshop.backend.dto

import io.swagger.v3.oas.annotations.media.Schema

data class OrderPageResponseDTO(
    @Schema(description = "Список заказов")
    val orders: List<OrderDTO>,

    @Schema(
        description = "Текущая страница",
        example = "1",
        minimum = "0"
    )
    val currentPage: Int,

    @Schema(
        description = "Всего страниц",
        example = "3",
        minimum = "0"
    )
    val totalPages: Int,

    @Schema(
        description = "Общее количество заказов",
        example = "25",
        minimum = "0"
    )
    val totalOrders: Long
)
