package com.gimapp.sportkeeper.web

import com.gimapp.sportkeeper.repo.UserRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/admin/users")
class UserAdminController(private val users: UserRepository) {
    @GetMapping
    fun list() = users.findAll().map { mapOf("id" to it.id, "email" to it.email, "role" to it.role, "status" to it.status) }
    @PutMapping("/{id}/block") fun block(@PathVariable id: UUID) = users.findById(id).orElseThrow().let { it.status="BLOCKED"; users.save(it); mapOf("status" to "BLOCKED") }
    @PutMapping("/{id}/unblock") fun unblock(@PathVariable id: UUID) = users.findById(id).orElseThrow().let { it.status="ACTIVE"; users.save(it); mapOf("status" to "ACTIVE") }
}
