package com.gimapp.sportkeeper.repo

import com.gimapp.sportkeeper.domain.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.util.*

interface ActivityPriceRepository : JpaRepository<ActivityPrice, UUID> {
    // Precio aplicable para una fecha (active_from <= date <= active_to/null)
    @Query("""
        select ap from ActivityPrice ap
        where ap.activity.id = :activityId
          and ap.activeFrom <= :asOf
          and (ap.activeTo is null or ap.activeTo >= :asOf)
        order by ap.activeFrom desc
    """)
    fun findApplicablePrice(activityId: UUID, asOf: LocalDate): List<ActivityPrice>
}



