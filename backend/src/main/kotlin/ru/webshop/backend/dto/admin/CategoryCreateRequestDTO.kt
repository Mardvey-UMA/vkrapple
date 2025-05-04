package ru.webshop.backend.controller.admin

data class CategoryCreateRequestDTO(
    val categoryName: String,
    val attributes: List<AttributeCreateRequestDTO> = emptyList()
)