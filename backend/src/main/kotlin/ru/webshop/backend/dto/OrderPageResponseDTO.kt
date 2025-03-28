package ru.webshop.backend.dto

data class OrderPageResponseDTO(
    val orders: List<OrderDTO>,
    val currentPage: Int,
    val totalPages: Int,
    val totalOrders: Long
)
