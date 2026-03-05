package edu.booking.hotelbooking.service

import edu.booking.hotelbooking.dao.RoomDao
import edu.booking.hotelbooking.dto.request.RoomRequest
import edu.booking.hotelbooking.dto.response.RoomResponse
import edu.booking.hotelbooking.entity.RoomEntity
import edu.booking.hotelbooking.kafka.EventType
import edu.booking.hotelbooking.kafka.KafkaProducer
import edu.booking.hotelbooking.kafka.event.RoomEvent
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RoomService(
    private val roomDao: RoomDao,
    private val kafkaProducer: KafkaProducer
) {
    fun findRoomById(id: UUID): RoomResponse? {
        val entity = roomDao.findById(id)
        return entity?.let { entityToResponse(it) }
    }

    fun createRoom(request: RoomRequest): RoomResponse {
        val entity =
            RoomEntity(
                id = UUID.randomUUID(),
                floor = request.floor,
                roomNumber = request.roomNumber,
                capacity = request.capacity,
            )
        roomDao.create(entity)
        val response = entityToResponse(entity)
        kafkaProducer.sendRoomEvent(RoomEvent(EventType.CREATED, response))
        return response
    }

    fun updateRoom(
        id: UUID,
        request: RoomRequest,
    ): RoomResponse {
        val exist = roomDao.findById(id) ?: throw NoSuchElementException("Room with id $id not found")
        val entity =
            exist.copy(
                floor = request.floor,
                roomNumber = request.roomNumber,
                capacity = request.capacity,
            )
        roomDao.update(entity)
        val response = entityToResponse(entity)
        kafkaProducer.sendRoomEvent(RoomEvent(EventType.UPDATED, response))
        return response
    }

    fun deleteRoom(id: UUID) {
        val entity = roomDao.findById(id) ?: throw NoSuchElementException("Room with id $id not found")
        roomDao.delete(id)
        kafkaProducer.sendRoomEvent(RoomEvent(EventType.DELETED, entityToResponse(entity)))
    }

    private fun entityToResponse(entity: RoomEntity): RoomResponse {
        return RoomResponse(
            id = entity.id,
            floor = entity.floor,
            roomNumber = entity.roomNumber,
            capacity = entity.capacity,
        )
    }
}
