package edu.booking.hotelbooking.controller

import edu.booking.hotelbooking.dto.request.BookingRequest
import edu.booking.hotelbooking.dto.response.BookingResponse
import edu.booking.hotelbooking.dto.response.RoomResponse
import edu.booking.hotelbooking.service.BookingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime
import java.util.UUID

@RestController
@RequestMapping("/bookings")
class BookingController(private val bookingService: BookingService) {
    @GetMapping("/{id}")
    fun getBooking(
        @PathVariable id: UUID,
    ): ResponseEntity<BookingResponse> {
        val booking = bookingService.findBookingById(id)
        if (booking != null) return ResponseEntity.ok(booking)
        return ResponseEntity.notFound().build()
    }

    @PostMapping
    fun postBooking(
        @RequestBody request: BookingRequest,
    ): ResponseEntity<BookingResponse> {
        val booking = bookingService.createBooking(request)
        return ResponseEntity.status(201).body(booking)
    }

    @PutMapping("/{id}")
    fun updateBooking(
        @PathVariable id: UUID,
        @RequestBody request: BookingRequest,
    ): ResponseEntity<BookingResponse> {
        val booking = bookingService.updateBooking(id, request)
        return ResponseEntity.ok(booking)
    }

    @DeleteMapping("/{id}")
    fun deleteBooking(
        @PathVariable id: UUID,
    ): ResponseEntity<Void> {
        bookingService.deleteBooking(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/available")
    fun findAvailableRooms(
        @RequestParam checkIn: OffsetDateTime,
        @RequestParam checkOut: OffsetDateTime,
    ): ResponseEntity<List<RoomResponse>> {
        val rooms = bookingService.findAvailableRooms(checkIn, checkOut)
        return ResponseEntity.ok(rooms)
    }
}
