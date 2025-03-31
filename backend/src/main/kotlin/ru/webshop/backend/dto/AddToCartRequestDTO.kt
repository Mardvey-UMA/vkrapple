package ru.webshop.backend.dto

import io.swagger.v3.oas.annotations.media.Schema

data class AddToCartRequestDTO(
    @Schema(
        description = "Артикул товара",
        example = "123456789",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val articleNumber: Long,

    @Schema(
        description = "Количество товара (может быть отрицательным для уменьшения)",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val quantity: Int
)
