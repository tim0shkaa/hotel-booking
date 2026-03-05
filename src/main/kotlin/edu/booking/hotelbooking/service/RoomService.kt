package edu.booking.hotelbooking.service

import edu.booking.hotelbooking.dao.RoomDao
import edu.booking.hotelbooking.dto.request.RoomRequest
import edu.booking.hotelbooking.dto.response.RoomResponse
import edu.booking.hotelbooking.entity.RoomEntity
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RoomService(private val roomDao: RoomDao) {
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
        return entityToResponse(entity)
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
        return entityToResponse(entity)
    }

    fun deleteRoom(id: UUID) {
        val deleted = roomDao.delete(id)
        if (deleted == 0) throw NoSuchElementException("Room with id $id not found")
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
