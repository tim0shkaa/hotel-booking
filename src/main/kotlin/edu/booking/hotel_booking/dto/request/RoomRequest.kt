package edu.booking.hotel_booking.dto.request

data class RoomRequest(
    val floor : Int,
    val roomNumber: Int,
    val capacity : Int
)
