package com.gimapp.sportkeeper.web

import com.gimapp.sportkeeper.repo.UserRepository
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/me")
class MeController(private val users: UserRepository) {
    @GetMapping
    fun me(auth: Authentication) = users.findByEmail(auth.name)!!.let {
        mapOf("id" to it.id, "email" to it.email, "role" to it.role, "status" to it.status)
    }
}
