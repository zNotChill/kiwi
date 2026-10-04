package me.znotchill.kiwi.annotations

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class KiwiProperty(val name: String = "INHERIT_FROM_DEFINITION")