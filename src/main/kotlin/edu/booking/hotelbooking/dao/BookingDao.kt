package edu.booking.hotelbooking.dao

import edu.booking.hotelbooking.entity.BookingEntity
import edu.booking.hotelbooking.entity.GuestEntity
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

@Repository
class BookingDao(private val jdbcTemplate: NamedParameterJdbcTemplate) {
    private fun mapBooking(
        resultSet: ResultSet,
        rowNum: Int,
    ): BookingEntity {
        return BookingEntity(
            id = resultSet.getObject("id", UUID::class.java),
            checkIn = resultSet.getObject("check_in", OffsetDateTime::class.java),
            checkOut = resultSet.getObject("check_out", OffsetDateTime::class.java),
            guests = emptyList(),
            roomId = resultSet.getObject("room_id", UUID::class.java),
        )
    }

    private fun mapGuest(
        resultSet: ResultSet,
        rowNum: Int,
    ): GuestEntity {
        return GuestEntity(
            id = resultSet.getObject("id", UUID::class.java),
            firstName = resultSet.getString("first_name"),
            surname = resultSet.getString("surname"),
            patronymic = resultSet.getString("patronymic"),
            birthDate = resultSet.getObject("birth_date", LocalDate::class.java),
            phoneNumber = resultSet.getString("phone_number"),
        )
    }

    fun findById(id: UUID): BookingEntity? {
        val sqlQueryOnBooking = "SELECT * FROM booking WHERE id = :id"
        val parametersOnBooking = mapOf("id" to id)
        val booking = jdbcTemplate.query(sqlQueryOnBooking, parametersOnBooking, ::mapBooking).firstOrNull()

        val sqlQueryOnGuest = """SELECT g.* FROM guest g
            JOIN booking_guest bg ON g.id = bg.guest_id
            WHERE bg.booking_id = :bookingId
        """
        val parametersOnGuest = mapOf("bookingId" to id)
        val guestsList = jdbcTemplate.query(sqlQueryOnGuest, parametersOnGuest, ::mapGuest)
        return booking?.copy(guests = guestsList)
    }

    fun create(booking: BookingEntity) {
        val sqlQueryBooking = """INSERT INTO booking (id, check_in, check_out, room_id)
            VALUES (:id, :checkIn, :checkOut, :roomId)
        """
        val parameters =
            mapOf(
                "id" to booking.id,
                "checkIn" to booking.checkIn,
                "checkOut" to booking.checkOut,
                "roomId" to booking.roomId,
            )
        jdbcTemplate.update(sqlQueryBooking, parameters)

        insertBookingGuest(booking.id, booking.guests)
    }

    fun update(booking: BookingEntity) {
        val sqlQueryBooking = """UPDATE booking SET check_in = :checkIn, check_out = :checkOut, room_id = :roomId
            WHERE id = :id
        """
        val parameters =
            mapOf(
                "id" to booking.id,
                "checkIn" to booking.checkIn,
                "checkOut" to booking.checkOut,
                "roomId" to booking.roomId,
            )
        jdbcTemplate.update(sqlQueryBooking, parameters)

        val sqlDeleteQueryBookingGuest = """DELETE FROM booking_guest WHERE booking_id = :bookingId"""
        val parametersDelete =
            mapOf(
                "bookingId" to booking.id,
            )
        jdbcTemplate.update(sqlDeleteQueryBookingGuest, parametersDelete)

        insertBookingGuest(booking.id, booking.guests)
    }

    fun delete(id: UUID): Int {
        val sqlQueryBooking = """DELETE FROM booking WHERE id = :id"""
        val parametersBooking =
            mapOf(
                "id" to id,
            )
        return jdbcTemplate.update(sqlQueryBooking, parametersBooking)
    }

    private fun insertBookingGuest(
        bookingId: UUID,
        guests: List<GuestEntity>,
    ) {
        for (guest in guests) {
            val sqlQueryInsertBookingGuest = """INSERT INTO booking_guest (guest_id, booking_id)
                VALUES (:guestId, :bookingId)
            """
            val parametersInsert =
                mapOf(
                    "guestId" to guest.id,
                    "bookingId" to bookingId,
                )
            jdbcTemplate.update(sqlQueryInsertBookingGuest, parametersInsert)
        }
    }
}
