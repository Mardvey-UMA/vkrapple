package ru.webshop.backend.service.impl

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.webshop.backend.dto.AddToCartRequestDTO
import ru.webshop.backend.dto.CartItemDTO
import ru.webshop.backend.dto.CartPageResponseDTO
import ru.webshop.backend.entity.CartItem
import ru.webshop.backend.entity.Product
import ru.webshop.backend.entity.User
import ru.webshop.backend.exception.GlobalExceptionHandler
import ru.webshop.backend.repository.CartItemRepository
import ru.webshop.backend.repository.ProductRepository
import ru.webshop.backend.repository.UserRepository
import ru.webshop.backend.service.interfaces.CartService

@Service
class CartServiceImpl(
    private val productRepository: ProductRepository,
    private val cartItemRepository: CartItemRepository,
    private val userRepository: UserRepository
): CartService {

    // Добавляет товар в корзину, если товар уже есть то прибавит количества
    // (убавить из корзины тогда можно будет передав quantity как -1)
    override fun addToCart(telegramId: Long, request: AddToCartRequestDTO) {
        val user: User = userRepository.findByTelegramId(telegramId)
            ?: throw GlobalExceptionHandler.UserNotFoundException("User with telegram id $telegramId not found")
        val product: Product = productRepository.findByArticleNumber(request.articleNumber)
            ?: throw GlobalExceptionHandler.ProductNotFoundException("Product not found")
        val cartItem: CartItem? = cartItemRepository.findByUserAndProductArticleNumber(user, request.articleNumber)
        cartItem?.let {
            if (it.quantity + request.quantity > 0) it.quantity += request.quantity // если вдруг закинут слишком большое отрицательное
            else cartItemRepository.deleteByUserAndProductArticleNumber(user, request.articleNumber)
            cartItemRepository.save(it)
        } ?: run {
            var validatedQuantity = 1
            if (request.quantity > 0) {
                validatedQuantity = request.quantity
            }
            val newItem = CartItem(
                product = product,
                user = user,
                quantity = validatedQuantity
            )
            cartItemRepository.save(newItem)
        }
    }

    // Удалить из корзины товар
    override fun removeFromCart(telegramId: Long, articleNumber: Long) {
        val user: User = userRepository.findByTelegramId(telegramId)
            ?: throw GlobalExceptionHandler.UserNotFoundException("User with telegram id $telegramId not found")
        val product: Product = productRepository.findByArticleNumber(articleNumber)
            ?: throw GlobalExceptionHandler.ProductNotFoundException("Product not found")
        val cartItem: CartItem? = cartItemRepository.findByUserAndProductArticleNumber(user, articleNumber)
        cartItem?.let { cartItemRepository.deleteByUserAndProductArticleNumber(user, articleNumber) }
    }

    override fun getCart(telegramId: Long, pageable: Pageable): CartPageResponseDTO {
        val user: User = userRepository.findByTelegramId(telegramId)
            ?: throw GlobalExceptionHandler.UserNotFoundException("User with telegram id $telegramId not found")
        val page = cartItemRepository.findAllByUser(user, pageable)
        return CartPageResponseDTO(
            items = page.content.map { it.toDTO() },
            currentPage = page.number,
            totalPages = page.totalPages,
            totalItems = page.totalElements
        )
    }

    private fun CartItem.toDTO() = CartItemDTO(
        id = id,
        articleNumber = product.articleNumber,
        productName = product.name,
        price = product.price,
        quantity = quantity,
        addDate = addDate
    )
}