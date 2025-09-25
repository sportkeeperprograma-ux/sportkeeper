package com.gimapp.sportkeeper.repo


import com.gimapp.sportkeeper.domain.TimeSlot
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import java.util.*


interface TimeSlotRepository : JpaRepository<TimeSlot, UUID>{
    fun findByCoachId(coachId: UUID): List<TimeSlot>
    fun findByCoachIdAndStartAtBetween(coachId: UUID, from: Instant, to: Instant): List<TimeSlot>
}