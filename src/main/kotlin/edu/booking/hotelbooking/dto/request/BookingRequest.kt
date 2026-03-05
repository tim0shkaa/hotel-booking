package edu.booking.hotelbooking.dto.request

import java.time.OffsetDateTime
import java.util.UUID

data class BookingRequest(
    val checkIn: OffsetDateTime,
    val checkOut: OffsetDateTime,
    val guestsIds: List<UUID>,
    val roomId: UUID,
)
