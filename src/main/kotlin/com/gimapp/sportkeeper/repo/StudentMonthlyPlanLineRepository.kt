package com.gimapp.sportkeeper.repo

import com.gimapp.sportkeeper.domain.StudentMonthlyPlanLine
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface StudentMonthlyPlanLineRepository : JpaRepository<StudentMonthlyPlanLine, UUID>