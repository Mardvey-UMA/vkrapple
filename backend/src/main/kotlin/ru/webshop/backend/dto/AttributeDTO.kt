package ru.webshop.backend.dto

data class AttributeDTO(
    val id: Long,
    val name: String,
    val values: List<String>
)