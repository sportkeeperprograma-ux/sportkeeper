package com.gimapp.sportkeeper.repo

import com.gimapp.sportkeeper.domain.ProgressNote
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProgressNoteRepository : JpaRepository<ProgressNote, UUID> {
    fun findByStudentIdOrderByCreatedAtDesc(studentId: UUID?): List<ProgressNote>
    fun findByTeacherIdAndStudentIdOrderByCreatedAtDesc(teacherId: UUID?, studentId: UUID): List<ProgressNote>
}
