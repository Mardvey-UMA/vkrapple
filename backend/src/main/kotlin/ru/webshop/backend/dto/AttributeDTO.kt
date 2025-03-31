package ru.webshop.backend.dto

import io.swagger.v3.oas.annotations.media.Schema

data class AttributeDTO(
    @Schema(description = "ID атрибута", example = "5")
    val id: Long,

    @Schema(description = "Название атрибута", example = "Цвет")
    val name: String,

    @Schema(
        description = "Допустимые значения атрибута",
        example = "[\"Красный\", \"Синий\"]"
    )
    val values: List<String>
)