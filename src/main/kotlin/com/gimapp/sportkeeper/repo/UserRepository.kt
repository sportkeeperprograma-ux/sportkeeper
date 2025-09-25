package com.gimapp.sportkeeper.repo


import com.gimapp.sportkeeper.domain.Role
import com.gimapp.sportkeeper.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface UserRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): User?
    fun countByRole(role: Role): Long
}