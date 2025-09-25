package com.gimapp.sportkeeper.service

import com.gimapp.sportkeeper.domain.User
import com.gimapp.sportkeeper.web.dto.AdminUserDto

fun User.toDto() = AdminUserDto(
    id = id,
    email = email,
    role = role,
    status = status
)
