package com.gimapp.sportkeeper.web

import com.gimapp.sportkeeper.domain.StudentMonthlyPlan
import com.gimapp.sportkeeper.service.BillingService
import com.gimapp.sportkeeper.web.dto.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

@RestController
@RequestMapping("/api/admin/billing")
class AdminBillingController(
    private val billing: BillingService
) {
    @PutMapping("/plans/{studentId}/{yearMonth}")
    @PreAuthorize("hasRole('ADMIN')")
    fun upsertPlan(
        @PathVariable studentId: UUID,
        @PathVariable yearMonth: String,
        @RequestBody req: UpsertPlanRequest
    ): StudentMonthlyPlanResponse {
        val ym = YearMonth.parse(yearMonth) // "YYYY-MM"
        val plan = billing.upsertPlan(studentId, ym, req.activityIds)
        return plan.toResponse()
    }

    @GetMapping("/plans/{studentId}/{yearMonth}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    fun getPlan(
        @PathVariable studentId: UUID,
        @PathVariable yearMonth: String
    ): StudentMonthlyPlanResponse {
        val ym = YearMonth.parse(yearMonth)
        val plan = billing.upsertPlan(studentId, ym, emptySet()) // devuelve existente si lo hay
        return plan.toResponse()
    }

    @PostMapping("/plans/{studentId}/{yearMonth}/mark-paid")
    @PreAuthorize("hasRole('ADMIN')")
    fun markPaid(
        @PathVariable studentId: UUID,
        @PathVariable yearMonth: String
    ): Map<String, String> {
        val ym = YearMonth.parse(yearMonth)
        billing.markPaid(studentId, ym)
        return mapOf("status" to "OK")
    }
}

// --- Mapeo a DTO (extensión simple) ---
private fun StudentMonthlyPlan.toResponse(): StudentMonthlyPlanResponse {
    val dtf = DateTimeFormatter.ISO_INSTANT
    return StudentMonthlyPlanResponse(
        id = this.id,
        studentId = this.studentId,
        yearMonth = this.yearMonth,
        status = this.status.name,
        paidAt = this.paidAt?.let(dtf::format),
        totalAmount = this.totalAmount,
        activities = this.activities.map {
            val act = it.activity
            com.gimapp.sportkeeper.web.dto.PlanActivityDto(
                id = act.id, code = act.code, name = act.name
            )
        },
        lines = this.lines.map {
            com.gimapp.sportkeeper.web.dto.PlanLineDto(label = it.label, amount = it.amount)
        },
        notes = this.notes
    )
}
