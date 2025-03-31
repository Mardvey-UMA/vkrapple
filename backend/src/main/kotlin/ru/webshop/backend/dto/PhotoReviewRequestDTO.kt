package ru.webshop.backend.dto

import io.swagger.v3.oas.annotations.media.Schema

data class PhotoReviewRequestDTO(
    @Schema(
        description = "ID отзыва",
        example = "42",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val reviewId: Long,

    @Schema(
        description = "Порядковый номер фото (от 1 до 5)",
        example = "1",
        minimum = "1",
        maximum = "5"
    )
    val indexNumber: Int
)
