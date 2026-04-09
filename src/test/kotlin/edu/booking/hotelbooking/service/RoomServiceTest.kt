package edu.booking.hotelbooking.service

import edu.booking.hotelbooking.dao.RoomDao
import edu.booking.hotelbooking.dto.request.RoomRequest
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
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class RoomServiceTest {
    @Mock
    lateinit var roomDao: RoomDao

    @InjectMocks
    lateinit var roomService: RoomService

    @Test
    fun `findRoomById returns response when room exists`() {
        val id = UUID.randomUUID()
        val entity = RoomEntity(id = id, floor = 5, roomNumber = 50, capacity = 2)
        whenever(roomDao.findById(id)).thenReturn(entity)

        val result = roomService.findRoomById(id)

        assertNotNull(result)
        assertEquals(5, result.floor)
        assertEquals(50, result.roomNumber)
        assertEquals(2, result.capacity)
    }

    @Test
    fun `findRoomById returns null when room not exists`() {
        whenever(roomDao.findById(any())).thenReturn(null)

        val result = roomService.findRoomById(UUID.randomUUID())

        assertNull(result)
    }

    @Test
    fun `createRoom creates room and returns correct response`() {
        val request = RoomRequest(floor = 5, roomNumber = 50, capacity = 2)

        val result = roomService.createRoom(request)

        assertNotNull(result)
        assertEquals(5, result.floor)
        assertEquals(50, result.roomNumber)
        assertEquals(2, result.capacity)
        verify(roomDao, times(1)).create(any())
    }

    @Test
    fun `updateRoom updates room and returns correct response`() {
        val id = UUID.randomUUID()
        val existing = RoomEntity(id = id, floor = 5, roomNumber = 50, capacity = 2)
        val request = RoomRequest(floor = 6, roomNumber = 60, capacity = 3)
        whenever(roomDao.findById(id)).thenReturn(existing)

        val result = roomService.updateRoom(id, request)

        assertNotNull(result)
        assertEquals(6, result.floor)
        assertEquals(60, result.roomNumber)
        assertEquals(3, result.capacity)
        verify(roomDao, times(1)).update(any())
    }

    @Test
    fun `updateRoom throws exception when room not found`() {
        whenever(roomDao.findById(any())).thenReturn(null)

        val request = RoomRequest(floor = 6, roomNumber = 60, capacity = 3)

        assertThrows<NoSuchElementException> {
            roomService.updateRoom(UUID.randomUUID(), request)
        }
    }

    @Test
    fun `deleteRoom deletes room`() {
        whenever(roomDao.delete(any())).thenReturn(1)

        assertDoesNotThrow {
            roomService.deleteRoom(UUID.randomUUID())
        }
        verify(roomDao, times(1)).delete(any())
    }

    @Test
    fun `deleteRoom throws exception when room not found`() {
        whenever(roomDao.delete(any())).thenReturn(0)

        assertThrows<NoSuchElementException> {
            roomService.deleteRoom(UUID.randomUUID())
        }
    }
}
