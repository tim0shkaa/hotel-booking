package edu.booking.hotelbooking.exception

import edu.booking.hotelbooking.metrics.HotelBookingMetrics
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler(private val hotelBookingMetrics: HotelBookingMetrics) {
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(exception: NoSuchElementException): ResponseEntity<String> {
        hotelBookingMetrics.notFoundErrors.increment()
        return ResponseEntity.status(404).body(exception.message)
    }
}
