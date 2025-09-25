package com.gimapp.sportkeeper.repo

import com.gimapp.sportkeeper.domain.StudentMonthlyPlanActivity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface StudentMonthlyPlanActivityRepository : JpaRepository<StudentMonthlyPlanActivity, UUID>