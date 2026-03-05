package edu.booking.hotelbooking.kafka

import edu.booking.hotelbooking.kafka.event.BookingEvent
import edu.booking.hotelbooking.kafka.event.GuestEvent
import edu.booking.hotelbooking.kafka.event.RoomEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducer (private val kafkaTemplate: KafkaTemplate<String, Any>) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun sendGuestEvent(event: GuestEvent) {
        kafkaTemplate.send(KafkaTopics.GUEST, event.data.id.toString(), event)
            .whenComplete { _, exception ->
                if (exception != null) {
                    logger.error("Error send guestEvent: ${event.eventType}, id=${event.data.id}", exception)
                } else {
                    logger.info("Success send guestEvent: ${event.eventType}, id=${event.data.id}")
                }
            }
    }

    fun sendRoomEvent(event: RoomEvent) {
        kafkaTemplate.send(KafkaTopics.ROOM, event.data.id.toString(), event)
            .whenComplete { _, exception ->
                if (exception != null) {
                    logger.error("Error send roomEvent: ${event.eventType}, id=${event.data.id}", exception)
                } else {
                    logger.info("Success send roomEvent: ${event.eventType}, id=${event.data.id}")
                }
            }
    }

    fun sendBookingEvent(event: BookingEvent) {
        kafkaTemplate.send(KafkaTopics.BOOKING, event.data.id.toString(), event)
            .whenComplete { _, exception ->
                if (exception != null) {
                    logger.error("Error send bookingEvent: ${event.eventType}, id=${event.data.id}", exception)
                } else {
                    logger.info("Success send bookingEvent: ${event.eventType}, id=${event.data.id}")
                }
            }
    }
}