package me.znotchill.kiwi.generated

import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.max
import kotlin.math.pow
import kotlin.random.Random

@Serializable
@SerialName("color")
data class Color(
    val argb: Int
) : TwineNative("color") {
    @TwineProperty
    val r: Int
        get() = (argb shr 16) and 0xFF

    @TwineProperty
    val g: Int
        get() = (argb shr 8) and 0xFF

    @TwineProperty
    val b: Int
        get() = argb and 0xFF

    @TwineProperty
    val a: Int
        get() = (argb ushr 24) and 0xFF

    @TwineProperty
    val hue: Float
        get() = hsvArray()[0] * 360f

    @TwineProperty
    val saturation: Float
        get() = hsvArray()[1] * 100f

    @TwineProperty
    val brightness: Float
        get() = hsvArray()[2] * 100f

    operator fun plus(other: Color): Color {
        return rgba(
            (r + other.r).coerceIn(0, 255),
            (g + other.g).coerceIn(0, 255),
            (b + other.b).coerceIn(0, 255),
            (a + other.a).coerceIn(0, 255)
        )
    }

    operator fun minus(other: Color): Color {
        return rgba(
            (r - other.r).coerceIn(0, 255),
            (g - other.g).coerceIn(0, 255),
            (b - other.b).coerceIn(0, 255),
            (a - other.a).coerceIn(0, 255)
        )
    }

    operator fun times(value: Float): Color {
        return rgba(
            (r * value).toInt().coerceIn(0, 255),
            (g * value).toInt().coerceIn(0, 255),
            (b * value).toInt().coerceIn(0, 255),
            a
        )
    }

    @TwineFunction
    fun withRed(value: Int): Color =
        rgba(value, g, b, a)

    @TwineFunction
    fun withGreen(value: Int): Color =
        rgba(r, value, b, a)

    @TwineFunction
    fun withBlue(value: Int): Color =
        rgba(r, g, value, a)

    @TwineFunction
    fun withAlpha(value: Int): Color =
        rgba(r, g, b, value)

    @TwineFunction
    fun alpha(value: Int): Color =
        withAlpha(value)

    @TwineFunction
    fun withOpacity(opacity: Float): Color {
        val newAlpha = (a * opacity.coerceIn(0f, 1f)).toInt()
        return withAlpha(newAlpha)
    }

    @TwineFunction
    fun darken(amount: Float): Color =
        lighten(-amount)

    @TwineFunction
    fun lighten(amount: Float): Color {
        val hsv = hsvArray()

        val brightness =
            (hsv[2] + amount)
                .coerceIn(0f, 1f)

        return hsv(
            hsv[0] * 360f,
            hsv[1] * 100f,
            brightness * 100f
        ).withAlpha(a)
    }

    @TwineFunction
    fun saturate(amount: Float): Color {
        val hsv = hsvArray()

        val saturation =
            (hsv[1] + amount)
                .coerceIn(0f, 1f)

        return hsv(
            hsv[0] * 360f,
            saturation * 100f,
            hsv[2] * 100f
        ).withAlpha(a)
    }

    @TwineFunction
    fun desaturate(amount: Float): Color =
        saturate(-amount)

    @TwineFunction
    fun rotateHue(amount: Float): Color {
        val hsv = hsvArray()

        var hue = (hsv[0] * 360f + amount) % 360f

        if (hue < 0f) {
            hue += 360f
        }

        return hsv(
            hue,
            hsv[1] * 100f,
            hsv[2] * 100f
        ).withAlpha(a)
    }

    @TwineFunction
    fun invert(): Color {
        return rgba(
            255 - r,
            255 - g,
            255 - b,
            a
        )
    }

    @TwineFunction
    fun grayscale(): Color {
        val gray = (
                0.299f * r +
                        0.587f * g +
                        0.114f * b
                ).toInt()

        return rgba(gray, gray, gray, a)
    }

    @TwineFunction
    fun lerp(a: Int, b: Int, t: Float): Int {
        return (a + ((b - a) * t))
            .toInt()
            .coerceIn(0, 255)
    }

    @TwineFunction
    fun lerp(other: Color, t: Float): Color {
        return rgba(
            lerp(r, other.r, t),
            lerp(g, other.g, t),
            lerp(b, other.b, t),
            lerp(a, other.a, t)
        )
    }

    @TwineFunction
    fun mix(other: Color, t: Float): Color =
        lerp(other, t)

    @TwineFunction
    fun lerp(other: Color, t: Double): Color {
        return lerp(other, t.toFloat())
    }

    @TwineFunction
    fun multiply(other: Color): Color {
        return rgba(
            r * other.r / 255,
            g * other.g / 255,
            b * other.b / 255,
            a * other.a / 255
        )
    }

    @TwineFunction
    fun screen(other: Color): Color {
        return rgba(
            255 - ((255 - r) * (255 - other.r) / 255),
            255 - ((255 - g) * (255 - other.g) / 255),
            255 - ((255 - b) * (255 - other.b) / 255),
            a
        )
    }
    @TwineFunction
    fun overlay(a: Int, b: Int): Int {
        return if (a < 128) {
            (2 * a * b / 255)
        } else {
            255 - (2 * (255 - a) * (255 - b) / 255)
        }
    }

    @TwineFunction
    fun overlay(other: Color): Color {
        return rgba(
            overlay(r, other.r),
            overlay(g, other.g),
            overlay(b, other.b),
            a
        )
    }

    @TwineFunction
    fun luminance(): Float {

        fun channel(c: Int): Float {
            val v = c / 255f

            return if (v <= 0.03928f) {
                v / 12.92f
            } else {
                ((v + 0.055f) / 1.055f)
                    .toDouble()
                    .pow(2.4)
                    .toFloat()
            }
        }

        val rr = channel(r)
        val gg = channel(g)
        val bb = channel(b)

        return (
                0.2126f * rr +
                        0.7152f * gg +
                        0.0722f * bb
                )
    }

    @TwineFunction
    fun brightnessValue(): Float {
        return max(
            r / 255f,
            max(g / 255f, b / 255f)
        )
    }

    @TwineFunction
    fun isDark(): Boolean =
        luminance() < 0.5f

    @TwineFunction
    fun isLight(): Boolean =
        !isDark()

    @TwineFunction
    fun complementary(): Color =
        rotateHue(180f)

    @TwineFunction
    fun copy(): Color =
        Color(argb)

    private fun Int.hex2() = toString(16).padStart(2, '0')

    @TwineFunction
    fun hex(): String =
        "#${a.hex2()}${r.hex2()}${g.hex2()}${b.hex2()}".uppercase()

    @TwineFunction
    fun mmHex(): String =
        "<#${r.hex2()}${g.hex2()}${b.hex2()}>".uppercase()

    @TwineFunction("tostring")
    override fun toString(): String {
        return buildString {
            append("color[")
            append("r=$r")
            append(", g=$g")
            append(", b=$b")
            append(", a=$a")
            append(", hex=${hex()}")
            append("]")
        }
    }

    private fun hsvArray(): FloatArray =
        hsvArray(r, g, b)

    companion object {
        val WHITE = rgb(255, 255, 255)
        val BLACK = rgb(0, 0, 0)

        val RED = rgb(255, 0, 0)
        val GREEN = rgb(0, 255, 0)
        val BLUE = rgb(0, 0, 255)

        fun rgb(r: Int, g: Int, b: Int): Color {
            return rgba(r, g, b, 255)
        }

        fun rgba(r: Int, g: Int, b: Int, a: Int): Color {
            return Color(
                ((a and 0xFF) shl 24) or
                        ((r and 0xFF) shl 16) or
                        ((g and 0xFF) shl 8) or
                        (b and 0xFF)
            )
        }

        fun int(value: Int): Color {
            return Color(value)
        }

        fun hex(value: String): Color {
            val clean = value.removePrefix("#")

            val rgba = when (clean.length) {
                6 -> (0xFF shl 24) or clean.toLong(16).toInt()
                8 -> clean.toLong(16).toInt()
                else -> error("Invalid hex color: $value")
            }

            return Color(rgba)
        }

        fun hsv(h: Float, s: Float, v: Float): Color {
            val hh = ((h % 360f) + 360f) % 360f
            val ss = (s / 100f).coerceIn(0f, 1f)
            val vv = (v / 100f).coerceIn(0f, 1f)

            val c = vv * ss
            val x = c * (1f - kotlin.math.abs((hh / 60f) % 2f - 1f))
            val m = vv - c

            val (r1, g1, b1) = when {
                hh < 60f  -> Triple(c, x, 0f)
                hh < 120f -> Triple(x, c, 0f)
                hh < 180f -> Triple(0f, c, x)
                hh < 240f -> Triple(0f, x, c)
                hh < 300f -> Triple(x, 0f, c)
                else -> Triple(c, 0f, x)
            }

            return rgb(
                ((r1 + m) * 255f).toInt(),
                ((g1 + m) * 255f).toInt(),
                ((b1 + m) * 255f).toInt()
            )
        }

        fun hsl(h: Float, s: Float, l: Float): Color {
            return Color(hslToRgb(h, s, l))
        }

        fun random(): Color {
            return rgb(
                Random.nextInt(256),
                Random.nextInt(256),
                Random.nextInt(256)
            )
        }

        fun rainbow(time: Float): Color {
            return hsv(
                (time * 360f) % 360f,
                100f,
                100f
            )
        }

        private fun hslToRgb(h: Float, s: Float, l: Float): Int {
            val hh = ((h % 360f) + 360f) % 360f / 360f
            val ss = (s / 100f).coerceIn(0f, 1f)
            val ll = (l / 100f).coerceIn(0f, 1f)

            if (ss <= 0f) {
                val gray = (ll * 255f).toInt()

                return (0xFF shl 24) or
                        (gray shl 16) or
                        (gray shl 8) or
                        gray
            }

            val q = if (ll < 0.5f)
                ll * (1f + ss)
            else
                ll + ss - ll * ss

            val p = 2f * ll - q

            fun hueToRgb(t: Float): Float {
                var tt = t

                if (tt < 0f) tt += 1f
                if (tt > 1f) tt -= 1f

                return when {
                    tt < 1f / 6f -> p + (q - p) * 6f * tt
                    tt < 1f / 2f -> q
                    tt < 2f / 3f -> p + (q - p) * (2f / 3f - tt) * 6f
                    else -> p
                }
            }

            val r = (hueToRgb(hh + 1f / 3f) * 255f).toInt()
            val g = (hueToRgb(hh) * 255f).toInt()
            val b = (hueToRgb(hh - 1f / 3f) * 255f).toInt()

            return (0xFF shl 24) or
                    (r shl 16) or
                    (g shl 8) or
                    b
        }

        private fun hsvArray(r: Int, g: Int, b: Int): FloatArray {
            val rf = r / 255f
            val gf = g / 255f
            val bf = b / 255f

            val max = maxOf(rf, gf, bf)
            val min = minOf(rf, gf, bf)
            val delta = max - min

            val h = when {
                delta == 0f -> 0f
                max == rf -> ((gf - bf) / delta).mod(6f)
                max == gf -> ((bf - rf) / delta) + 2f
                else -> ((rf - gf) / delta) + 4f
            } / 6f

            val s = if (max == 0f) 0f else delta / max
            val v = max

            return floatArrayOf(
                if (h < 0) h + 1f else h,
                s,
                v
            )
        }
    }
}