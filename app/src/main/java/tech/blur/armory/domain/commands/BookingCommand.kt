package tech.blur.armory.domain.commands

import tech.blur.armory.data.services.booking.BookingService

class BookingCommand(private val bookingService: BookingService) {
    suspend fun getMineEvents() = bookingService.getMineEvents()
}