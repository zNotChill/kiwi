package me.znotchill.kiwi.generated.builders

import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.znotchill.kiwi.generated.Color

@Serializable
@SerialName("color")
class ColorBuilder : TwineNative("color") {
    @TwineFunction
    fun rgb(r: Int, g: Int, b: Int): Color =
        Color.rgb(r, g, b)

    @TwineFunction
    fun rgba(r: Int, g: Int, b: Int, a: Int): Color =
        Color.rgba(r, g, b, a)

    @TwineFunction
    fun int(value: Int): Color =
        Color(value)

    @TwineFunction
    fun hex(value: String): Color =
        Color.hex(value)

    @TwineFunction
    fun hsv(h: Float, s: Float, v: Float): Color =
        Color.hsv(h, s, v)

    @TwineFunction
    fun hsl(h: Float, s: Float, l: Float): Color =
        Color.hsl(h, s, l)

    @TwineFunction
    fun rainbow(time: Float): Color =
        Color.rainbow(time)

    @TwineFunction
    fun random(): Color =
        Color.random()

    @TwineProperty
    val white: Color = Color.WHITE

    @TwineProperty
    val black: Color = Color.BLACK

    @TwineProperty
    val red: Color = Color.RED

    @TwineProperty
    val green: Color = Color.GREEN

    @TwineProperty
    val blue: Color = Color.BLUE
}