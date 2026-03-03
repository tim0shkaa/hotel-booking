package edu.booking.hotel_booking.dao

import edu.booking.hotel_booking.entity.RoomEntity
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.UUID

@Repository
class RoomDao (
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {
    private fun mapRoom(resultSet: ResultSet, rowNum : Int) : RoomEntity {
        return RoomEntity(
            id = resultSet.getObject("id", UUID::class.java),
            floor = resultSet.getInt("floor"),
            roomNumber = resultSet.getInt("roomNumber"),
            capacity = resultSet.getInt("capacity")
        )
    }

    fun findById(id : UUID) : RoomEntity? {
        val sqlQuery = "SELECT * FROM rooms WHERE id = :id"
        val parameters = mapOf("id" to id)
        return jdbcTemplate.query(sqlQuery, parameters, ::mapRoom).firstOrNull()
    }

    fun create(room : RoomEntity) {
        val sqlQuery = """INSERT INTO rooms (id, floor, roomNumber, capacity)
            VALUES (:id, :floor, :roomNumber, :capacity)
        """
        val parameters = mapOf(
            "id" to room.id,
            "floor" to room.floor,
            "roomNumber" to room.roomNumber,
            "capacity" to room.capacity
        )
        jdbcTemplate.update(sqlQuery, parameters)
    }

    fun update(id : UUID, room : RoomEntity) {
        val sqlQuery = """UPDATE rooms SET floor = :floor, roomNumber = :roomNumber, capacity = :capacity
            WHERE id = :id"""
        val parameters = mapOf(
            "id" to id,
            "floor" to room.floor,
            "roomNumber" to room.roomNumber,
            "capacity" to room.capacity
        )
        jdbcTemplate.update(sqlQuery, parameters)
    }

    fun delete(id : UUID) {
        val sqlQuery = """DELETE FROM rooms WHERE id = :id"""
        val parameters = mapOf("id" to id)
        jdbcTemplate.update(sqlQuery, parameters)
    }
}