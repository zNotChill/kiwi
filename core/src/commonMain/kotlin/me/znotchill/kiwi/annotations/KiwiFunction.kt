package me.znotchill.kiwi.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class KiwiFunction(val name: String = "INHERIT_FROM_DEFINITION")