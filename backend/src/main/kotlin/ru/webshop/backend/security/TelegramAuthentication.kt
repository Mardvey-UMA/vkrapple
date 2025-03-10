package ru.webshop.backend.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class TelegramAuthentication(
    private val principal: TelegramUserPrincipal
) : AbstractAuthenticationToken(emptyList<GrantedAuthority>()) {
    override fun getCredentials() = ""
    override fun getPrincipal() = principal
    override fun isAuthenticated() = true
}