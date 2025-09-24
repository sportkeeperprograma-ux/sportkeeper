package com.gimapp.sportkeeper.infra

import com.gimapp.sportkeeper.repo.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val users: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val u = users.findByEmail(username) ?: throw UsernameNotFoundException("User not found")
        val auth = listOf(SimpleGrantedAuthority("ROLE_${u.role}"))
        return org.springframework.security.core.userdetails.User(u.email, u.password, auth)
    }
}
