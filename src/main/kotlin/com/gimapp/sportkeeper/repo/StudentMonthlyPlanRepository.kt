package com.gimapp.sportkeeper.repo

import com.gimapp.sportkeeper.domain.StudentMonthlyPlan
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID


interface StudentMonthlyPlanRepository : JpaRepository<StudentMonthlyPlan, UUID> {
    fun findByStudentIdAndYearMonth(studentId: UUID, yearMonth: String): Optional<StudentMonthlyPlan>
}