package edu.booking.hotel_booking.dto.request

import java.time.OffsetDateTime
import java.util.UUID

data class BookingRequest(
    val checkIn : OffsetDateTime,
    val checkOut: OffsetDateTime,
    val guests : List<GuestRequest>,
    val roomId : UUID
)
