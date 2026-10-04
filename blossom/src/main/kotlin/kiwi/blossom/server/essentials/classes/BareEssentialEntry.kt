package me.znotchill.kiwi.blossom.server.essentials.classes

sealed class BareEssentialEntry {
    data class Type(val type: BareEssential) : BareEssentialEntry()
    data class Configured(val type: BareEssential, val config: EssentialConfig) : BareEssentialEntry()
}