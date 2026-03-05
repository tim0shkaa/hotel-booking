package edu.booking.hotelbooking.controller

import edu.booking.hotelbooking.dto.request.GuestRequest
import edu.booking.hotelbooking.dto.response.GuestResponse
import edu.booking.hotelbooking.service.GuestService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/guests")
class GuestController(private val guestService: GuestService) {
    @GetMapping("/{id}")
    fun getGuest(
        @PathVariable id: UUID,
    ): ResponseEntity<GuestResponse> {
        val guest = guestService.findGuestById(id)
        if (guest != null) return ResponseEntity.ok(guest)
        return ResponseEntity.notFound().build()
    }

    @PostMapping
    fun addGuest(
        @RequestBody request: GuestRequest,
    ): ResponseEntity<GuestResponse> {
        val guest = guestService.createGuest(request)
        return ResponseEntity.status(201).body(guest)
    }

    @PutMapping("/{id}")
    fun updateGuest(
        @PathVariable id: UUID,
        @RequestBody request: GuestRequest,
    ): ResponseEntity<GuestResponse> {
        val guest = guestService.updateGuest(id, request)
        return ResponseEntity.ok(guest)
    }
}
