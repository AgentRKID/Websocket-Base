package org.example.server.nethandler

import com.cheatbreaker.websocket.nethandler.ByteBufWrapper
import io.netty.buffer.Unpooled
import org.example.server.nethandler.impl.WSPacketExample
import org.java_websocket.WebSocket
import java.io.IOException
import java.lang.Exception

class WSNetHandler {

    fun sendPacket(conn: WebSocket?, packet: WSPacket) {
        if (conn != null && conn.isOpen) {
            val wrapper = ByteBufWrapper(Unpooled.buffer())
            WSPacket.REGISTRY[packet.javaClass]?.let { wrapper.writeVarInt(it) }

            try {
                packet.write(wrapper)
                conn.send(wrapper.array())
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
    }

    fun handlePacket(conn: WebSocket?, wrapper: ByteBufWrapper) {
        val packetId = wrapper.readVarInt()
        val clazz = WSPacket.REGISTRY.inverse()[packetId]

        if (clazz != null) {
            try {
                val packet = clazz.getConstructor().newInstance()

                packet.read(wrapper)
                packet.process(conn, this)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun handleExamplePacket(conn: WebSocket?, packet: WSPacketExample) {
        println("Handled Example Packet")
    }

}