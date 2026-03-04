package edu.booking.hotel_booking.dao

import edu.booking.hotel_booking.entity.GuestEntity
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDate
import java.util.UUID

@Repository
class GuestDao (private val jdbcTemplate: NamedParameterJdbcTemplate) {

    private fun paramsGuest(guest: GuestEntity) = mapOf(
        "id" to guest.id,
        "first_name" to guest.firstName,
        "surname" to guest.surname,
        "patronymic" to guest.patronymic,
        "birth_date" to guest.birthDate,
        "phone_number" to guest.phoneNumber
    )

    private fun mapGuest(resultSet: ResultSet, rowNum: Int): GuestEntity {
        return GuestEntity(
            id = resultSet.getObject("id", UUID::class.java),
            firstName = resultSet.getString("first_name"),
            surname = resultSet.getString("surname"),
            patronymic = resultSet.getString("patronymic"),
            birthDate = resultSet.getObject("birth_date", LocalDate::class.java),
            phoneNumber = resultSet.getString("phone_number"),
        )
    }

    fun findById(id : UUID) : GuestEntity? {
        val sqlQuery = "SELECT * FROM guest WHERE id = :id"
        val parameters = mapOf("id" to id)
        return jdbcTemplate.query(sqlQuery, parameters, ::mapGuest).firstOrNull()
    }

    fun create(guest: GuestEntity) {
        val sqlQuery = """INSERT INTO guest (id, first_name, surname, patronymic, birth_date, phone_number) 
            VALUES (:id, :first_name, :surname, :patronymic, :birth_date, :phone_number)"""
        val parameters = paramsGuest(guest)
        jdbcTemplate.update(sqlQuery, parameters)
    }

    fun update(guest: GuestEntity) {
        val sqlQuery = """UPDATE guest SET first_name = :first_name, surname = :surname, patronymic = :patronymic, birth_date = :birth_date, phone_number = :phone_number
            WHERE id = :id"""
        val parameters = paramsGuest(guest)
        jdbcTemplate.update(sqlQuery, parameters)
    }
}