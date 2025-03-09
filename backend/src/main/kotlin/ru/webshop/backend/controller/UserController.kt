package ru.webshop.backend.controller
import org.springframework.web.bind.annotation.*

import org.slf4j.LoggerFactory

@RestController
@RequestMapping("/api")
class UserController {

    private val logger = LoggerFactory.getLogger(UserController::class.java)

    @GetMapping("/meid")
    fun getInfo(@RequestHeader("X-Telegram-Init") initData: Any) {
        logger.info(initData.toString())
    }

    @GetMapping("/me")
    fun getInfo(@RequestAttribute("X-Telegram-User-Id") telegramId: Long): String {
        logger.info(telegramId.toString())
        return "User ID: $telegramId"
    }
}

