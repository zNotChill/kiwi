package me.znotchill.kiwi.twine

import computer.obscure.twine.resolvers.TwineBindingResolver
import me.znotchill.kiwi.annotations.KiwiFunction
import me.znotchill.kiwi.annotations.KiwiProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties

object KiwiAnnotationResolver : TwineBindingResolver {
    override fun supports(type: KClass<*>) = true

    override fun functions(instance: Any) =
        instance::class.functions
            .mapNotNull { fn ->
                fn.findAnnotation<KiwiFunction>()
                    ?.let { it.name to fn }
            }

    override fun properties(instance: Any) =
        instance::class.memberProperties
            .mapNotNull { prop ->
                prop.findAnnotation<KiwiProperty>()
                    ?.let {
                        @Suppress("UNCHECKED_CAST")
                        it.name to (prop as KProperty1<Any, *>)
                    }
            }
}