package ru.webshop.backend.filters

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import ru.webshop.backend.security.TelegramUserPrincipal
import java.time.Instant
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.webshop.backend.entity.User
import ru.webshop.backend.security.TelegramAuthentication
import ru.webshop.backend.service.UserService
import ru.webshop.backend.utils.TelegramCodeUtils
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Component
class TelegramAuthFilter(
    private val objectMapper: ObjectMapper = ObjectMapper().apply {
        propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
    },
    private val userService: UserService,

    private val telegramCodeUtils: TelegramCodeUtils,

    ) : OncePerRequestFilter() {

    private val secretKey by lazy { telegramCodeUtils.generateSecretKey() }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        try {
            val authHeader = request.getHeader("Authorization")
            if (authHeader?.startsWith("tma ") == true) {
                val initData = authHeader.substringAfter("tma ")
                val params = telegramCodeUtils.parseQueryString(initData)
                val userBody = params["user"]
                val hash = params["hash"]

                if (!userBody.isNullOrBlank() && !hash.isNullOrBlank() && telegramCodeUtils.validateTelegramAuth(secretKey, params, hash)) {
                    val userCurrent = objectMapper.readValue(userBody, UserData::class.java)
                    val user: User? = userService.findByTelegramId(userCurrent.id)
                    if (user == null) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found")
                        return
                    }
                    ///
                    //TODO Добавить проверку на наличие в БД + вытащить роли ✅
                    // роли будут добавлять при реге через тг бота, и добавить список админов и селлеров
                    ///

                    SecurityContextHolder.getContext().authentication = TelegramAuthentication(
                        TelegramUserPrincipal(
                            id = userCurrent.id,
                            authDate = Instant.ofEpochSecond(params["auth_date"]!!.toLong()),
                            userJson = userBody
                        )
                    )
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Telegram authentication")
                    return
                }
            }
        } catch (e: Exception) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: ${e.message}")
            return
        }
        chain.doFilter(request, response)
    }

    private data class UserData(val id: Long)
}