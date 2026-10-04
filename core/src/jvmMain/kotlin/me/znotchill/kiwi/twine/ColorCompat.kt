package me.znotchill.kiwi.twine

import computer.obscure.twine.resolvers.TwineBindingResolver
import me.znotchill.kiwi.generated.Color
import me.znotchill.kiwi.generated.toShadowColor
import me.znotchill.kiwi.generated.toTextColor
import kotlin.reflect.KClass

object AdventureColorResolver : TwineBindingResolver {
    override fun supports(type: KClass<*>) =
        type == Color::class

    override fun functions(instance: Any) =
        listOf(
            "toTextColor" to Color::toTextColor,
            "toShadowColor" to Color::toShadowColor
        )
}