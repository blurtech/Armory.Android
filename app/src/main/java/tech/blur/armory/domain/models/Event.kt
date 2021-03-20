package tech.blur.armory.domain.models

import com.soywiz.klock.DateTime
import java.io.Serializable

data class Event(
    val id: Int,
    val startTime: DateTime,
    val endTime: DateTime
): Serializable