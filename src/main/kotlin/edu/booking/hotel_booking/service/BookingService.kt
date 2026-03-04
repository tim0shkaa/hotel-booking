package edu.booking.hotel_booking.service

import edu.booking.hotel_booking.dao.BookingDao
import edu.booking.hotel_booking.dao.GuestDao
import edu.booking.hotel_booking.dao.RoomDao
import edu.booking.hotel_booking.dto.request.BookingRequest
import edu.booking.hotel_booking.dto.response.BookingResponse
import edu.booking.hotel_booking.dto.response.GuestResponse
import edu.booking.hotel_booking.dto.response.RoomResponse
import edu.booking.hotel_booking.entity.BookingEntity
import edu.booking.hotel_booking.entity.GuestEntity
import edu.booking.hotel_booking.entity.RoomEntity
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.UUID

@Service
class BookingService (
    private val bookingDao: BookingDao,
    private val roomDao: RoomDao,
    private val guestDao: GuestDao
){
    fun findBookingById(id: UUID): BookingResponse? {
        val entity = bookingDao.findById(id)
        return entity?.let { entityToResponse(it) }
    }

    fun createBooking(booking: BookingRequest): BookingResponse {
        val entity = BookingEntity(
            id = UUID.randomUUID(),
            checkIn = booking.checkIn,
            checkOut = booking.checkOut,
            guests = booking.guestsIds.map {findEntityById(it)},
            roomId = booking.roomId,
        )
        bookingDao.create(entity)
        return entityToResponse(entity)
    }

    fun updateBooking(id: UUID, booking: BookingRequest): BookingResponse? {
        val exist = bookingDao.findById(id) ?: throw NoSuchElementException("Booking with id $id not found")
        val entity = exist.copy(
            checkIn = booking.checkIn,
            checkOut = booking.checkOut,
            guests = booking.guestsIds.map {findEntityById(it)},
            roomId = booking.roomId
        )
        bookingDao.update(entity)
        return entityToResponse(entity)
    }

    fun deleteBooking(id: UUID) {
        val deleted = bookingDao.delete(id)
        if (deleted == 0) throw NoSuchElementException("Booking with id $id not found")
    }

    fun findPossibleRooms(checkIn: OffsetDateTime, checkOut: OffsetDateTime): List<RoomResponse> {
        val entity = roomDao.findPossibleRoom(checkIn, checkOut)
        return entity.map { entityToResponse(it) }
    }


    private fun entityToResponse(entity: BookingEntity): BookingResponse {
        return BookingResponse(
            id = entity.id,
            checkIn = entity.checkIn,
            checkOut = entity.checkOut,
            guests = entity.guests.map {entityToResponse(it)},
            roomId = entity.roomId,
        )
    }

    private fun entityToResponse(entity: GuestEntity): GuestResponse {
        return GuestResponse(
            id = entity.id,
            firstName = entity.firstName,
            surname = entity.surname,
            patronymic = entity.patronymic,
            birthDate = entity.birthDate,
            phoneNumber = entity.phoneNumber
        )
    }

    private fun entityToResponse(entity: RoomEntity): RoomResponse {
        return RoomResponse(
            id = entity.id,
            floor = entity.floor,
            roomNumber = entity.roomNumber,
            capacity = entity.capacity
        )
    }

    private fun findEntityById(id: UUID): GuestEntity {
        return guestDao.findById(id) ?: throw NoSuchElementException("Guest not found")
    }
}