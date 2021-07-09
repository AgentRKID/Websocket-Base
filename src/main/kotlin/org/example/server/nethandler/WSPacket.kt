package org.example.server.nethandler

import com.cheatbreaker.websocket.nethandler.ByteBufWrapper
import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import org.example.server.nethandler.impl.WSPacketExample
import org.java_websocket.WebSocket
import java.io.IOException

abstract class WSPacket {

    /**
     * Writes outgoing packet
     */
    @Throws(IOException::class)
    abstract fun write(wrapper: ByteBufWrapper?)

    /**
     * Reads incoming packet
     */
    @Throws(IOException::class)
    abstract fun read(wrapper: ByteBufWrapper?)

    /**
     * Process incoming packet after read
     */
    abstract fun process(conn: WebSocket?, netHandler: WSNetHandler)

    companion object {
        val REGISTRY: BiMap<Class<out WSPacket>, Int> = HashBiMap.create()

        init {
            REGISTRY[WSPacketExample::class.java] = 1
        }
    }
}