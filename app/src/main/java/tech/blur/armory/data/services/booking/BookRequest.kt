@file:UseSerializers(DateTimeTzSerializer::class)

package tech.blur.armory.data.services.booking

import com.soywiz.klock.DateTimeTz
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import tech.blur.armory.common.DateTimeTzSerializer

@Serializable
data class BookRequest(
    val name: String,
    val startTime: DateTimeTz,
    val endTime: DateTimeTz,
    val meetingRoomID: Int,
    val userEmailList: List<String>
)