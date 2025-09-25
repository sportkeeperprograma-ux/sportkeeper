package com.gimapp.sportkeeper.infra.security

import com.gimapp.sportkeeper.domain.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

data class MyPrincipal(
    val userId: UUID?,
    private val email: String,
    val role: Role,
    private val passwordHash: String? = null
) : UserDetails {

    override fun getUsername(): String = email
    override fun getPassword(): String? = passwordHash

    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority("ROLE_$role"))

    // si tus usuarios no expiran/bloquean por aquí, deja estos en true
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}
