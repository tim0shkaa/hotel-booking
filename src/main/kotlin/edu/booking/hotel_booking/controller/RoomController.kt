package edu.booking.hotel_booking.controller

import edu.booking.hotel_booking.dto.request.RoomRequest
import edu.booking.hotel_booking.dto.response.RoomResponse
import edu.booking.hotel_booking.service.RoomService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/rooms")
class RoomController (
    private val roomService: RoomService,
) {

    @GetMapping("/{id}")
    fun getRoom(@PathVariable id: UUID): ResponseEntity<RoomResponse> {
        val room = roomService.findRoomById(id)
        if (room != null) return ResponseEntity.ok(room)
        return ResponseEntity.notFound().build()
    }

    @PostMapping
    fun addRoom(@RequestBody request: RoomRequest): ResponseEntity<RoomResponse> {
        val room = roomService.createRoom(request)
        return ResponseEntity.status(201).body(room)
    }

    @PutMapping("/{id}")
    fun updateRoom(
        @PathVariable id: UUID,
        @RequestBody request: RoomRequest,
    ): ResponseEntity<RoomResponse> {
        val room = roomService.updateRoom(id, request)
        return ResponseEntity.ok(room)
    }

    @DeleteMapping("/{id}")
    fun deleteRoom(@PathVariable id: UUID): ResponseEntity<Void> {
        roomService.deleteRoom(id)
        return ResponseEntity.noContent().build()
    }
}