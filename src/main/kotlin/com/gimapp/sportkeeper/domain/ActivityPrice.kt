package com.gimapp.sportkeeper.domain

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Entity
@Table(
    name = "activity_price",
    indexes = [Index(columnList = "active_from, active_to")]
)
class ActivityPrice(
    @Id
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id")
    val activity: Activity,

    @Column(nullable = false, precision = 10, scale = 2)
    val amount: BigDecimal,

    @Column(name = "active_from", nullable = false)
    val activeFrom: LocalDate,

    @Column(name = "active_to", nullable = true)
    val activeTo: LocalDate? = null
) {
    init {
        require(amount >= BigDecimal.ZERO) { "amount no puede ser negativo" }
        activeTo?.let { require(!it.isBefore(activeFrom)) { "activeTo < activeFrom" } }
    }
}
