package com.gimapp.sportkeeper.repo

import com.gimapp.sportkeeper.domain.Activity
import com.gimapp.sportkeeper.domain.UserActivityRole
import com.gimapp.sportkeeper.domain.RoleType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ActivityRepository : JpaRepository<Activity, UUID> {
    fun findByCode(code: String): Optional<Activity>
    fun existsByCode(code: String): Boolean
}

