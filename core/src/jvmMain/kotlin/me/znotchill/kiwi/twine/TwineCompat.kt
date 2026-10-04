package me.znotchill.kiwi.twine

import computer.obscure.twine.TwineEngine

object TwineCompat {
    fun bind(engine: TwineEngine) {
        engine.addResolver(KiwiAnnotationResolver)
        engine.addResolver(AdventureColorResolver)
    }
}