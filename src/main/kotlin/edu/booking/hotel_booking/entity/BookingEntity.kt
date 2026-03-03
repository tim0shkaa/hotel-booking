package edu.booking.hotel_booking.entity

import java.time.OffsetDateTime
import java.util.UUID

data class BookingEntity(
    val id : UUID,
    val checkIn : OffsetDateTime,
    val checkOut: OffsetDateTime,
    val guests : List<GuestEntity>,
    val roomId : UUID
)
