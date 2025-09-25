package com.gimapp.sportkeeper.web

import com.gimapp.sportkeeper.service.ProgressNoteService
import com.gimapp.sportkeeper.web.dto.ProgressNoteResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.format.DateTimeFormatter
import java.util.*

@RestController
@RequestMapping("/api/student")
class StudentController(
    private val notes: ProgressNoteService
) {
    private fun currentUserId(): UUID? =
        (org.springframework.security.core.context.SecurityContextHolder
            .getContext().authentication.principal as com.gimapp.sportkeeper.infra.security.MyPrincipal).userId

    @GetMapping("/me/notes")
    @PreAuthorize("hasAnyRole('MEMBER','COACH','ADMIN')")
    @Transactional(readOnly = true)
    fun myNotes(): List<ProgressNoteResponse> {
        val studentId = currentUserId()
        val dtf = DateTimeFormatter.ISO_INSTANT
        return notes.listForStudentView(studentId).map {
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
