package com.gimapp.sportkeeper.domain

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(
    name = "progress_note",
    indexes = [
        Index(columnList = "student_id, activity_id, created_at"),
        Index(columnList = "teacher_id, activity_id, created_at")
    ]
)
class ProgressNote(
    @Id
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id")
    val activity: Activity,

    @Column(name = "teacher_id", nullable = false)
    val teacherId: UUID?,

    @Column(name = "student_id", nullable = false)
    val studentId: UUID,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(nullable = true, length = 128)
    val title: String? = null,

    @Lob
    @Column(nullable = false)
    val comment: String,

    // JSON/text opcional para métricas (fuerza, técnica, cardio, etc.)
    @Lob
    @Column(name = "metrics_json", nullable = true)
    val metricsJson: String? = null,

    @Column(name = "visible_to_student", nullable = false)
    val visibleToStudent: Boolean = true
) {
    init {
        require(comment.isNotBlank()) { "comment no puede estar vacío" }
        require(teacherId != studentId) { "teacherId y studentId no pueden ser iguales" }
    }
}
