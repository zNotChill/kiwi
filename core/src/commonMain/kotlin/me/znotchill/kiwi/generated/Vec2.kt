package me.znotchill.kiwi.generated

import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.Double

@Serializable
@SerialName("vec2")
data class Vec2(
    @TwineProperty
    var x: Double = 0.0,
    @TwineProperty
    var y: Double = 0.0,
) : TwineNative("vec2") {
    constructor(x: Number, y: Number): this(x.toDouble(), y.toDouble())

    @TwineFunction
    fun add(other: Vec2): Vec2 = Vec2(x + other.x, y + other.y)
    operator fun plus(other: Vec2): Vec2 = add(other)
    @TwineFunction
    fun add(x: Number, y: Number): Vec2 = Vec2(this.x + x.toDouble(), this.y + y.toDouble())

    @TwineFunction
    fun sub(other: Vec2): Vec2 = Vec2(x - other.x, y - other.y)
    operator fun minus(other: Vec2): Vec2 = sub(other)
    @TwineFunction
    fun sub(x: Number, y: Number): Vec2 = Vec2(this.x - x.toDouble(), this.y - y.toDouble())

    @TwineFunction
    fun mul(other: Vec2): Vec2 = Vec2(x * other.x, y * other.y)
    operator fun times(other: Vec2): Vec2 = mul(other)
    @TwineFunction
    fun mul(x: Number, y: Number): Vec2 = Vec2(this.x * x.toDouble(), this.y * y.toDouble())

    @TwineFunction
    fun divide(other: Vec2): Vec2 = Vec2(x / other.x, y / other.y)
    operator fun div(other: Vec2): Vec2 = divide(other)
    @TwineFunction
    fun divide(x: Number, y: Number): Vec2 = Vec2(this.x / x.toDouble(), this.y / y.toDouble())

    @TwineFunction
    fun mod(other: Vec2): Vec2 = Vec2(x % other.x, y % other.y)
    operator fun rem(other: Vec2): Vec2 = mod(other)
    @TwineFunction
    fun mod(x: Number, y: Number): Vec2 = Vec2(this.x % x.toDouble(), this.y % y.toDouble())

    fun lineTo(to: Vec2): List<Vec2> {
        val points = mutableListOf<Vec2>()

        var x1 = x.toInt()
        var y1 = y.toInt()
        val x2 = to.x.toInt()
        val y2 = to.y.toInt()

        // absolute distance between from and to
        val dx = kotlin.math.abs(x2 - x1)
        val dy = kotlin.math.abs(y2 - y1)

        // step direction
        val sx = if (x1 < x2) 1 else -1
        val sy = if (y1 < y2) 1 else -1
        var err = dx - dy

        // todo: find out if this is dangerous
        while (true) {
            points.add(Vec2(x1.toDouble(), y1.toDouble()))
            if (x1 == x2 && y1 == y2) break
            val e2 = 2 * err
            if (e2 > -dy) {
                err -= dy
                x1 += sx
            }
            if (e2 < dx) {
                err += dx
                y1 += sy
            }
        }

        return points
    }

    @TwineFunction("tostring")
    override fun toString(): String {
        return "vec2[x=$x, y=$y]"
    }

    companion object {
        val ZERO: Vec2
            get() = Vec2(0.0, 0.0)

        val ONE: Vec2
            get() = Vec2(1.0, 1.0)
    }
}
