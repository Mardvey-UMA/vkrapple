package ru.webshop.backend.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

data class OrderItemDTO(
    @Schema(description = "Артикул товара", example = "123456789")
    val articleNumber: Long,

    @Schema(description = "Название товара", example = "Смартфон Xiaomi")
    val productName: String,

    @Schema(description = "Количество товара", example = "2")
    val quantity: Long,

    @Schema(
        description = "Цена за единицу товара",
        example = "19999.99",
        type = "number"
    )
    val price: BigDecimal
)
