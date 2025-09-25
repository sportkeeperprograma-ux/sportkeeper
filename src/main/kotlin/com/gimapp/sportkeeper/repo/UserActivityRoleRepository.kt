package com.gimapp.sportkeeper.repo

import com.gimapp.sportkeeper.domain.RoleType
import com.gimapp.sportkeeper.domain.UserActivityRole
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserActivityRoleRepository : JpaRepository<UserActivityRole, UUID> {
    fun existsByUserIdAndActivity_IdAndRoleType(userId: UUID?, activityId: UUID, roleType: RoleType): Boolean
    fun findByUserId(userId: UUID): List<UserActivityRole>
    fun findByActivity_IdAndRoleType(activityId: UUID, roleType: RoleType): List<UserActivityRole>
}
