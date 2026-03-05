package edu.booking.hotelbooking.kafka.event

import edu.booking.hotelbooking.dto.response.GuestResponse
import edu.booking.hotelbooking.kafka.EventType

data class GuestEvent(
    val eventType: EventType,
    val data: GuestResponse
)
