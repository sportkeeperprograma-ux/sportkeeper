package com.gimapp.sportkeeper.web.dto

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.*

data class CreateSlotReq(
    @field:NotNull @field:Future val startAt: LocalDateTime,
    @field:NotNull @field:Future val endAt: LocalDateTime,
    @field:Min(1) val capacity: Int = 30,
    @field:NotBlank val name: String,             // 👈 nuevo
    val description: String = ""                  // 👈 nuevo
)


data class UpdateSlotReq(
    val startAt: LocalDateTime? = null,
    val endAt: LocalDateTime? = null,
    @field:Min(1) val capacity: Int? = null,
    val name: String? = null,                     // 👈 nuevo
    val description: String? = null               // 👈 nuevo
)


data class SlotRes(
    val id: UUID,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val capacity: Int,
    val name: String,          // 👈 nuevo
    val description: String    // 👈 nuevo
)
