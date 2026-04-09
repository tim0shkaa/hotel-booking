package edu.booking.hotelbooking.dto.response

import java.time.LocalDate
import java.util.UUID

data class GuestResponse(
    val id: UUID,
    val firstName: String,
    val surname: String,
    val patronymic: String?,
    val birthDate: LocalDate,
    val phoneNumber: String,
)
