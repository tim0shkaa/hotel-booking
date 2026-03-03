package edu.booking.hotel_booking.dto.response

import java.util.UUID

data class RoomResponse(
    val id : UUID,
    val floor : Int,
    val roomNumber : Int,
    val capacity : Int
)
