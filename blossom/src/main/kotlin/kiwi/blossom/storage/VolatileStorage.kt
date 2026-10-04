package me.znotchill.kiwi.blossom.storage

import me.znotchill.kiwi.storage.Storage
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerDisconnectEvent

class VolatileStorage<V> : Storage<Player, V>() {
    init {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerDisconnectEvent::class.java) { event ->
            remove(event.player)
        }
    }
}