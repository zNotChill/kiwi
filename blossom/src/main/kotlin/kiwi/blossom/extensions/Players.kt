package me.znotchill.kiwi.blossom.extensions

import net.kyori.adventure.audience.Audience
import net.minestom.server.entity.Player
import org.jetbrains.annotations.NotNull

val Set<@NotNull Player>.audience: Audience
    get() {
        return Audience.audience(this)
    }