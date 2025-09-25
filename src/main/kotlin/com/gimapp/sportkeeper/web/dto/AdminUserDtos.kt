package com.gimapp.sportkeeper.web.dto

import com.gimapp.sportkeeper.domain.Role
import com.gimapp.sportkeeper.domain.UserStatus
import java.util.UUID

data class AdminUserDto(
    val id: UUID?,
    val email: String,
    val role: Role,
    val status: UserStatus
)

data class RoleUpdateRequest(val role: Role)
data class StatusUpdateRequest(val status: UserStatus)