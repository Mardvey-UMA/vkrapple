package ru.webshop.backend.dto

data class CategoryAttributesResponseDTO(
    val categoryId: Long,
    val categoryName: String,
    val attributes: List<AttributeDTO>
)