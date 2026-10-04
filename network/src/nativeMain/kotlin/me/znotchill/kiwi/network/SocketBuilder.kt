package me.znotchill.kiwi.network

import io.ktor.network.sockets.BoundDatagramSocket
import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.ServerSocket
import io.ktor.network.sockets.SocketOptions
import io.ktor.network.sockets.TcpSocketBuilder
import io.ktor.network.sockets.UDPSocketBuilder

suspend fun TcpSocketBuilder.bind(
    socketAddress: Address,
    configure: SocketOptions.AcceptorOptions.() -> Unit = {}
): ServerSocket {
    return bind(
        InetSocketAddress(
            socketAddress.host,
            socketAddress.port
        ),
        configure
    )
}

suspend fun TcpSocketBuilder.connect(
    socketAddress: Address,
    configure: SocketOptions.AcceptorOptions.() -> Unit = {}
): ServerSocket {
    return bind(socketAddress, configure)
}

suspend fun UDPSocketBuilder.bind(
    socketAddress: Address,
    configure: SocketOptions.UDPSocketOptions.() -> Unit = {}
): BoundDatagramSocket {
    return bind(
        InetSocketAddress(
            socketAddress.host,
            socketAddress.port
        ),
        configure
    )
}

suspend fun UDPSocketBuilder.connect(
    socketAddress: Address,
    configure: SocketOptions.UDPSocketOptions.() -> Unit = {}
): BoundDatagramSocket {
    return bind(socketAddress, configure)
}