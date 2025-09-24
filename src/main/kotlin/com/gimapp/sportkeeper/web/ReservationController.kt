package com.gimapp.sportkeeper.web


import com.gimapp.sportkeeper.domain.Reservation
import com.gimapp.sportkeeper.domain.ReservationStatus
import com.gimapp.sportkeeper.repo.ReservationRepository
import com.gimapp.sportkeeper.repo.TimeSlotRepository
import com.gimapp.sportkeeper.repo.UserRepository
import com.gimapp.sportkeeper.web.dto.CreateReservationRequest
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/reservations")
class ReservationController(
    private val users: UserRepository,
    private val slots: TimeSlotRepository,
    private val reservations: ReservationRepository,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    fun create(@RequestBody body: CreateReservationRequest): Map<String, Any> {
        val slot = slots.findById(body.timeSlotId).orElseThrow { NoSuchElementException("Slot not found") }
        val email = SecurityContextHolder.getContext().authentication?.name ?: error("Unauthorized")
        val user = users.findByEmail(email)!!


        if (reservations.existsByUserIdAndTimeSlotIdAndStatus(user.id!!, slot.id!!)) {
            throw IllegalStateException("Already reserved")
        }
        val reserved = reservations.countByTimeSlotIdAndStatus(slot.id!!)
        if (reserved >= slot.capacity) {
            throw IllegalStateException("Full")
        }
        val r = reservations.save(Reservation(user = user, timeSlot = slot, status = ReservationStatus.ACTIVE))
        return mapOf("id" to r.id!!)
    }
}