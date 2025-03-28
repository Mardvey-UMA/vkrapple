package ru.webshop.backend.controller

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.webshop.backend.dto.AddToCartRequestDTO
import ru.webshop.backend.dto.CartItemDTO
import ru.webshop.backend.dto.CartPageResponseDTO
import ru.webshop.backend.service.interfaces.CartService

@RestController
@RequestMapping("/api/cart")
class CartController(
    private val cartService: CartService,
) {

    @PostMapping("/add")
    fun addToCart(@RequestBody request: AddToCartRequestDTO,
                  @RequestHeader("X-Telegram-User-Id") telegramId: Long){
        cartService.addToCart(telegramId, request)
    }

    @DeleteMapping("/remove")
    fun removeFromCart(@RequestParam("article") articleNumber: Long,
                       @RequestHeader("X-Telegram-User-Id") telegramId: Long){
        cartService.removeFromCart(articleNumber, telegramId)
    }

    @GetMapping("/list")
    fun getCart(@PageableDefault(size = 20) pageable: Pageable,
                @RequestHeader("X-Telegram-User-Id") telegramId: Long): CartPageResponseDTO
        = cartService.getCart(telegramId, pageable)
}