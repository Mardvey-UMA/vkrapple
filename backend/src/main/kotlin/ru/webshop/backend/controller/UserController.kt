package ru.webshop.backend.controller
import org.springframework.web.bind.annotation.*

import org.slf4j.LoggerFactory

@RestController
@RequestMapping("/api")
class UserController {

    private val logger = LoggerFactory.getLogger(UserController::class.java)
    
    @GetMapping("/me")
    fun getInfo(@RequestHeader("X-Telegram-User-Id") telegramId: Long): String {
        logger.info("ЗАПРОС")
        logger.info(telegramId.toString())
        return "User ID: $telegramId"
    }
}

