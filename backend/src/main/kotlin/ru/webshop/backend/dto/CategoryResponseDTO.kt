package ru.webshop.backend.dto

import io.swagger.v3.oas.annotations.media.Schema

data class CategoryResponseDTO(
    @Schema(
        description = "Категории в формате: Название -> ID",
        example = "{\"Смартфоны\": 1, \"Ноутбуки\": 2}"
    )
    val categories: Map<String, Long>
)
