package ru.webshop.backend.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.security.Principal
import java.time.Instant

data class TelegramUserPrincipal(
    val id: Long,
    val authDate: Instant,
    val userJson: String
) : UserDetails, Principal {
    override fun getAuthorities() = emptyList<GrantedAuthority>()
    override fun getPassword() = ""
    override fun getUsername() = id.toString()
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
    override fun getName() = id.toString()
}