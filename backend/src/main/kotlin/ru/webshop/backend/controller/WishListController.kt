package ru.webshop.backend.controller

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import ru.webshop.backend.service.interfaces.WishListService

@RestController
@RequestMapping("/wishlist")
class WishListController(
    private val wishListService: WishListService,
) {

    @PostMapping("/add")
    fun addToWishList(@RequestParam("article") articleNumber: Long,
                      @RequestHeader("X-Telegram-User-Id") telegramId: Long){
        wishListService.addToWishList(telegramId, articleNumber)
    }

    @DeleteMapping("/remove")
    fun removeFromWishList(@RequestParam("article") articleNumber: Long,
                           @RequestHeader("X-Telegram-User-Id") telegramId: Long){
        wishListService.removeFromWishList(telegramId, articleNumber)
    }

    @GetMapping("/list")
    fun getWishList(@PageableDefault(size = 20) pageable: Pageable,
                    @RequestHeader("X-Telegram-User-Id") telegramId: Long)
    = wishListService.getWishList(telegramId, pageable)
}