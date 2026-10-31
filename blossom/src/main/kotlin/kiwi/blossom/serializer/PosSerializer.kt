package me.znotchill.kiwi.blossom.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minestom.server.coordinate.Pos

object PosSerializer : KSerializer<Pos> {
    @Serializable
    @SerialName("Pos")
    private class PosSurrogate(
        val x: Double,
        val y: Double,
        val z: Double,
        val yaw: Float = 0f,
        val pitch: Float = 0f,
    )
 
    override val descriptor: SerialDescriptor = PosSurrogate.serializer().descriptor
 
    override fun serialize(encoder: Encoder, value: Pos) {
        encoder.encodeSerializableValue(
            PosSurrogate.serializer(),
            PosSurrogate(value.x(), value.y(), value.z(), value.yaw(), value.pitch())
        )
    }
 
    override fun deserialize(decoder: Decoder): Pos {
        val s = decoder.decodeSerializableValue(PosSurrogate.serializer())
        return Pos(s.x, s.y, s.z, s.yaw, s.pitch)
    }
}
 