package com.gimapp.sportkeeper.service

import com.gimapp.sportkeeper.domain.*
import com.gimapp.sportkeeper.repo.UserActivityRoleRepository
import org.springframework.stereotype.Component
import java.util.*

interface SchedulingPolicy {
    fun assertCoachCanTeach(coach: User, activityId: UUID)
}
interface EnrollmentPolicy {
    fun assertStudentCanEnroll(student: User, slot: TimeSlot)
}

@Component
class DefaultPolicies(
    private val uarRepo: UserActivityRoleRepository
) : SchedulingPolicy, EnrollmentPolicy {

    override fun assertCoachCanTeach(coach: User, activityId: UUID) {
        require(coach.status == UserStatus.ACTIVE) { "El coach está bloqueado" }
        val ok = uarRepo.existsByUserIdAndActivity_IdAndRoleType(coach.id, activityId, RoleType.TEACHES)
        require(ok) { "El coach no tiene TEACHES en la actividad" }
    }

    override fun assertStudentCanEnroll(student: User, slot: TimeSlot) {
        require(student.status == UserStatus.ACTIVE) { "El alumno está bloqueado" }
        require(student.id != slot.coachId) { "El coach no puede apuntarse a su propia sesión" }
        val ok = uarRepo.existsByUserIdAndActivity_IdAndRoleType(student.id, slot.activity.id, RoleType.LEARNS)
        require(ok) { "El alumno no tiene LEARNS en la actividad del slot" }
    }
}
