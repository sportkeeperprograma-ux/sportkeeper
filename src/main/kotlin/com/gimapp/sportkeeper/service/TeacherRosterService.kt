package com.gimapp.sportkeeper.service

import com.gimapp.sportkeeper.domain.ReservationStatus
import com.gimapp.sportkeeper.repo.ReservationRepository
import com.gimapp.sportkeeper.repo.TimeSlotRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class TeacherRosterService(
    private val slots: TimeSlotRepository,
    private val reservations: ReservationRepository
) {
    private val attending = listOf(ReservationStatus.ACTIVE, ReservationStatus.ATTENDED)

    fun getSlotAttendees(slotId: UUID): List<UUID> =
        reservations.findByTimeSlotIdAndStatusIn(slotId, attending)
            .map { it.user.id as UUID }  // adapta si tu Reservation guarda userId o User

    fun getMyRoster(coachId: UUID, from: Instant?, to: Instant?): Set<UUID> {
        val mySlots = when {
            from != null && to != null -> slots.findByCoachIdAndStartAtBetween(coachId, from, to)
            else -> slots.findByCoachId(coachId)
        }
        var studentIds = mutableSetOf<UUID?>()
        mySlots.forEach { s ->
            reservations.findByTimeSlotIdAndStatusIn(s.id, attending).forEach { r ->
                studentIds += r.user.id
            }
        }
        return studentIds as Set<UUID>
    }
}
