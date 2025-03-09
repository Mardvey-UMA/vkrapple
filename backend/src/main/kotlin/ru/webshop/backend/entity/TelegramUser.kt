package ru.webshop.backend.entity

data class TelegramUser(
    val id: Long,
    val first_name: String? = null,
    val last_name: String? = null,
    val username: String? = null,
    val language_code: String? = null,
    val is_premium: Boolean? = null
)