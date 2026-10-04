package me.znotchill.kiwi.blossom.server.essentials.classes

import me.znotchill.kiwi.blossom.server.BlossomServer

interface Essential<C : EssentialConfig> {
    val config: C

    fun load(server: BlossomServer)
}

interface EssentialConfig