package org.example.server

import com.cheatbreaker.websocket.nethandler.ByteBufWrapper
import io.netty.buffer.Unpooled
import org.example.server.nethandler.WSNetHandler
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.lang.Exception
import java.net.InetSocketAddress
import java.nio.ByteBuffer

class Application constructor(address: InetSocketAddress) : WebSocketServer(address)  {

    companion object {
        lateinit var instance: Application

        @JvmStatic
        fun main(args: Array<String>) {
            val server = Application(InetSocketAddress("0.0.0.0", 85))
            server.connectionLostTimeout = 30;
            server.run()
        }
    }

    val netHandler: WSNetHandler

    init {
        instance = this

        netHandler = WSNetHandler()
    }

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {


        // You can get handshake stuff like, username + playerId like:
        val handshakeUuid = handshake?.getFieldValue("playerId")

        println("Connected $handshakeUuid")
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onMessage(conn: WebSocket?, message: String?) { /* Don't listen for String Messages */ }

    override fun onMessage(conn: WebSocket?, message: ByteBuffer?) {
        netHandler.handlePacket(conn, ByteBufWrapper(Unpooled.wrappedBuffer(message?.array())))
    }

    override fun onError(conn: WebSocket?, ex: Exception?) {
        ex?.printStackTrace()
    }

    override fun onStart() {
        println("Started WebServer")
    }
}