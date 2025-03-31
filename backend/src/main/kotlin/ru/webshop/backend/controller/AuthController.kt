package ru.webshop.backend.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.context.annotation.Description
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.webshop.backend.dto.AuthResponseDTO
import ru.webshop.backend.service.interfaces.AuthenticationService

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Security", description = "Управление авторизацией валидацией пользователя")
class AuthController(
    private val authenticationService: AuthenticationService
) {
    @Operation(summary = "Авторизировать пользователя получить токены",
        description = "Заголовок с X-Telegram-Init-Data для проверки подписи и выдачи токенов")
    @PostMapping("/authenticate")
    fun authenticateUser(
        @RequestHeader("X-Telegram-Init-Data") webAppData: String ,
        response: HttpServletResponse
    ): ResponseEntity<AuthResponseDTO> {
        val authResponse = authenticationService.authenticate(webAppData, response)
        return ResponseEntity.ok(authResponse)
    }
}