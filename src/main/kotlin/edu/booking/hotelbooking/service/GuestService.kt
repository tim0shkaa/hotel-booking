package edu.booking.hotelbooking.service

import edu.booking.hotelbooking.dao.GuestDao
import edu.booking.hotelbooking.dto.request.GuestRequest
import edu.booking.hotelbooking.dto.response.GuestResponse
import edu.booking.hotelbooking.entity.GuestEntity
import edu.booking.hotelbooking.kafka.EventType
import edu.booking.hotelbooking.kafka.KafkaProducer
import edu.booking.hotelbooking.kafka.event.GuestEvent
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GuestService(
    private val guestDao: GuestDao,
    private val kafkaProducer: KafkaProducer
) {
    fun findGuestById(id: UUID): GuestResponse? {
        val entity = guestDao.findById(id)
        return entity?.let { entityToResponse(it) }
    }

    fun createGuest(request: GuestRequest): GuestResponse {
        val entity =
            GuestEntity(
                id = UUID.randomUUID(),
                firstName = request.firstName,
                surname = request.surname,
                patronymic = request.patronymic,
                birthDate = request.birthDate,
                phoneNumber = request.phoneNumber,
            )
        guestDao.create(entity)
        val response = entityToResponse(entity)
        kafkaProducer.sendGuestEvent(GuestEvent(EventType.CREATED, response))
        return response
    }

    fun updateGuest(
        id: UUID,
        request: GuestRequest,
    ): GuestResponse {
        val exist = guestDao.findById(id) ?: throw NoSuchElementException("Guest with id $id not found")
        val entity =
            exist.copy(
                firstName = request.firstName,
                surname = request.surname,
                patronymic = request.patronymic,
                birthDate = request.birthDate,
                phoneNumber = request.phoneNumber,
            )
        guestDao.update(entity)
        val response = entityToResponse(entity)
        kafkaProducer.sendGuestEvent(GuestEvent(EventType.UPDATED, response))
        return response
    }

    private fun entityToResponse(entity: GuestEntity): GuestResponse {
        return GuestResponse(
            id = entity.id,
            firstName = entity.firstName,
            surname = entity.surname,
            patronymic = entity.patronymic,
            birthDate = entity.birthDate,
            phoneNumber = entity.phoneNumber,
        )
    }
}
