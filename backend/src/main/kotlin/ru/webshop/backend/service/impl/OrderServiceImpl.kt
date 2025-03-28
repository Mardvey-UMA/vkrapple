package ru.webshop.backend.service.impl

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.webshop.backend.dto.CreateOrderRequestDTO
import ru.webshop.backend.dto.OrderDTO
import ru.webshop.backend.dto.OrderItemDTO
import ru.webshop.backend.dto.OrderPageResponseDTO
import ru.webshop.backend.entity.Order
import ru.webshop.backend.entity.OrderProduct
import ru.webshop.backend.entity.User
import ru.webshop.backend.enums.OrderStatus
import ru.webshop.backend.exception.GlobalExceptionHandler
import ru.webshop.backend.repository.CartItemRepository
import ru.webshop.backend.repository.OrderRepository
import ru.webshop.backend.repository.ProductRepository
import ru.webshop.backend.repository.UserRepository
import ru.webshop.backend.service.interfaces.OrderService
import java.math.BigDecimal

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val cartItemRepository: CartItemRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) : OrderService {
    override fun createOrder(telegramId: Long, request: CreateOrderRequestDTO): OrderDTO {
        val user: User = userRepository.findByTelegramId(telegramId)
            ?: throw GlobalExceptionHandler.UserNotFoundException("User with telegram id $telegramId not found")
        val cartItems = cartItemRepository.findAllByUser(user)

        cartItems.forEach { item ->
            if (item.product.balanceInStock < item.quantity) {
                throw IllegalStateException(item.product.name)
            }
        }

        cartItems.forEach { item ->
            item.product.balanceInStock -= item.quantity
            item.product.numberOfOrders += item.quantity
            productRepository.save(item.product)
        }

        val order = Order(
            user = user,
            status = OrderStatus.IN_PROGRESS,
            orderAmount = cartItems.sumOf { it.product.price * BigDecimal(it.quantity) },
            paymentMethod = request.paymentMethod,
            orderAddress = request.orderAddress,
            expectedDate = request.expectedDate
        ).apply {
            orderProducts = cartItems.map { cartItem ->
                OrderProduct(
                    product = cartItem.product,
                    order = this,
                    quantity = cartItem.quantity.toLong(),
                    amount = cartItem.product.price * BigDecimal(cartItem.quantity)
                )
            }.toMutableList()
        }

        val savedOrder = orderRepository.save(order)
        cartItemRepository.deleteAll(cartItems)

        return savedOrder.toDTO()
    }

    override fun cancelOrder(telegramId: Long, orderId: Long) {
        val user: User = userRepository.findByTelegramId(telegramId)
            ?: throw GlobalExceptionHandler.UserNotFoundException("User with telegram id $telegramId not found")
        val order = orderRepository.findByIdAndUser(orderId, user)
            ?: throw IllegalStateException("Order not found")

        if (order.status == OrderStatus.CANCELLED) {
            throw IllegalStateException("Order canceled")
        }

        order.orderProducts.forEach { item ->
            item.product.balanceInStock += item.quantity
            item.product.numberOfOrders -= item.quantity
            productRepository.save(item.product)
        }

        order.status = OrderStatus.CANCELLED
        orderRepository.save(order)
    }

    override fun getOrders(telegramId: Long, pageable: Pageable): OrderPageResponseDTO {
        val user: User = userRepository.findByTelegramId(telegramId)
            ?: throw GlobalExceptionHandler.UserNotFoundException("User with telegram id $telegramId not found")
        val page = orderRepository.findAllByUserOrderByCreatedAtDesc(user, pageable)
        return OrderPageResponseDTO(
            orders = page.content.map { it.toDTO() },
            currentPage = page.number,
            totalPages = page.totalPages,
            totalOrders = page.totalElements
        )
    }

    private fun Order.toDTO() = OrderDTO(
        id = id,
        status = status,
        orderAmount = orderAmount,
        paymentMethod = paymentMethod,
        orderAddress = orderAddress,
        expectedDate = expectedDate,
        createdAt = createdAt,
        items = orderProducts.map { it.toDTO() }
    )

    private fun OrderProduct.toDTO() = OrderItemDTO(
        articleNumber = product.articleNumber,
        productName = product.name,
        quantity = quantity,
        price = product.price
    )
}