package cz.danielkouba.ktorStarterpackDk.lib.serializers

import ch.qos.logback.classic.Level
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object LogLevelSerializer : KSerializer<Level> {

    override val descriptor = PrimitiveSerialDescriptor("Level", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Level) { value.toString() }

    override fun deserialize(decoder: Decoder) = decoder.decodeString().let { Level.toLevel(it) }
}
