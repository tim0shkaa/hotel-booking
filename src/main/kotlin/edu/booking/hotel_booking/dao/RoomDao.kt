package edu.booking.hotel_booking.dao

import edu.booking.hotel_booking.entity.RoomEntity
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.OffsetDateTime
import java.util.UUID

@Repository
class RoomDao (private val jdbcTemplate: NamedParameterJdbcTemplate) {

    private fun mapRoom(resultSet: ResultSet, rowNum : Int) : RoomEntity {
        return RoomEntity(
            id = resultSet.getObject("id", UUID::class.java),
            floor = resultSet.getInt("floor"),
            roomNumber = resultSet.getInt("room_number"),
            capacity = resultSet.getInt("capacity")
        )
    }

    fun findById(id : UUID) : RoomEntity? {
        val sqlQuery = "SELECT * FROM room WHERE id = :id"
        val parameters = mapOf("id" to id)
        return jdbcTemplate.query(sqlQuery, parameters, ::mapRoom).firstOrNull()
    }

    fun create(room : RoomEntity) {
        val sqlQuery = """INSERT INTO room (id, floor, room_number, capacity)
            VALUES (:id, :floor, :room_number, :capacity)
        """
        val parameters = mapOf(
            "id" to room.id,
            "floor" to room.floor,
            "room_number" to room.roomNumber,
            "capacity" to room.capacity
        )
        jdbcTemplate.update(sqlQuery, parameters)
    }

    fun update(id : UUID, room : RoomEntity) {
        val sqlQuery = """UPDATE room SET floor = :floor, room_number = :room_number, capacity = :capacity
            WHERE id = :id"""
        val parameters = mapOf(
            "id" to id,
            "floor" to room.floor,
            "room_number" to room.roomNumber,
            "capacity" to room.capacity
        )
        jdbcTemplate.update(sqlQuery, parameters)
    }

    fun delete(id : UUID) {
        val sqlQuery = """DELETE FROM room WHERE id = :id"""
        val parameters = mapOf("id" to id)
        jdbcTemplate.update(sqlQuery, parameters)
    }

    fun findPossibleRoom(checkIn: OffsetDateTime, checkOut: OffsetDateTime) : List<RoomEntity> {
        val sqlQuery = """SELECT * FROM room r WHERE NOT EXISTS (
            SELECT 1 FROM booking b WHERE r.id = b.room_id AND
            b.check_in < :checkOut AND b.check_out > :checkIn 
        """
        val parameters = mapOf(
            "check_in" to checkIn,
            "check_out" to checkOut
        )
        return jdbcTemplate.query(sqlQuery, parameters, ::mapRoom)
    }
}