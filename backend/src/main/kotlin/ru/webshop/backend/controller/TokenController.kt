package ru.webshop.backend.controller

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.webshop.backend.dto.AuthResponseDTO
import ru.webshop.backend.service.interfaces.TokenService

@RequestMapping("/api/auth/token")
@RestController
class TokenController(
    private val tokenService: TokenService
) {
    @PostMapping("/refresh")
    fun refreshToken(
        @CookieValue("REFRESH_TOKEN") refreshToken: String,
        response: HttpServletResponse
    ): ResponseEntity<AuthResponseDTO> {
        val newTokens = tokenService.refreshToken(refreshToken, response)
        return ResponseEntity.ok(newTokens)
    }
}