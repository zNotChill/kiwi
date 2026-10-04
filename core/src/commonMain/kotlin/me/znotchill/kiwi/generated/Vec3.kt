package me.znotchill.kiwi.generated

import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("vec3")
data class Vec3(
    @TwineProperty
    var x: Double = 0.0,
    @TwineProperty
    var y: Double = 0.0,
    @TwineProperty
    var z: Double = 0.0,
) : TwineNative("vec3") {
    constructor(x: Number, y: Number, z: Number) : this(
        x.toDouble(),
        y.toDouble(),
        z.toDouble()
    )

    @TwineFunction
    fun add(other: Vec3): Vec3 = Vec3(x + other.x, y + other.y, z + other.z)
    operator fun plus(other: Vec3): Vec3 = add(other)

    @TwineFunction
    fun add(x: Number, y: Number, z: Number): Vec3 =
        Vec3(this.x + x.toDouble(), this.y + y.toDouble(), this.z + z.toDouble())

    @TwineFunction
    fun sub(other: Vec3): Vec3 = Vec3(x - other.x, y - other.y, z - other.z)
    operator fun minus(other: Vec3): Vec3 = sub(other)

    @TwineFunction
    fun sub(x: Number, y: Number, z: Number): Vec3 =
        Vec3(this.x - x.toDouble(), this.y - y.toDouble(), this.z - z.toDouble())

    @TwineFunction
    fun mul(other: Vec3): Vec3 = Vec3(x * other.x, y * other.y, z * other.z)
    operator fun times(other: Vec3): Vec3 = mul(other)

    @TwineFunction
    fun mul(x: Number, y: Number, z: Number): Vec3 =
        Vec3(this.x * x.toDouble(), this.y * y.toDouble(), this.z * z.toDouble())

    @TwineFunction
    fun divide(other: Vec3): Vec3 = Vec3(x / other.x, y / other.y, z / other.z)
    operator fun div(other: Vec3): Vec3 = divide(other)

    @TwineFunction
    fun divide(x: Number, y: Number, z: Number): Vec3 =
        Vec3(this.x / x.toDouble(), this.y / y.toDouble(), this.z / z.toDouble())

    @TwineFunction
    fun mod(other: Vec3): Vec3 = Vec3(x % other.x, y % other.y, z % other.z)
    operator fun rem(other: Vec3): Vec3 = mod(other)

    @TwineFunction
    fun mod(x: Number, y: Number, z: Number): Vec3 =
        Vec3(this.x % x.toDouble(), this.y % y.toDouble(), this.z % z.toDouble())

    @TwineFunction("tostring")
    override fun toString(): String {
        return "vec3[x=$x, y=$y, z=$z]"
    }

    companion object {
        val ZERO: Vec3
            get() = Vec3(0.0, 0.0, 0.0)

        val ONE: Vec3
            get() = Vec3(1.0, 1.0, 1.0)
    }
}