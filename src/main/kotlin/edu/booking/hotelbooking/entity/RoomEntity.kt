package edu.booking.hotelbooking.entity

import java.util.UUID

data class RoomEntity(
    val id: UUID,
    val floor: Int,
    val roomNumber: Int,
    val capacity: Int,
)
