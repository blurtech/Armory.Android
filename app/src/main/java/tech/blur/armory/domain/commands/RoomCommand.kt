package tech.blur.armory.domain.commands

import tech.blur.armory.data.services.room.RoomService

class RoomCommand(private val roomService: RoomService) {
    suspend fun getAllRooms() = roomService.getAllRooms()
}