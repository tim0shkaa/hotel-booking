package edu.booking.hotelbooking.metrics

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component

@Component
class HotelBookingMetrics(registry: MeterRegistry) {
    val guestsCreated: Counter = Counter.builder("guests.created")
        .description("Number of created guests")
        .register(registry)

    val bookingsCreated: Counter = Counter.builder("bookings.created")
        .description("Number of created bookings")
        .register(registry)

    val notFoundErrors: Counter = Counter.builder("errors.not.found")
        .description("Number of 404 errors")
        .register(registry)
}