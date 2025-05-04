package ru.webshop.backend.controller.admin

data class AttributeCreateRequestDTO(
    val name: String,
    val values: List<String>
)