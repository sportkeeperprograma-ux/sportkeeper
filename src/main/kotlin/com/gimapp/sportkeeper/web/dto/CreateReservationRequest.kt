package com.gimapp.sportkeeper.web.dto


import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import java.util.*


data class CreateReservationRequest(
    @field:NotNull val timeSlotId: UUID,
    @field:Email val email: String, // temporal hasta tener auth real
)