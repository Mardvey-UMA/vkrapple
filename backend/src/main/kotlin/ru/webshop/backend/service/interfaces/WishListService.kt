package ru.webshop.backend.service.interfaces

import org.springframework.data.domain.Pageable
import ru.webshop.backend.dto.WishListPageResponseDTO
import ru.webshop.backend.entity.Product
import ru.webshop.backend.entity.User

interface WishListService {
    fun addToWishList(telegramId: Long, articleNumber: Long)
    fun removeFromWishList(telegramId: Long, articleNumber: Long)
    fun getWishList(telegramId: Long, pageable: Pageable): WishListPageResponseDTO
}