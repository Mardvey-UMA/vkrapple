package ru.webshop.backend.filters

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import ru.webshop.backend.security.TelegramUserPrincipal
import java.time.Instant
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.macs.HMac
import org.bouncycastle.crypto.params.KeyParameter
import org.slf4j.Logger
import ru.webshop.backend.security.TelegramAuthentication
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class TelegramAuthFilter(
    private val objectMapper: ObjectMapper,
    private val botToken: String,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val logger: Logger = LoggerFactory.getLogger(this::class.java)
        try {
            val authHeader = request.getHeader("Authorization")
            logger.info("Authorization header: $authHeader")
            if (authHeader?.startsWith("tma ") == true) {
                val initData = authHeader.substringAfter("tma ")
                logger.debug("Processing Telegram init data: $initData")

                val params = parseQueryParams(initData)
                if (params["hash"] == null) {
                    throw SecurityException("Missing hash parameter")
                }
                validateParams(params)

                val user = parseUser(params["user"]!!)

                SecurityContextHolder.getContext().authentication = TelegramAuthentication(
                    TelegramUserPrincipal(
                        id = user.id,
                        authDate = Instant.ofEpochSecond(params["auth_date"]!!.toLong()),
                        userJson = params["user"]!!
                    )
                )
            }
        } catch (e: Exception) {
            logger.error("Authentication failed: ${e.message}")
            response.sendError(401, "Unauthorized: ${e.message}")
            return
        }
        chain.doFilter(request, response)
    }

    private fun parseQueryParams(query: String): Map<String, String> {
        return query.split("&").associate {
            val (key, value) = it.split("=", limit = 2)
            key to value//URLDecoder.decode(value, "UTF-8")
        }
    }
    private fun validateParams(params: Map<String, String>) {
        require(params.containsKey("hash")) { "Missing hash in init data" }
        require(params.containsKey("auth_date")) { "Missing auth_date" }
        require(params.containsKey("user")) { "Missing user data" }

        val dataCheckString = params
            .filterKeys { it != "hash" }
            .toSortedMap()
            .entries.joinToString("\n") { "${it.key}=${it.value}" }

        val secretKey = generateSecretKey()
        val computedHash = calculateSignature(dataCheckString, secretKey)

        logger.debug("DataCheckString: $dataCheckString")
        logger.debug("Computed hash: $computedHash")
        logger.debug("Received hash: ${params["hash"]}")

        if (computedHash != params["hash"]) {
            logger.info(computedHash)
            logger.info(params["hash"])
            logger.info(params["signature"])
            throw SecurityException("Invalid signature")
        }

        val authDate = Instant.ofEpochSecond(params["auth_date"]!!.toLong())
        if (authDate.isBefore(Instant.now().minusSeconds(3600))) {
            throw SecurityException("Init data expired")
        }
    }

    private fun parseUser(userJson: String): UserData {
        logger.info(userJson)
        return try {
            objectMapper.readValue(userJson, UserData::class.java)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid user JSON format")
        }
    }
    private fun generateSecretKey(): ByteArray {
        val hmac = HMac(SHA256Digest())
        hmac.init(KeyParameter("WebAppData".toByteArray(StandardCharsets.UTF_8)))
        hmac.update(botToken.toByteArray(StandardCharsets.UTF_8), 0, botToken.length)
        val secret = ByteArray(hmac.macSize)
        hmac.doFinal(secret, 0)
        return secret
    }

    private fun calculateSignature(dataCheckString: String, secretKey: ByteArray): String {
        val hmac = HMac(SHA256Digest())
        hmac.init(KeyParameter(secretKey))
        hmac.update(dataCheckString.toByteArray(StandardCharsets.UTF_8), 0, dataCheckString.length)
        val hash = ByteArray(hmac.macSize)
        hmac.doFinal(hash, 0)
        return hash.toHex()
    }

    fun ByteArray.toHex(): String =
        joinToString("") { "%02x".format(it) }

    private data class UserData(val id: Long)
}