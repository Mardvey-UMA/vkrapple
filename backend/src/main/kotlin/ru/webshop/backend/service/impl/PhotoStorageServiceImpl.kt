package ru.webshop.backend.service.impl

import org.springframework.web.multipart.MultipartFile
import ru.webshop.backend.repository.PhotoRepository
import ru.webshop.backend.service.interfaces.PhotoStorageService

class PhotoStorageServiceImpl(
    private val photoRepository: PhotoRepository,
) : PhotoStorageService {
    override suspend fun uploadPhoto(categoryName: String, objectKey: String, file: MultipartFile): String {
        TODO("Not yet implemented")
    }

    override suspend fun downloadPhoto(categoryName: String, objectKey: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePhoto(categoryName: String, objectKey: String) {
        TODO("Not yet implemented")
    }
}