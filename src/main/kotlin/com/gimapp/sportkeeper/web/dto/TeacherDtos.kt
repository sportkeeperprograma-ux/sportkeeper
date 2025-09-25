package com.gimapp.sportkeeper.web.dto

import java.util.*

data class AttendeesResponse(
    val slotId: UUID,
    val studentIds: List<UUID>
)

data class ProgressNoteCreateRequest(
    val activityId: UUID,
    val comment: String,
    val title: String? = null,
    val metricsJson: String? = null,
    val visibleToStudent: Boolean = true
)

data class ProgressNoteResponse(
    val id: UUID,
    val activityId: UUID,
    val teacherId: UUID?,
    val studentId: UUID,
    val createdAt: String,
    val title: String?,
    val comment: String,
    val visibleToStudent: Boolean
)
