package ru.webshop.backend.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.webshop.backend.service.LogoutService

@RestController
@RequestMapping("/logout")
class LogoutController(
    private val logoutService: LogoutService
) {
    @PostMapping()
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication?
    ): ResponseEntity<String> {
        logoutService.logout(request, response, authentication)
        return ResponseEntity.ok("Logged out successfully")
    }
}