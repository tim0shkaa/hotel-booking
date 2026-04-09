package edu.booking.hotelbooking.entity

import java.time.LocalDate
import java.util.UUID

data class GuestEntity(
    val id: UUID,
    val firstName: String,
    val surname: String,
    val patronymic: String?,
    val birthDate: LocalDate,
    val phoneNumber: String,
)
