package ru.webshop.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.webshop.backend.entity.OrderProduct

interface OrderProductRepository: JpaRepository<OrderProduct, Long> {
}