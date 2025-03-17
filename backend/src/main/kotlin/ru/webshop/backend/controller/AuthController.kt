package ru.webshop.backend.controller

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
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
class AuthController(
    private val authenticationService: AuthenticationService
) {
    @PostMapping("/authenticate")
    fun authenticateUser(
        @RequestHeader("X-Telegram-Init-Data") webAppData: String ,
        response: HttpServletResponse
    ): ResponseEntity<AuthResponseDTO> {
        val authResponse = authenticationService.authenticate(webAppData, response)
        return ResponseEntity.ok(authResponse)
    }
}