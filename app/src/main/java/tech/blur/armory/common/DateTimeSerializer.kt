package tech.blur.armory.common

import com.soywiz.klock.DateFormat
import com.soywiz.klock.parseUtc
import com.soywiz.klock.wrapped.WDateTime
import com.soywiz.klock.wrapped.wrapped
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializer(forClass = DateTimeSerializer::class)
object DateTimeSerializer : KSerializer<WDateTime> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("DateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): WDateTime {
        return DateFormat(DATE_FORMAT).parseUtc(decoder.decodeString()).wrapped
    }

    override fun serialize(encoder: Encoder, value: WDateTime) {
        encoder.encodeString(value.value.format(DateFormat(DATE_FORMAT)))
    }

    private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
}