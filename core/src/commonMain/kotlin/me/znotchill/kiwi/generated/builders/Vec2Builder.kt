package me.znotchill.kiwi.generated.builders

import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.znotchill.kiwi.generated.Vec2

@Serializable
@SerialName("vec2")
class Vec2Builder : TwineNative("vec2") {
    @TwineFunction
    fun new(x: Number, y: Number) = Vec2(x, y)
}