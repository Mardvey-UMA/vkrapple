package ru.webshop.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.webshop.backend.entity.Photo

interface PhotoRepository: JpaRepository<Photo, Long> {
    fun createPhoto(objectKey: String, photoUrl: String): Photo
    fun deletePhoto(objectKey: String, photoUrl: String)
}