package com.gimapp.sportkeeper.web.dto


import java.time.LocalDateTime
import java.util.*


data class SlotDto(
    val id: UUID,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val capacity: Int,
    val reservedCount: Int,
    val name: String,          // 👈 nuevo
    val description: String    // 👈 nuevo
)