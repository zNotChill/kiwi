package me.znotchill.kiwi.generated.builders

import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.znotchill.kiwi.generated.Vec3

@Serializable
@SerialName("vec3")
class Vec3Builder : TwineNative("vec3") {
    @TwineFunction
    fun new(x: Number, y: Number, z: Number) = Vec3(x, y, z)
}