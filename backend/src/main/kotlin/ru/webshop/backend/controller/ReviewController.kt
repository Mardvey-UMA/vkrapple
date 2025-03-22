package ru.webshop.backend.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.webshop.backend.dto.ReviewDTO
import ru.webshop.backend.service.interfaces.PhotoUploadService
import ru.webshop.backend.service.interfaces.ReviewService

@RestController
@RequestMapping("/api/review")
class ReviewController(
    private val reviewService: ReviewService,
) {
    @PostMapping
    fun uploadReview(
        @RequestHeader("X-Telegram-User-Id") userId: Long,
        @RequestBody request: ReviewDTO,
        @RequestParam("article") articleNumber : Long
    ) : ResponseEntity<String> {
        reviewService.createReview(
            reviewDTO = request,
            userId = userId,
            productArticle = articleNumber
        )
        return ResponseEntity.ok().build()
    }


}