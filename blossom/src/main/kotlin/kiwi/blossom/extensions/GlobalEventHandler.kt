package me.znotchill.kiwi.blossom.extensions

import me.znotchill.kiwi.blossom.server.BlossomServer
import net.minestom.server.event.Event
import net.minestom.server.event.GlobalEventHandler

inline fun <reified T : Event> GlobalEventHandler.addListener(
    noinline callback: (T) -> Unit
) {
    this.addListener(T::class.java) { event ->
        callback(event)
    }
}