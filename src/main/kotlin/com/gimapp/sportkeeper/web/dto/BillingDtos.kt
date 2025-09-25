package com.gimapp.sportkeeper.web.dto

import java.math.BigDecimal
import java.util.*

data class UpsertPlanRequest(
    val activityIds: Set<UUID>
)

data class PlanLineDto(
    val label: String,
    val amount: BigDecimal
)

data class PlanActivityDto(
    val id: UUID,
    val code: String,
    val name: String
)

data class StudentMonthlyPlanResponse(
    val id: UUID,
    val studentId: UUID,
    val yearMonth: String,
    val status: String,
    val paidAt: String?,
    val totalAmount: BigDecimal,
    val activities: List<PlanActivityDto>,
    val lines: List<PlanLineDto>,
    val notes: String?
)
