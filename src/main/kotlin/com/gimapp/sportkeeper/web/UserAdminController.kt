package com.gimapp.sportkeeper.web

import com.gimapp.sportkeeper.domain.Role
import com.gimapp.sportkeeper.domain.UserStatus
import com.gimapp.sportkeeper.infra.security.MyPrincipal
import com.gimapp.sportkeeper.repo.UserRepository
import com.gimapp.sportkeeper.service.UserAdminService
import com.gimapp.sportkeeper.web.dto.AdminUserDto
import com.gimapp.sportkeeper.web.dto.RoleUpdateRequest
import com.gimapp.sportkeeper.web.dto.StatusUpdateRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
class UserAdminController(
    private val service: UserAdminService
) {
    @GetMapping
    fun list() = service.list()

    @PatchMapping("/{id}/role")
    fun changeRole(
        @PathVariable id: UUID,
        @Valid @RequestBody body: RoleUpdateRequest,
        authentication: Authentication
    ): ResponseEntity<AdminUserDto> {
        val actorId = (authentication.principal as? MyPrincipal)?.userId // adapta a tu principal
        return ResponseEntity.ok(service.changeRole(id, body.role, actorId))
    }

    @PatchMapping("/{id}/status")
    fun updateStatus(
        @PathVariable id: UUID,
        @Valid @RequestBody body: StatusUpdateRequest
    ): ResponseEntity<AdminUserDto> =
        ResponseEntity.ok(service.updateStatus(id, body.status))

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: UUID,
        authentication: Authentication
    ): ResponseEntity<Void> {
        val actorId = (authentication.principal as? MyPrincipal)?.userId
        service.delete(id, actorId)
        return ResponseEntity.noContent().build()
    }
}
