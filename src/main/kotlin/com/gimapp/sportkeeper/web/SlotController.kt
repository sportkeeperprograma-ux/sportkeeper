package com.gimapp.sportkeeper.web


import com.gimapp.sportkeeper.repo.ReservationRepository
import com.gimapp.sportkeeper.repo.TimeSlotRepository
import com.gimapp.sportkeeper.web.dto.SlotDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/slots")
class SlotController(
    private val timeSlotRepo: TimeSlotRepository,
    private val reservationRepo: ReservationRepository,
) {
    @GetMapping
    fun list(): List<SlotDto> = timeSlotRepo.findAll().map { s ->
        SlotDto(
            id = s.id!!,
            startAt = s.startAt,
            endAt = s.endAt,
            capacity = s.capacity,
            reservedCount = reservationRepo.countByTimeSlotIdAndStatus(s.id!!),
            name = s.name,
            description = s.description,
        )
    }
}