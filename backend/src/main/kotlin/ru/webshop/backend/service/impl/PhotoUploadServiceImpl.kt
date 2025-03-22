package ru.webshop.backend.service.impl

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import ru.webshop.backend.entity.Photo
import ru.webshop.backend.entity.PhotoProduct
import ru.webshop.backend.entity.PhotoReview
import ru.webshop.backend.exception.GlobalExceptionHandler
import ru.webshop.backend.repository.PhotoProductRepository
import ru.webshop.backend.repository.PhotoRepository
import ru.webshop.backend.repository.PhotoReviewRepository
import ru.webshop.backend.repository.ProductRepository
import ru.webshop.backend.service.interfaces.PhotoStorageService
import ru.webshop.backend.service.interfaces.PhotoUploadService
import java.util.*

@Service
class PhotoUploadServiceImpl(
    private val productRepository: ProductRepository,
    private val photoStorageService: PhotoStorageService,
    private val photoRepository: PhotoRepository,
    private val photoProductRepository: PhotoProductRepository,
) : PhotoUploadService {

    // Загрузить фотку продукта по артиклю продукта
    override fun uploadPhotoForProduct(articleNumber: Long, file: MultipartFile, indexNumber: Int): String {
        
        val product = productRepository.findByArticleNumber(articleNumber)
            ?: throw GlobalExceptionHandler.ProductNotFoundException(articleNumber.toString())

        val objectKey = "${articleNumber}/product"

        // TODO (Добавить логику для порядка фото)

        val photoUrl = photoStorageService.uploadPhoto(
            objectKey = objectKey,
            content = file.bytes,
            contentType = file.contentType ?: "image/jpeg"
        )

        val photo = photoRepository.save(
            Photo(
                objectKey = objectKey,
                photoUrl = photoUrl
            )
        )

        photoProductRepository.save(
            PhotoProduct(
                indexNumber = indexNumber,
                product = product,
                photo = photo
            )
        )

        return photoUrl
    }
}