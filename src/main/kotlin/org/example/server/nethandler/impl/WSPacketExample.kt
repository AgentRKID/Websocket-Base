package org.example.server.nethandler.impl;

import com.cheatbreaker.websocket.nethandler.ByteBufWrapper
import org.example.server.nethandler.WSNetHandler
import org.example.server.nethandler.WSPacket
import org.java_websocket.WebSocket

object WSPacketExample : WSPacket() {

    override fun write(wrapper: ByteBufWrapper?) {
        wrapper?.writeString("Hello, World!")
    }

    override fun read(wrapper: ByteBufWrapper?) {
        println(wrapper?.readString(32))
    }

    override fun process(conn: WebSocket?, netHandler: WSNetHandler) {
        netHandler.handleExamplePacket(conn, this)
    }

}
