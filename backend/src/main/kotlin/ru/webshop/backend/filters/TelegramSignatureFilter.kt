package ru.webshop.backend.filters

import com.fasterxml.jackson.core.type.TypeReference
import org.springframework.stereotype.Component
import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.net.URLDecoder
import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequestWrapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import java.io.IOException
import java.time.Instant
import java.util.*

@Component
class TelegramSignatureFilter(
    private val objectMapper: ObjectMapper = ObjectMapper()
) : Filter {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${bot.token}")
    private lateinit var botToken: String

    @Value("\${dev.mode}")
    private var devMode: Boolean = true

    @Throws(ServletException::class, IOException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        logger.info("Starting Telegram signature validation for request: ${httpRequest.requestURI}")

        val initData = httpRequest.getHeader("X-Telegram-Init") ?: run {
            logger.error("Missing X-Telegram-Init header")
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing X-Telegram-Init header")
            return
        }

        logger.debug("Received X-Telegram-Init header: $initData")

        val queryParams = parseQueryParams(initData)
        logger.debug("Parsed query parameters: {}", queryParams)

        if (devMode) {
            logger.info("DEV_MODE is enabled, skipping signature validation")
        } else {
            if (!validateSignature(initData, queryParams)) {
                logger.error("Signature validation failed")
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid signature")
                return
            }

            logger.info("Signature validation passed successfully")
        }

        val authDate = queryParams["auth_date"]?.toLongOrNull() ?: run {
            logger.error("Missing or invalid auth_date parameter")
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing auth_date")
            return
        }

        logger.debug("Auth date: {} ({})", authDate, Instant.ofEpochSecond(authDate))

        if (System.currentTimeMillis() / 1000 - authDate > 86400) {
            logger.error("Expired authorization data (older than 24 hours)")
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Expired authorization")
            return
        }

        logger.info("Authorization data is fresh")

        val userJson = queryParams["user"]?.let { URLDecoder.decode(it, "UTF-8") } ?: run {
            logger.error("Missing user data")
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing user data")
            return
        }

        logger.debug("User JSON data: $userJson")

        val telegramId = parseTelegramId(userJson) ?: run {
            logger.error("Invalid user format or missing ID")
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user format")
            return
        }

        logger.info("Successfully extracted Telegram ID: $telegramId")

        val wrappedRequest = object : HttpServletRequestWrapper(httpRequest) {
            private val customHeaders = mapOf(
                "x-telegram-user-id" to telegramId.toString()
            )

            override fun getHeader(name: String): String? {
                logger.trace("Requesting header: $name")
                return customHeaders[name.lowercase()]
                    ?: super.getHeader(name)
            }

            override fun getHeaders(name: String): Enumeration<String> {
                logger.trace("Requesting headers for: $name")
                val header = customHeaders[name.lowercase()]
                return if (header != null) {
                    Collections.enumeration(listOf(header))
                } else {
                    super.getHeaders(name)
                }
            }

            override fun getHeaderNames(): Enumeration<String> {
                logger.trace("Requesting header names")
                val originalHeaders = super.getHeaderNames().toList()
                return Collections.enumeration(originalHeaders + customHeaders.keys)
            }
        }

        logger.info("Adding X-Telegram-User-Id header with value: $telegramId")
        chain.doFilter(wrappedRequest, response)
    }

    private fun parseQueryParams(query: String): Map<String, String> {
        logger.debug("Parsing query parameters from: $query")
        return query.split("&").associate {
            val parts = it.split("=", limit = 2)
            parts[0] to (parts.getOrNull(1) ?: "").also { value ->
                logger.trace("Parsed parameter: ${parts[0]} = $value")
            }
        }
    }

    private fun validateSignature(initData: String, params: Map<String, String>): Boolean {
        logger.debug("Starting signature validation")

        val receivedHash = params["hash"] ?: run {
            logger.error("Missing hash parameter")
            return false
        }

        logger.debug("Received hash: $receivedHash")

        val dataCheckString = initData.split("&")
            .filter { !it.startsWith("hash=") }
            .sorted()
            .joinToString("\n") { it.substringBefore("=") + "=" + it.substringAfter("=") }

        logger.debug("Data check string: $dataCheckString")

        val secretKeyString = "WebAppData$botToken"
        logger.debug("Secret key string: $secretKeyString")

        val secretKeySha256 = MessageDigest.getInstance("SHA-256")
            .digest(secretKeyString.toByteArray())

        logger.debug("SHA-256 of secret key: ${secretKeySha256.joinToString("") { "%02x".format(it) }}")

        val secretKey = Mac.getInstance("HmacSHA256").apply {
            init(SecretKeySpec(secretKeySha256, "HmacSHA256"))
        }

        val calculatedHash = secretKey.doFinal(dataCheckString.toByteArray())
            .joinToString("") { "%02x".format(it) }

        logger.debug("Calculated hash: $calculatedHash")

        val isValid = calculatedHash == receivedHash
        logger.info("Signature validation result: $isValid")

        return isValid
    }

    private fun parseTelegramId(userJson: String): Long? {
        logger.debug("Parsing Telegram ID from user JSON")
        return try {
            val user = objectMapper.readValue(userJson, object : TypeReference<Map<String, Any>>() {})
            val id = (user["id"] as? Number)?.toLong()
            logger.debug("Parsed user data: {}", user)
            logger.debug("Extracted Telegram ID: $id")
            id
        } catch (e: Exception) {
            logger.error("Error parsing user JSON: ${e.message}")
            null
        }
    }

    override fun init(filterConfig: FilterConfig?) {
        logger.info("TelegramSignatureFilter initialized")
    }

    override fun destroy() {
        logger.info("TelegramSignatureFilter destroyed")
    }
}
