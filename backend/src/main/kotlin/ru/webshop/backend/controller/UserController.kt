package ru.webshop.backend.controller
import org.springframework.web.bind.annotation.*

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import ru.webshop.backend.dto.UserResponse

@RestController
@RequestMapping("/api")
class UserController {

    private val logger = LoggerFactory.getLogger(UserController::class.java)
    
    @GetMapping("/me")
    fun getInfo(@RequestHeader("X-Telegram-User-Id") telegramId: Long): ResponseEntity<UserResponse> {
        logger.info("ЗАПРОС")
        logger.info(telegramId.toString())
        return ResponseEntity.ok(UserResponse(telegramId))
    }
}

