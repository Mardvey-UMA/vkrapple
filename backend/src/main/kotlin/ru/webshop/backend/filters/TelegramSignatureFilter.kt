package ru.webshop.backend.filters

import com.fasterxml.jackson.core.type.TypeReference
import org.springframework.stereotype.Component
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException
import java.security.MessageDigest
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.webshop.backend.controller.UserController
import java.net.URLDecoder

@Component
class TelegramSignatureFilter(
    val objectMapper: ObjectMapper = ObjectMapper(),
    val telegramSecret: String = "7543101157:AAESvq4Mu3IHx-2gtkj4xJwktoohT9RbWqE",
    val logger: Logger = LoggerFactory.getLogger(UserController::class.java)
) : Filter {

    @Throws(ServletException::class, IOException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        logger.info("Received request at: ${httpRequest.requestURI}")

        val initData = httpRequest.getHeader("X-Telegram-Init") ?: run {
            logger.warn("Missing X-Telegram-Init header")
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing X-Telegram-Init header")
            return
        }

        logger.debug("X-Telegram-Init header content: $initData")

        val queryParams = try {
            parseQueryParams(initData)
        } catch (e: Exception) {
            logger.error("Error parsing query params: ${e.message}")
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid init data format")
            return
        }

        logger.debug("Parsed query parameters: {}", queryParams)

        if (!validateSignature(queryParams)) {
            logger.warn("Invalid signature for init data")
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid signature")
            return
        }

        logger.info("Signature validation passed")

        // Check data freshness (24 hours)
        val authDate = queryParams["auth_date"]?.toLongOrNull()?.let { it * 1000 } // Convert to milliseconds
        if (authDate == null || System.currentTimeMillis() - authDate > 24 * 3600 * 1000) {
            logger.warn("Expired auth data")
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Expired authorization")
            return
        }

        val userJson = queryParams["user"]?.let { URLDecoder.decode(it, "UTF-8") } ?: run {
            logger.warn("Missing user data")
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing user data")
            return
        }

        val userMap = try {
            objectMapper.readValue(userJson, object : TypeReference<Map<String, Any>>() {})
        } catch (e: Exception) {
            logger.error("Error parsing user data: ${e.message}")
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user data format")
            return
        }

        logger.info("Parsed user data: $userMap")

        val telegramId = (userMap["id"] as? Number)?.toLong() ?: run {
            logger.warn("Invalid user ID format")
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID")
            return
        }

        logger.info("Extracted telegramId: $telegramId")

        if (!isUserExists(telegramId)) {
            logger.warn("User $telegramId not authorized")
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not authorized")
            return
        }

        logger.info("User $telegramId authorized successfully")

        httpRequest.setAttribute("X-Telegram-User-Id", telegramId)
        chain.doFilter(request, response)
    }

    private fun parseQueryParams(query: String): Map<String, String> {
        return query.split("&").associate {
            val parts = it.split("=", limit = 2)
            val key = URLDecoder.decode(parts[0], "UTF-8")
            val value = if (parts.size > 1) URLDecoder.decode(parts[1], "UTF-8") else ""
            key to value
        }
    }

    private fun validateSignature(params: Map<String, String>): Boolean {
        val receivedHash = params["hash"] ?: return false

        val dataCheckString = params.filter { it.key != "hash" }
            .toSortedMap()
            .map { (k, v) -> "$k=$v" }
            .joinToString("\n")

        val computedHash = generateSignature(dataCheckString)
        return receivedHash == computedHash
    }

    private fun generateSignature(dataCheckString: String): String {
        val secretKeyString = "WebAppData$telegramSecret"
        val secretKeySha256 = MessageDigest.getInstance("SHA-256")
            .digest(secretKeyString.toByteArray(Charsets.UTF_8))

        val mac = Mac.getInstance("HmacSHA256").apply {
            init(SecretKeySpec(secretKeySha256, "HmacSHA256"))
        }

        return mac.doFinal(dataCheckString.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
    }


    private fun isUserExists(telegramId: Long): Boolean {
        // Mock logic, replace with actual DB check
        logger.debug("Checking if user with telegramId $telegramId exists in the database")
        return true
    }

    override fun init(filterConfig: FilterConfig?) {}
    override fun destroy() {}
}
