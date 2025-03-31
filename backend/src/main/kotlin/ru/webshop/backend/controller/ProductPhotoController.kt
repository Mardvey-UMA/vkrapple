package ru.webshop.backend.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.webshop.backend.dto.PhotoUploadRequestDTO
import ru.webshop.backend.service.interfaces.PhotoUploadService

@RestController
@RequestMapping("/api/products/photo")
@Tag(name = "Photo", description = "Управление фотографиями")
class ProductPhotoController(
    private val photoUploadService: PhotoUploadService
) {
    @Operation(summary = "Загрузить фото товара",
        description = "Загрузка фото товара по его артикулу + порядок")
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadPhoto(
        @ModelAttribute request: PhotoUploadRequestDTO,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<String> {
        return ResponseEntity.ok(
            photoUploadService.uploadPhotoForProduct(
                articleNumber = request.articleNumber,
                file = file,
                indexNumber = request.indexNumber
            )
        )
    }
}