package ru.webshop.backend.dto

import ru.webshop.backend.enums.PaymentMethods
import java.time.Instant

data class CreateOrderRequestDTO(
    val paymentMethod: PaymentMethods,
    val orderAddress: String,
    val expectedDate: Instant
)
