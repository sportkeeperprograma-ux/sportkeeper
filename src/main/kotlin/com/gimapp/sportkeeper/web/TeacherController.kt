package com.gimapp.sportkeeper.web

import com.gimapp.sportkeeper.service.ProgressNoteService
import com.gimapp.sportkeeper.service.TeacherRosterService
import com.gimapp.sportkeeper.web.dto.*
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.time.format.DateTimeFormatter
import java.util.*

@RestController
@RequestMapping("/api/teacher")
class TeacherController(
    private val roster: TeacherRosterService,
    private val notes: ProgressNoteService
) {

    private fun currentUserId(): UUID? =
        (org.springframework.security.core.context.SecurityContextHolder
            .getContext().authentication.principal as com.gimapp.sportkeeper.infra.security.MyPrincipal).userId

    // --- Asistentes de un slot del profesor ---
    @GetMapping("/slots/{slotId}/attendees")
    @PreAuthorize("hasRole('COACH') or hasRole('ADMIN')")
    fun attendees(@PathVariable slotId: UUID): AttendeesResponse {
        val list = roster.getSlotAttendees(slotId)
        return AttendeesResponse(slotId = slotId, studentIds = list)
    }

    // --- Crear nota de progreso sobre un alumno ---
    @PostMapping("/students/{studentId}/notes")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('COACH') or hasRole('ADMIN')")
    fun createNote(
        @PathVariable studentId: UUID,
        @RequestBody body: ProgressNoteCreateRequest
    ): Map<String, UUID> {
        val teacherId = currentUserId()
        val id = notes.create(
            teacherId = teacherId,
            studentId = studentId,
            activityId = body.activityId,
            comment = body.comment,
            title = body.title,
            metricsJson = body.metricsJson,
            visibleToStudent = body.visibleToStudent
        )
        return mapOf("id" to id)
    }

    // --- Listar mis notas sobre un alumno (vista profesor) ---
    @GetMapping("/students/{studentId}/notes")
    @PreAuthorize("hasRole('COACH') or hasRole('ADMIN')")
    @Transactional(readOnly = true)
    fun listNotesForMyStudent(@PathVariable studentId: UUID): List<ProgressNoteResponse> {
        val teacherId = currentUserId()
        val dtf = DateTimeFormatter.ISO_INSTANT
        return notes.listForTeacherView(teacherId, studentId).map {
            ProgressNoteResponse(
                id = it.id,
                activityId = it.activity.id,
                teacherId = it.teacherId,
                studentId = it.studentId,
                createdAt = dtf.format(it.createdAt),
                title = it.title,
                comment = it.comment,
                visibleToStudent = it.visibleToStudent
            )
        }
    }
}
