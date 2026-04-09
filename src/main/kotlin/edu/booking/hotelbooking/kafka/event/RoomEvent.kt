package edu.booking.hotelbooking.kafka.event

import edu.booking.hotelbooking.dto.response.RoomResponse
import edu.booking.hotelbooking.kafka.EventType

data class RoomEvent(
    val eventType: EventType,
    val data: RoomResponse
)
