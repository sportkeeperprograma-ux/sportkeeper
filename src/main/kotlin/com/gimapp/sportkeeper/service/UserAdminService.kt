package com.gimapp.sportkeeper.service

import com.gimapp.sportkeeper.domain.Role
import com.gimapp.sportkeeper.domain.UserStatus
import com.gimapp.sportkeeper.repo.UserRepository
import com.gimapp.sportkeeper.web.dto.AdminUserDto
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class UserAdminService(
    private val users: UserRepository
) {

    fun list(): List<AdminUserDto> = users.findAll().map { it.toDto() }

    @Transactional
    fun changeRole(id: UUID, newRole: Role, actorId: UUID?): AdminUserDto {
        val u = users.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND)
        }

        // no dejar el sistema sin ADMIN
        if (u.role == Role.ADMIN && newRole != Role.ADMIN && users.countByRole(Role.ADMIN) <= 1) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Debe existir al menos un ADMIN")
        }
        // evitar que alguien se quite a sí mismo el rol de ADMIN
        if (actorId != null && actorId == u.id && newRole != Role.ADMIN) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes quitarte tu propio rol")
        }

        u.role = newRole
        return users.save(u).toDto()
    }

    @Transactional
    fun updateStatus(id: UUID, newStatus: UserStatus): AdminUserDto {
        val u = users.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND)
        }
        u.status = newStatus
        return users.save(u).toDto()
    }

    @Transactional
    fun delete(id: UUID, actorId: UUID?) {
        val u = users.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND)
        }
        if (actorId != null && actorId == u.id) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes eliminar tu propio usuario")
        }
        if (u.role == Role.ADMIN && users.countByRole(Role.ADMIN) <= 1) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Debe existir al menos un ADMIN")
        }
        users.delete(u)
    }
}
