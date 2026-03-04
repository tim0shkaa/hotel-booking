package edu.booking.hotel_booking.service

import edu.booking.hotel_booking.dao.GuestDao
import edu.booking.hotel_booking.dto.request.GuestRequest
import edu.booking.hotel_booking.dto.response.GuestResponse
import edu.booking.hotel_booking.entity.GuestEntity
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GuestService (private val guestDao: GuestDao) {

    fun findGuestById(id: UUID): GuestResponse? {
        val entity = guestDao.findById(id)
        return entity?.let { entityToResponse(it) }
    }

    fun createGuest(request: GuestRequest): GuestResponse {
        val entity = GuestEntity(
            id = UUID.randomUUID(),
            firstName = request.firstName,
            surname = request.surname,
            patronymic = request.patronymic,
            birthDate = request.birthDate,
            phoneNumber = request.phoneNumber
        )
        guestDao.create(entity)
        return entityToResponse(entity)
    }

    fun updateGuest(id: UUID, request: GuestRequest): GuestResponse? {
        val exist = guestDao.findById(id) ?: throw NoSuchElementException("Guest with id $id not found")
        val entity = exist.copy(
            firstName = request.firstName,
            surname = request.surname,
            patronymic = request.patronymic,
            birthDate = request.birthDate,
            phoneNumber = request.phoneNumber
        )
        guestDao.update(entity)
        return entityToResponse(entity)
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
}