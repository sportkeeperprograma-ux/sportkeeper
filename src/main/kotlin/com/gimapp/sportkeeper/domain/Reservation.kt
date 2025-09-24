package com.gimapp.sportkeeper.domain


import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*


@Entity
@Table(
    name = "reservations",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "time_slot_id"])]
)
class Reservation(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_id", nullable = false)
    var timeSlot: TimeSlot,


    @Enumerated(EnumType.STRING)
    var status: ReservationStatus = ReservationStatus.ACTIVE,


    var createdAt: OffsetDateTime = OffsetDateTime.now(),
)