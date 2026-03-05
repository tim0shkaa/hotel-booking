package edu.booking.hotelbooking.kafka.event

import edu.booking.hotelbooking.dto.response.BookingResponse
import edu.booking.hotelbooking.kafka.EventType

data class BookingEvent(
    val eventType: EventType,
    val data: BookingResponse
)
