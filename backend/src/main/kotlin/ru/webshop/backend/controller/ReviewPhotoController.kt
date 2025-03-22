package ru.webshop.backend.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.webshop.backend.dto.PhotoReviewRequestDTO
import ru.webshop.backend.dto.PhotoUploadRequestDTO
import ru.webshop.backend.service.interfaces.PhotoUploadService

@RestController
@RequestMapping("/api/review/photo")
class ReviewPhotoController(
    private val photoUploadService: PhotoUploadService
) {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadPhoto(
        @RequestHeader("X-Telegram-User-Id") userId: Long,
        @ModelAttribute request: PhotoReviewRequestDTO,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<String> {
        return ResponseEntity.ok(
            photoUploadService.uploadPhotoForReview(
                reviewId = request.reviewId,
                userId = userId,
                file = file,
                indexNumber = request.indexNumber
            )
        )
    }
}