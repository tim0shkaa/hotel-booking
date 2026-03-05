package edu.booking.hotelbooking.dto.request

data class RoomRequest(
    val floor: Int,
    val roomNumber: Int,
    val capacity: Int,
)
