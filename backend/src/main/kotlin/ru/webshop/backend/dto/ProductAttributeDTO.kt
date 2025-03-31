package ru.webshop.backend.dto

import io.swagger.v3.oas.annotations.media.Schema

data class ProductAttributeDTO(
    @Schema(description = "ID атрибута", example = "3")
    val attributeId: Long,

    @Schema(description = "Название атрибута", example = "Цвет")
    val attributeName: String,

    @Schema(
        description = "Значение атрибута",
        example = "Синий"
    )
    val value: String
)
