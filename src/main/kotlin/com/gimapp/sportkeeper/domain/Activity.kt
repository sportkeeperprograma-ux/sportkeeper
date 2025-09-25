package com.gimapp.sportkeeper.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(
    name = "activity",
    uniqueConstraints = [UniqueConstraint(columnNames = ["code"])]
)
class Activity(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false, length = 64)
    val code: String,            // p.ej. "BJJ", "BOXING" (único, apto para URL)

    @Column(nullable = false, length = 128)
    val name: String,            // p.ej. "Brazilian Jiu-Jitsu"

    @Column(nullable = false)
    val active: Boolean = true
) {
    init {
        require(code.isNotBlank()) { "code no puede estar vacío" }
        require(name.isNotBlank()) { "name no puede estar vacío" }
    }
}
