package com.gimapp.sportkeeper.web.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.*

data class ActivityUpsertRequest(
    @field:NotBlank @field:Size(max = 64)
    val code: String,
    @field:NotBlank @field:Size(max = 128)
    val name: String,
    val active: Boolean = true
)

data class ActivityResponse(
    val id: UUID,
    val code: String,
    val name: String,
    val active: Boolean
)
