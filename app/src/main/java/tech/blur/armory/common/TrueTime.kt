package tech.blur.armory.common

import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.TimezoneOffset
import io.tempo.Tempo

class TrueTime {
     companion object {
         fun unixTime() = Tempo.nowOrNull() ?: DateTime.nowUnixLong()

         fun nowLocal(timezoneOffset: Double): DateTimeTz {
             return DateTime(unixTime()).toOffset(TimezoneOffset(timezoneOffset))
         }

         fun nowLocal(): DateTimeTz {
             return DateTime(unixTime()).toOffset(DateTime.nowLocal().offset)
         }

         fun nowUtc(): DateTime {
             return DateTime(unixTime())
         }
    }
}