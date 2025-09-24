package com.gimapp.sportkeeper.domain


import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*


@Entity @Table(name = "time_slots")
class TimeSlot(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    var startAt: LocalDateTime = LocalDateTime.now(),
    var endAt: LocalDateTime = LocalDateTime.now().plusHours(1),
    var capacity: Int = 30,
    var name: String = "",            // 👈 nuevo
    var description: String = ""      // 👈 nuevo
)