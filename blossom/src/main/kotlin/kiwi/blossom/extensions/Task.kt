package me.znotchill.kiwi.blossom.extensions

import net.minestom.server.timer.Task

fun Task.Builder.repeat(time: Time): Task.Builder {
    return this.repeat(time.duration)
}