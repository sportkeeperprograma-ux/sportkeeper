package com.gimapp.sportkeeper.service

import com.gimapp.sportkeeper.domain.*
import com.gimapp.sportkeeper.repo.ActivityPriceRepository
import com.gimapp.sportkeeper.repo.ActivityRepository
import com.gimapp.sportkeeper.repo.StudentMonthlyPlanRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.YearMonth
import java.util.*

@Service
class BillingService(
    private val planRepo: StudentMonthlyPlanRepository,
    private val activityRepo: ActivityRepository,
    private val priceRepo: ActivityPriceRepository
) {
    @Transactional
    fun upsertPlan(studentId: UUID, ym: YearMonth, activityIds: Set<UUID>): StudentMonthlyPlan {
        val ymStr = ym.toString() // "YYYY-MM"
        val plan = planRepo.findByStudentIdAndYearMonth(studentId, ymStr)
            .orElseGet { StudentMonthlyPlan(studentId = studentId, yearMonth = ymStr) }

        // Cargar actividades
        val activities = activityRepo.findAllById(activityIds).toSet()
        plan.updateActivities(activities)

        // Calcular líneas: suma de precio vigente de cada actividad (día 1 del mes)
        val asOf = ym.atDay(1)
        val newLines = mutableListOf<StudentMonthlyPlanLine>()
        var total = BigDecimal.ZERO

        activities.forEach { act ->
            val price = priceRepo.findApplicablePrice(act.id, asOf).firstOrNull()
            val amount = price?.amount ?: BigDecimal.ZERO
            newLines += StudentMonthlyPlanLine(
                plan = plan,
                label = "Cuota ${act.name} ($ymStr)",
                amount = amount
            )
            total += amount
        }

        plan.updateLines(newLines)
        plan.totalAmount = total
        return planRepo.save(plan)
    }

    @Transactional
    fun markPaid(studentId: UUID, ym: YearMonth) {
        val plan = planRepo.findByStudentIdAndYearMonth(studentId, ym.toString())
            .orElseThrow { IllegalArgumentException("Plan no encontrado") }
        plan.status = PlanStatus.PAID
        plan.paidAt = java.time.Instant.now()
        planRepo.save(plan)
    }
}
