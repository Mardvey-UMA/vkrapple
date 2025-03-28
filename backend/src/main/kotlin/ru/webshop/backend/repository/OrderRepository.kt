package ru.webshop.backend.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import ru.webshop.backend.entity.Order
import ru.webshop.backend.entity.User

interface OrderRepository: JpaRepository<Order, Long> {
    fun findAllByUserOrderByCreatedAtDesc(user: User, pageable: Pageable): Page<Order>
    fun findByIdAndUser(id: Long, user: User): Order?
}