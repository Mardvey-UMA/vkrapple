package ru.webshop.backend.dto

import io.swagger.v3.oas.annotations.media.Schema

data class PhotoUploadRequestDTO(
    @Schema(
        description = "Артикул товара",
        example = "987654321",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val articleNumber: Long,

    @Schema(
        description = "Порядковый номер фото (от 1 до 5)",
        example = "1",
        minimum = "1",
        maximum = "5"
    )
    val indexNumber: Int
)