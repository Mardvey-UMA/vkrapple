package ru.webshop.backend.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.Cookie
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import ru.webshop.backend.config.JwtConfig
import ru.webshop.backend.entity.User
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService(
    private val jwtConfig: JwtConfig
) {

    fun getSignInKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(jwtConfig.secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun extractTelegramId(token: String): String? = extractClaim(token) { claims: Claims -> claims.subject }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver.invoke(claims)
    }

    fun extractAllClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .payload

    private fun buildToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails,
        jwtExpiration: Long
    ): String {
        val now = Instant.now()
        return Jwts.builder()
            .claims(extraClaims)
            .subject(userDetails.getUsername()) // Тонкий момент здесь это telegramId
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(jwtExpiration)))
            .signWith(getSignInKey())
            .compact()
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val telegramId: String? = extractTelegramId(token)
        return telegramId != null &&
                telegramId == userDetails.getUsername() &&
                !isTokenExpired(token)
    }

    fun createHttpOnlyCookie(name: String, value: String): Cookie {
        val cookie = Cookie(name, value)
        cookie.isHttpOnly = true
        cookie.maxAge = jwtConfig.expiration.toInt()
        cookie.path = "/"
        return cookie
    }

    private fun isTokenExpired(token: String): Boolean = extractExpiration(token).before(Date.from(Instant.now()))

    fun extractExpiration(token: String): Date = extractClaim(token, Claims::getExpiration)

    fun generateAccessToken(user: UserDetails): String {
        val userEntity = user as User
        val claims = ("telegram_id" to userEntity.telegramId).let {
            mapOf<String, Any>(
                "telegram_id" to (user as User).telegramId,
                "roles" to user.authorities.map { it.authority },
                "token_type" to "ACCESS",
                "sub" to userEntity.telegramId.toString()
            )
        }
        return buildToken(claims, user, jwtConfig.expiration)
    }

    fun generateRefreshToken(user: UserDetails): String {
        val claims = mapOf("type" to "refresh")
        return buildToken(claims, user, jwtConfig.refreshExpiration)
    }
    fun isRefreshTokenValid(token: String): Boolean {
        val claims = extractAllClaims(token)
        return claims["type"] == "refresh" && !isTokenExpired(token)
    }

    /////////////////////////////////////////////
    fun extractExpirationLocalDateTime(token: String): LocalDateTime? {
        return try {
            val expirationDate = extractExpiration(token)
            expirationDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        } catch (e: Exception) {
            null
        }
    }
}