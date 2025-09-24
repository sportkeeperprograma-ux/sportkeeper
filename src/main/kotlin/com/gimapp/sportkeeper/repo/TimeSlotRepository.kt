package com.gimapp.sportkeeper.repo


import com.gimapp.sportkeeper.domain.TimeSlot
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface TimeSlotRepository : JpaRepository<TimeSlot, UUID>