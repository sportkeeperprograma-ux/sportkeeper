package com.gimapp.sportkeeper.domain

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant
import java.time.YearMonth
import java.util.*

@Entity
@Table(
    name = "student_monthly_plan",
    uniqueConstraints = [UniqueConstraint(columnNames = ["student_id", "year_month"])]
)
class StudentMonthlyPlan(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(name = "student_id", nullable = false)
    val studentId: UUID,

    // formato "YYYY-MM" para portabilidad
    @Column(name = "year_month", nullable = false, length = 7)
    val yearMonth: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: PlanStatus = PlanStatus.UNPAID,

    @Column(name = "paid_at", nullable = true)
    var paidAt: Instant? = null,

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    var totalAmount: BigDecimal = BigDecimal.ZERO,

    @Lob
    @Column(nullable = true)
    var notes: String? = null
) {
    @OneToMany(mappedBy = "plan", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var activities: MutableSet<StudentMonthlyPlanActivity> = mutableSetOf()

    @OneToMany(mappedBy = "plan", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var lines: MutableList<StudentMonthlyPlanLine> = mutableListOf()

    fun updateActivities(newOnes: Set<Activity>) {
        activities.clear()
        activities.addAll(newOnes.map { StudentMonthlyPlanActivity(plan = this, activity = it) })
    }

    fun updateLines(newLines: List<StudentMonthlyPlanLine>) {
        lines.clear()
        lines.addAll(newLines)
        recalcTotal()
    }

    fun recalcTotal() {
        totalAmount = lines.fold(BigDecimal.ZERO) { acc, l -> acc + l.amount }
    }

    companion object {
        fun ym(ym: YearMonth) = ym.toString() // "2025-09"
    }
}
