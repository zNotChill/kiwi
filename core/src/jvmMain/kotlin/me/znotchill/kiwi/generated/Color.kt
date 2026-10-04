package me.znotchill.kiwi.generated

import me.znotchill.kiwi.annotations.KiwiFunction
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor
fun Color.toShadowColor() = ShadowColor.shadowColor(r, g, b, a)
@KiwiFunction
fun Color.toTextColor() = TextColor.color(r, g, b)