package edu.booking.hotelbooking.service

import edu.booking.hotelbooking.dao.BookingDao
import edu.booking.hotelbooking.dao.GuestDao
import edu.booking.hotelbooking.dao.RoomDao
import edu.booking.hotelbooking.dto.request.BookingRequest
import edu.booking.hotelbooking.entity.BookingEntity
import edu.booking.hotelbooking.entity.GuestEntity
import edu.booking.hotelbooking.entity.RoomEntity
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
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
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class BookingServiceTest {
    @Mock
    lateinit var bookingDao: BookingDao

    @Mock
    lateinit var guestDao: GuestDao

    @Mock
    lateinit var roomDao: RoomDao

    @InjectMocks
    lateinit var bookingService: BookingService

    private val guestEntity1 =
        GuestEntity(
            id = UUID.randomUUID(),
            firstName = "Алексей",
            surname = "Тимохин",
            patronymic = "Вадимович",
            birthDate = LocalDate.of(2005, 7, 30),
            phoneNumber = "+79999999999",
        )

    private val guestEntity2 =
        GuestEntity(
            id = UUID.randomUUID(),
            firstName = "Петров",
            surname = "Петр",
            patronymic = null,
            birthDate = LocalDate.of(2000, 5, 20),
            phoneNumber = "+79876543210",
        )

    private val bookingEntity =
        BookingEntity(
            id = UUID.randomUUID(),
            checkIn = OffsetDateTime.of(2025, 3, 10, 14, 0, 0, 0, ZoneOffset.UTC),
            checkOut = OffsetDateTime.of(2025, 3, 15, 12, 0, 0, 0, ZoneOffset.UTC),
            guests = listOf(guestEntity1, guestEntity2),
            roomId = UUID.randomUUID(),
        )

    @Test
    fun `findBookingById returns response when booking exists`() {
        whenever(bookingDao.findById(bookingEntity.id)).thenReturn(bookingEntity)

        val result = bookingService.findBookingById(bookingEntity.id)

        assertNotNull(result)
        assertEquals(
            OffsetDateTime.of(2025, 3, 10, 14, 0, 0, 0, ZoneOffset.UTC),
            result.checkIn,
        )
        assertEquals(
            OffsetDateTime.of(2025, 3, 15, 12, 0, 0, 0, ZoneOffset.UTC),
            bookingEntity.checkOut,
        )
        assertEquals(2, result.guests.size)
    }

    @Test
    fun `findBookingById returns null when booking not found`() {
        whenever(bookingDao.findById(any())).thenReturn(null)

        val result = bookingService.findBookingById(UUID.randomUUID())

        assertNull(result)
    }

    @Test
    fun `createBooking returns correct response`() {
        val request =
            BookingRequest(
                checkIn = OffsetDateTime.of(2025, 3, 10, 14, 0, 0, 0, ZoneOffset.UTC),
                checkOut = OffsetDateTime.of(2025, 3, 15, 12, 0, 0, 0, ZoneOffset.UTC),
                guestsIds = listOf(guestEntity1.id, guestEntity2.id),
                roomId = bookingEntity.roomId,
            )
        whenever(guestDao.findById(guestEntity1.id)).thenReturn(guestEntity1)
        whenever(guestDao.findById(guestEntity2.id)).thenReturn(guestEntity2)

        val result = bookingService.createBooking(request)

        assertNotNull(result)
        assertEquals(
            OffsetDateTime.of(2025, 3, 10, 14, 0, 0, 0, ZoneOffset.UTC),
            result.checkIn,
        )
        assertEquals(
            OffsetDateTime.of(2025, 3, 15, 12, 0, 0, 0, ZoneOffset.UTC),
            result.checkOut,
        )
        assertEquals(2, result.guests.size)
        verify(bookingDao, times(1)).create(any())
    }

    @Test
    fun `createBooking throws exception when guest not found`() {
        whenever(guestDao.findById(any())).thenReturn(null)

        assertThrows<NoSuchElementException> {
            bookingService.createBooking(
                BookingRequest(
                    checkIn = OffsetDateTime.of(2025, 3, 10, 14, 0, 0, 0, ZoneOffset.UTC),
                    checkOut = OffsetDateTime.of(2025, 3, 15, 12, 0, 0, 0, ZoneOffset.UTC),
                    guestsIds = listOf(UUID.randomUUID()),
                    roomId = UUID.randomUUID(),
                ),
            )
        }
    }

    @Test
    fun `updateBooking returns updated response when booking exists`() {
        val request =
            BookingRequest(
                checkIn = OffsetDateTime.of(2025, 4, 1, 14, 0, 0, 0, ZoneOffset.UTC),
                checkOut = OffsetDateTime.of(2025, 4, 5, 12, 0, 0, 0, ZoneOffset.UTC),
                guestsIds = listOf(guestEntity1.id),
                roomId = bookingEntity.roomId,
            )
        whenever(bookingDao.findById(bookingEntity.id)).thenReturn(bookingEntity)
        whenever(guestDao.findById(guestEntity1.id)).thenReturn(guestEntity1)

        val result = bookingService.updateBooking(bookingEntity.id, request)

        assertNotNull(result)
        assertEquals(
            OffsetDateTime.of(2025, 4, 1, 14, 0, 0, 0, ZoneOffset.UTC),
            result.checkIn,
        )
        assertEquals(
            OffsetDateTime.of(2025, 4, 5, 12, 0, 0, 0, ZoneOffset.UTC),
            result.checkOut,
        )
        assertEquals(1, result.guests.size)
        verify(bookingDao, times(1)).update(any())
    }

    @Test
    fun `updateBooking throws exception when booking not found`() {
        whenever(bookingDao.findById(any())).thenReturn(null)

        assertThrows<NoSuchElementException> {
            bookingService.updateBooking(
                UUID.randomUUID(),
                BookingRequest(
                    checkIn = OffsetDateTime.of(2025, 4, 1, 14, 0, 0, 0, ZoneOffset.UTC),
                    checkOut = OffsetDateTime.of(2025, 4, 5, 12, 0, 0, 0, ZoneOffset.UTC),
                    guestsIds = listOf(guestEntity1.id),
                    roomId = bookingEntity.roomId,
                ),
            )
        }
    }

    @Test
    fun `deleteBooking succeeds when booking exists`() {
        whenever(bookingDao.delete(any())).thenReturn(1)

        assertDoesNotThrow {
            bookingService.deleteBooking(UUID.randomUUID())
        }
        verify(bookingDao, times(1)).delete(any())
    }

    @Test
    fun `deleteBooking throws exception when booking not found`() {
        whenever(bookingDao.delete(any())).thenReturn(0)

        assertThrows<NoSuchElementException> {
            bookingService.deleteBooking(UUID.randomUUID())
        }
    }

    @Test
    fun `findAvailableRooms returns list of available rooms`() {
        val roomEntity1 = RoomEntity(id = UUID.randomUUID(), floor = 5, roomNumber = 50, capacity = 2)
        val roomEntity2 = RoomEntity(id = UUID.randomUUID(), floor = 6, roomNumber = 60, capacity = 3)
        val checkIn = OffsetDateTime.of(2025, 3, 10, 14, 0, 0, 0, ZoneOffset.UTC)
        val checkOut = OffsetDateTime.of(2025, 3, 15, 12, 0, 0, 0, ZoneOffset.UTC)
        whenever(roomDao.findAvailableRoom(checkIn, checkOut)).thenReturn(listOf(roomEntity1, roomEntity2))

        val result = bookingService.findAvailableRooms(checkIn, checkOut)

        assertEquals(2, result.size)
        assertEquals(5, result[0].floor)
        assertEquals(6, result[1].floor)
    }

    @Test
    fun `findAvailableRooms returns empty list when no rooms available`() {
        val checkIn = OffsetDateTime.of(2025, 3, 10, 14, 0, 0, 0, ZoneOffset.UTC)
        val checkOut = OffsetDateTime.of(2025, 3, 15, 12, 0, 0, 0, ZoneOffset.UTC)
        whenever(roomDao.findAvailableRoom(checkIn, checkOut)).thenReturn(emptyList())

        val result = bookingService.findAvailableRooms(checkIn, checkOut)

        assertEquals(0, result.size)
    }
}
