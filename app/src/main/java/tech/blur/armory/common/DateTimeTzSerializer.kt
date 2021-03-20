package tech.blur.armory.common

import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.parse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializer(forClass = DateTimeTz::class)
object DateTimeTzSerializer : KSerializer<DateTimeTz> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("DateTimeTz", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): DateTimeTz {
        val dateFormat = DateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        return dateFormat.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: DateTimeTz) {
        encoder.encodeString(value.format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))
    }
}