package com.gimapp.sportkeeper.web

import com.gimapp.sportkeeper.domain.User
import com.gimapp.sportkeeper.infra.JwtUtil
import com.gimapp.sportkeeper.repo.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val users: UserRepository,
    private val pe: PasswordEncoder,
    private val jwt: JwtUtil
) {
    data class RegisterReq(val email: String, val password: String)
    data class LoginReq(val email: String, val password: String)

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody r: RegisterReq) {
        require(users.findByEmail(r.email) == null) { "Email already exists" }
        users.save(User(email = r.email, password = pe.encode(r.password), role = "MEMBER"))
    }

    @PostMapping("/login")
    fun login(@RequestBody r: LoginReq) =
        users.findByEmail(r.email)?.takeIf { pe.matches(r.password, it.password) }
            ?.let { mapOf("token" to jwt.generate(it.email)) }
            ?: throw IllegalArgumentException("Invalid credentials")
}
