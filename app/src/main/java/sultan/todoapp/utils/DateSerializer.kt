package sultan.todoapp.utils

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Date::class)
class DateSerializer : KSerializer<Date> {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(formatter.format(value.toInstant()))
    }

    override fun deserialize(decoder: Decoder): Date {
        val instant = Instant.from(formatter.parse(decoder.decodeString()))
        return Date.from(instant)
    }

}
