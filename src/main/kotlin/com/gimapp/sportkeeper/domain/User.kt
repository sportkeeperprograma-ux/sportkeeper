package com.gimapp.sportkeeper.domain


import jakarta.persistence.*
import java.util.*


@Entity @Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    @Column(unique = true, nullable = false)
    var email: String = "",
    var password: String = "",       // BCrypt
    var role: String = "MEMBER",     // MEMBER | ADMIN
    var status: String = "ACTIVE",
)