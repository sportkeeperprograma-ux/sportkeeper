package com.gimapp.sportkeeper.repo

import com.gimapp.sportkeeper.domain.Reservation
import com.gimapp.sportkeeper.domain.ReservationStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface ReservationRepository : JpaRepository<Reservation, UUID> {
    fun countByTimeSlotIdAndStatus(timeSlotId: UUID, status: ReservationStatus = ReservationStatus.ACTIVE): Int
    fun existsByUserIdAndTimeSlotIdAndStatus(userId: UUID, timeSlotId: UUID, status: ReservationStatus = ReservationStatus.ACTIVE): Boolean
}