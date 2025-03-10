package ru.webshop.backend.filters

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.webshop.backend.security.TelegramUserPrincipal
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*
import org.slf4j.Logger

@Component
class TelegramHeaderFilter : OncePerRequestFilter() {


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val logger: Logger = LoggerFactory.getLogger(this::class.java)
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication?.principal is TelegramUserPrincipal) {
            val user = authentication.principal as TelegramUserPrincipal
            val wrappedRequest = object : HttpServletRequestWrapper(request) {
                override fun getHeader(name: String) = when (name.lowercase()) {
                    "x-telegram-user-id" -> user.id.toString()
                    else -> super.getHeader(name)
                }

                override fun getHeaders(name: String): Enumeration<String> {
                    return if (name.equals("x-telegram-user-id", ignoreCase = true)) {
                        Collections.enumeration(listOf(user.id.toString()))
                    } else {
                        super.getHeaders(name)
                    }
                }

                override fun getHeaderNames(): Enumeration<String> {
                    val original = super.getHeaderNames().toList()
                    return Collections.enumeration(original + "X-Telegram-User-Id")
                }
            }
            logger.debug("Added X-Telegram-User-ID header: {}", user.id)
            chain.doFilter(wrappedRequest, response)
        } else {
            chain.doFilter(request, response)
        }
    }
}