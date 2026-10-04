package me.znotchill.kiwi.network

import io.ktor.network.sockets.InetSocketAddress

data class Address(
    val host: String,
    val port: Int
) {
    override fun toString(): String {
        return "$host:$port"
    }
}

fun String.toAddress(): Address {
    return NetworkUtils.parseAddress(this)
}

fun InetSocketAddress.toAddress(): Address {
    return Address(
        this.hostname,
        this.port
    )
}