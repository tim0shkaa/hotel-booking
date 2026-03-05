package edu.booking.hotelbooking.service

import edu.booking.hotelbooking.dao.GuestDao
import edu.booking.hotelbooking.dto.request.GuestRequest
import edu.booking.hotelbooking.entity.GuestEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class GuestServiceTest {
    @Mock
    lateinit var guestDao: GuestDao

    @InjectMocks
    lateinit var guestService: GuestService

    private val guestEntity =
        GuestEntity(
            id = UUID.randomUUID(),
            firstName = "Алексей",
            surname = "Тимохин",
            patronymic = "Вадимович",
            birthDate = LocalDate.of(2005, 7, 30),
            phoneNumber = "+79999999999",
        )

    @Test
    fun `findGuestById returns response when guest exists`() {
        whenever(guestDao.findById(guestEntity.id)).thenReturn(guestEntity)

        val result = guestService.findGuestById(guestEntity.id)

        assertNotNull(result)
        assertEquals("Алексей", result.firstName)
        assertEquals("Тимохин", result.surname)
        assertEquals("Вадимович", result.patronymic)
        assertEquals(LocalDate.of(2005, 7, 30), result.birthDate)
        assertEquals("+79999999999", result.phoneNumber)
    }

    @Test
    fun `findGuestById returns null when guest not found`() {
        whenever(guestDao.findById(any())).thenReturn(null)

        val result = guestService.findGuestById(UUID.randomUUID())

        assertNull(result)
    }

    @Test
    fun `createGuest returns correct response`() {
        val request =
            GuestRequest(
                firstName = "Алексей",
                surname = "Тимохин",
                patronymic = "Вадимович",
                birthDate = LocalDate.of(2005, 7, 30),
                phoneNumber = "+79999999999",
            )

        val result = guestService.createGuest(request)

        assertNotNull(result)
        assertEquals("Алексей", result.firstName)
        assertEquals("Тимохин", result.surname)
        assertEquals("Вадимович", result.patronymic)
        assertEquals(LocalDate.of(2005, 7, 30), result.birthDate)
        assertEquals("+79999999999", result.phoneNumber)
        verify(guestDao, times(1)).create(any())
    }

    @Test
    fun `updateGuest returns updated response when guest exists`() {
        val id = guestEntity.id
        val request =
            GuestRequest(
                firstName = "Пётр",
                surname = "Петров",
                patronymic = null,
                birthDate = LocalDate.of(2000, 5, 20),
                phoneNumber = "+79876543210",
            )
        whenever(guestDao.findById(id)).thenReturn(guestEntity)

        val result = guestService.updateGuest(id, request)

        assertNotNull(result)
        assertEquals("Пётр", result.firstName)
        assertEquals("Петров", result.surname)
        assertNull(result.patronymic)
        assertEquals(LocalDate.of(2000, 5, 20), result.birthDate)
        assertEquals("+79876543210", result.phoneNumber)
        verify(guestDao, times(1)).update(any())
    }

    @Test
    fun `updateGuest throws exception when guest not found`() {
        whenever(guestDao.findById(any())).thenReturn(null)

        assertThrows<NoSuchElementException> {
            guestService.updateGuest(
                UUID.randomUUID(),
                GuestRequest(
                    firstName = "Пётр",
                    surname = "Петров",
                    patronymic = null,
                    birthDate = LocalDate.of(2000, 5, 20),
                    phoneNumber = "+79999999999",
                ),
            )
        }
    }
}
