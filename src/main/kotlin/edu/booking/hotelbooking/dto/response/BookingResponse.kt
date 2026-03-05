package edu.booking.hotelbooking.dto.response

import java.time.OffsetDateTime
import java.util.UUID

data class BookingResponse(
    val id: UUID,
    val checkIn: OffsetDateTime,
    val checkOut: OffsetDateTime,
    val guests: List<GuestResponse>,
    val roomId: UUID,
)
