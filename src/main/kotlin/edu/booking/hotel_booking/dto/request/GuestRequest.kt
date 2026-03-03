package edu.booking.hotel_booking.dto.request

import java.time.LocalDate

data class GuestRequest(
    val firstName: String,
    val surname: String,
    val patronymic: String?,
    val birthDate: LocalDate,
    val phoneNumber: String
)
