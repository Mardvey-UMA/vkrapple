package ru.webshop.backend.dto

import io.swagger.v3.oas.annotations.media.Schema

data class CategoryAttributesResponseDTO(
    @Schema(description = "ID категории", example = "5")
    val categoryId: Long,

    @Schema(description = "Название категории", example = "Смартфоны")
    val categoryName: String,

    @Schema(description = "Список атрибутов категории")
    val attributes: List<AttributeDTO>
)