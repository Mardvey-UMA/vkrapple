package ru.webshop.backend.service.interfaces

import org.springframework.web.multipart.MultipartFile

interface PhotoStorageService {
    suspend fun uploadPhoto(categoryName: String, objectKey: String, file: MultipartFile): String
    suspend fun downloadPhoto(categoryName: String, objectKey: String)
    suspend fun deletePhoto(categoryName: String, objectKey: String)
}