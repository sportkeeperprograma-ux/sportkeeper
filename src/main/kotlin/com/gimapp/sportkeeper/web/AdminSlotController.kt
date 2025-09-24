package com.gimapp.sportkeeper.web

import com.gimapp.sportkeeper.domain.TimeSlot
import com.gimapp.sportkeeper.repo.TimeSlotRepository
import com.gimapp.sportkeeper.web.dto.CreateSlotReq
import com.gimapp.sportkeeper.web.dto.UpdateSlotReq
import com.gimapp.sportkeeper.web.dto.SlotRes
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/admin/slots")
@PreAuthorize("hasRole('ADMIN')")
class AdminSlotController(
    private val slots: TimeSlotRepository
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody req: CreateSlotReq): SlotRes {
        require(req.endAt.isAfter(req.startAt)) { "endAt must be after startAt" }
        val saved = slots.save(TimeSlot(startAt = req.startAt, endAt = req.endAt, capacity = req.capacity, name = req.name, description = req.description))
        return SlotRes(saved.id!!, saved.startAt, saved.endAt, saved.capacity, saved.name, saved.description)
    }

    @PutMapping("/{id}")
    @Transactional
    fun update(@PathVariable id: UUID, @Valid @RequestBody req: UpdateSlotReq): SlotRes {
        val s = slots.findById(id).orElseThrow()
        req.startAt?.let { s.startAt = it }
        req.endAt?.let { s.endAt = it }
        req.capacity?.let { s.capacity = it }
        req.name?.let { s.name = it }
        req.description?.let { s.description = it }
        require(s.endAt.isAfter(s.startAt)) { "endAt must be after startAt" }
        return SlotRes(s.id!!, s.startAt, s.endAt, s.capacity, s.name, s.description)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID) {
        if (!slots.existsById(id)) throw NoSuchElementException("Slot not found")
        slots.deleteById(id)
    }
}
