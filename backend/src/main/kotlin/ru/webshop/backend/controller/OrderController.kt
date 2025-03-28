package ru.webshop.backend.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.webshop.backend.dto.CreateOrderRequestDTO
import ru.webshop.backend.dto.OrderDTO
import ru.webshop.backend.entity.User
import ru.webshop.backend.service.interfaces.OrderService

@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping("/create")
    fun createOrder(@RequestBody createOrderRequestDTO: CreateOrderRequestDTO,
                    @RequestHeader("X-Telegram-User-Id") telegramId: Long) : OrderDTO
        = orderService.createOrder(telegramId, createOrderRequestDTO)

    @DeleteMapping("/cancel")
    fun cancelOrder(@RequestParam("orderId") orderId: Long,
                    @RequestHeader("X-Telegram-User-Id") telegramId: Long){
        orderService.cancelOrder(telegramId, orderId)
    }

    @GetMapping("/list")
    fun getOrders(@PageableDefault(size = 20) pageable: Pageable,
        @RequestHeader("X-Telegram-User-Id") telegramId: Long)
    = orderService.getOrders(telegramId, pageable)

}