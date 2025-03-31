package ru.webshop.backend.controller

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import ru.webshop.backend.dto.WishListItemDTO
import ru.webshop.backend.dto.WishListPageResponseDTO
import ru.webshop.backend.service.interfaces.WishListService

@RestController
@RequestMapping("/api/wishlist")
class WishListController(
    private val wishListService: WishListService,
) {

    @PostMapping("/add")
    fun addToWishList(@RequestParam("article") articleNumber: Long,
                      @RequestHeader("X-Telegram-User-Id") telegramId: Long) : WishListItemDTO
    = wishListService.addToWishList(telegramId, articleNumber)

    @DeleteMapping("/remove")
    fun removeFromWishList(@RequestParam("article") articleNumber: Long,
                           @RequestHeader("X-Telegram-User-Id") telegramId: Long) : WishListItemDTO
    = wishListService.removeFromWishList(telegramId, articleNumber)

    @GetMapping("/list")
    fun getWishList(@PageableDefault(size = 20) pageable: Pageable,
                    @RequestHeader("X-Telegram-User-Id") telegramId: Long) : WishListPageResponseDTO
    = wishListService.getWishList(telegramId, pageable)
}