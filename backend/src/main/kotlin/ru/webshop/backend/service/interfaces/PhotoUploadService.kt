package ru.webshop.backend.service.interfaces

import org.springframework.web.multipart.MultipartFile

interface PhotoUploadService {
    fun uploadPhoto(articleNumber: Long, file: MultipartFile, indexNumber: Int): String
}