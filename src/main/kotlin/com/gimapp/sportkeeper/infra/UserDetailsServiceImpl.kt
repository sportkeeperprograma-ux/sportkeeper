package com.gimapp.sportkeeper.infra

import com.gimapp.sportkeeper.infra.security.MyPrincipal
import com.gimapp.sportkeeper.repo.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val users: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val u = users.findByEmail(username) ?: throw UsernameNotFoundException("User not found")
        return MyPrincipal(
            userId = u.id,
            email = u.email,
            role = u.role,
            passwordHash = u.password // si usas login con contraseña
        )
    }
}
