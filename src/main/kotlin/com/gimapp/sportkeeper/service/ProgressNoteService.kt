package com.gimapp.sportkeeper.service

import com.gimapp.sportkeeper.domain.*
import com.gimapp.sportkeeper.repo.ActivityRepository
import com.gimapp.sportkeeper.repo.ProgressNoteRepository
import com.gimapp.sportkeeper.repo.UserActivityRoleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class ProgressNoteService(
    private val notes: ProgressNoteRepository,
    private val activities: ActivityRepository,
    private val uar: UserActivityRoleRepository
) {
    @Transactional
    fun create(
        teacherId: UUID?,
        studentId: UUID,
        activityId: UUID,
        comment: String,
        title: String? = null,
        metricsJson: String? = null,
        visibleToStudent: Boolean = true
    ): UUID {
        require(teacherId != studentId) { "teacherId y studentId no pueden ser iguales" }
        require(comment.isNotBlank()) { "comment vacío" }
        // Validar que el profe enseña esa actividad
        require(uar.existsByUserIdAndActivity_IdAndRoleType(teacherId, activityId, RoleType.TEACHES)) {
            "El profesor no tiene TEACHES en esa actividad"
        }
        // (opcional) Validar que el alumno aprende esa actividad
        // require(uar.existsByUserIdAndActivity_IdAndRoleType(studentId, activityId, RoleType.LEARNS)) { ... }

        val activity = activities.findById(activityId).orElseThrow()
        val pn = ProgressNote(
            activity = activity,
            teacherId = teacherId,
            studentId = studentId,
            createdAt = Instant.now(),
            title = title,
            comment = comment,
            metricsJson = metricsJson,
            visibleToStudent = visibleToStudent
        )
        return notes.save(pn).id
    }

    fun listForStudentView(studentId: UUID?): List<ProgressNote> =
        notes.findByStudentIdOrderByCreatedAtDesc(studentId)
            .filter { it.visibleToStudent }

    fun listForTeacherView(teacherId: UUID?, studentId: UUID): List<ProgressNote> =
        notes.findByTeacherIdAndStudentIdOrderByCreatedAtDesc(teacherId, studentId)
}
