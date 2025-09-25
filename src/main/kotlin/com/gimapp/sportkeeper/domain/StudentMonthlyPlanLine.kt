package com.gimapp.sportkeeper.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "student_monthly_plan_line")
class StudentMonthlyPlanLine(
    @Id
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id")
    val plan: StudentMonthlyPlan,

    @Column(nullable = false, length = 128)
    val label: String,                    // p.ej. "Cuota BJJ (sep 2025)"

    @Column(nullable = false, precision = 10, scale = 2)
    val amount: BigDecimal                // +importe o -descuento

    // Si más adelante quieres trazar la regla/precio usado, añade aquí ruleId/priceId opcional.
)
