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
import ru.webshop.backend.security.TelegramAuthentication
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Component
class TelegramAuthFilter(
    private val objectMapper: ObjectMapper = ObjectMapper().apply {
        propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
    },
    @Value("\${telegram.bot.token}")
    private val botToken: String,

) : OncePerRequestFilter() {

    private val secretKey by lazy { generateSecretKey() }

    private fun generateSecretKey(): ByteArray {
        val hmac = Mac.getInstance("HmacSHA256")
        hmac.init(SecretKeySpec("WebAppData".toByteArray(StandardCharsets.UTF_8), "HmacSHA256"))
        return hmac.doFinal(botToken.toByteArray(StandardCharsets.UTF_8))
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        try {
            val authHeader = request.getHeader("Authorization")
            if (authHeader?.startsWith("tma ") == true) {
                val initData = authHeader.substringAfter("tma ")
                val params = parseQueryString(initData)
                val userBody = params["user"]
                val hash = params["hash"]

                if (!userBody.isNullOrBlank() && !hash.isNullOrBlank() && validateTelegramAuth(params, hash)) {
                    val user = objectMapper.readValue(userBody, UserData::class.java)
                    SecurityContextHolder.getContext().authentication = TelegramAuthentication(
                        TelegramUserPrincipal(
                            id = user.id,
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

    private fun validateTelegramAuth(paramMap: Map<String, String>, receivedHash: String): Boolean {
        val dataCheckString = paramMap.entries
            .filter { it.key != "hash" }
            .sortedBy { it.key }
            .joinToString("\n") { "${it.key}=${it.value}" }

        val hmac = Mac.getInstance("HmacSHA256").apply {
            init(SecretKeySpec(secretKey, "HmacSHA256"))
        }
        val calculatedHash = bytesToHex(hmac.doFinal(dataCheckString.toByteArray(StandardCharsets.UTF_8)))
        return calculatedHash.equals(receivedHash, ignoreCase = true)
    }

    private fun parseQueryString(queryString: String): Map<String, String> {
        return queryString.split("&").associate {
            val (key, value) = it.split("=", limit = 2)
            URLDecoder.decode(key, StandardCharsets.UTF_8) to
                    URLDecoder.decode(value, StandardCharsets.UTF_8)
        }
    }

    private fun bytesToHex(bytes: ByteArray): String {
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private data class UserData(val id: Long)
}