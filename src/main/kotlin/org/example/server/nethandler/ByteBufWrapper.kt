package com.cheatbreaker.websocket.nethandler

import io.netty.buffer.ByteBuf
import java.lang.RuntimeException
import java.io.IOException
import java.nio.charset.Charset
import java.io.UnsupportedEncodingException
import io.netty.buffer.ByteBufAllocator
import java.nio.ByteOrder
import java.nio.channels.GatheringByteChannel
import java.nio.channels.ScatteringByteChannel
import io.netty.buffer.ByteBufProcessor
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer

class ByteBufWrapper(private val buf: ByteBuf) : ByteBuf() {
        fun readVarInt(): Int {
                var var1 = 0
                var var2 = 0
                var var3: Byte
                do {
                        var3 = readByte()
                        var1 = var1 or ((var3).toInt() and (0x7F).toInt() shr var2++ * 7)
                        if (var2 > 5) {
                                throw RuntimeException("VarInt too big")
                        }
                } while ((var3).toInt() and (0x80).toInt() == 0x80)
                return var1
        }

        fun writeVarInt(input: Int) {
                var input = input
                while (input and -0x80 != 0x0) {
                        writeByte(((input and 0x7F or 0x80).toByte()).toInt())
                        input = input ushr 7
                }
                writeByte(input)
        }

        @Throws(IOException::class)
        fun readString(maxLength: Int): String {
                val i = readVarInt()
                if (i > maxLength * 4) {
                        throw IOException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")")
                }

                if (i < 0) {
                        throw IOException("The received encoded string buffer length is less than zero! Weird string!")
                }

                return try {
                        val data = this.readBytes(i).array()
                        val s = String(data, Charset.defaultCharset())
                        if (s.length > maxLength) {
                                throw IOException("The received string length is longer than maximum allowed (" + s.length + " > " + maxLength + ")")
                        }
                        s
                } catch (e: UnsupportedEncodingException) {
                        throw IOException(e)
                }
        }

        @Throws(IOException::class)
        fun writeString(string: String) {
                try {
                        val abyte = string.toByteArray(Charset.defaultCharset())
                        if (abyte.size > 32767) {
                                throw IOException("String too big (was " + abyte.size + " bytes encoded, max " + 32767 + ")")
                        }
                        writeVarInt(abyte.size)
                        this.writeBytes(abyte)
                } catch (e: UnsupportedEncodingException) {
                        throw IOException(e)
                }
        }

        override fun capacity(): Int {
                return buf.capacity()
        }

        override fun capacity(var1: Int): ByteBuf {
                return buf.capacity(var1)
        }

        override fun maxCapacity(): Int {
                return buf.maxCapacity()
        }

        override fun alloc(): ByteBufAllocator {
                return buf.alloc()
        }

        override fun order(): ByteOrder {
                return buf.order()
        }

        override fun order(var1: ByteOrder): ByteBuf {
                return buf.order(var1)
        }

        override fun unwrap(): ByteBuf {
                return buf.unwrap()
        }

        override fun isDirect(): Boolean {
                return buf.isDirect
        }

        override fun readerIndex(): Int {
                return buf.readerIndex()
        }

        override fun readerIndex(var1: Int): ByteBuf {
                return buf.readerIndex(var1)
        }

        override fun writerIndex(): Int {
                return buf.writerIndex()
        }

        override fun writerIndex(var1: Int): ByteBuf {
                return buf.writerIndex(var1)
        }

        override fun setIndex(var1: Int, var2: Int): ByteBuf {
                return buf.setIndex(var1, var2)
        }

        override fun readableBytes(): Int {
                return buf.readableBytes()
        }

        override fun writableBytes(): Int {
                return buf.writableBytes()
        }

        override fun maxWritableBytes(): Int {
                return buf.maxWritableBytes()
        }

        override fun isReadable(): Boolean {
                return buf.isReadable
        }

        override fun isReadable(var1: Int): Boolean {
                return buf.isReadable(var1)
        }

        override fun isWritable(): Boolean {
                return buf.isWritable
        }

        override fun isWritable(var1: Int): Boolean {
                return buf.isWritable(var1)
        }

        override fun clear(): ByteBuf {
                return buf.clear()
        }

        override fun markReaderIndex(): ByteBuf {
                return buf.markReaderIndex()
        }

        override fun resetReaderIndex(): ByteBuf {
                return buf.resetReaderIndex()
        }

        override fun markWriterIndex(): ByteBuf {
                return buf.markWriterIndex()
        }

        override fun resetWriterIndex(): ByteBuf {
                return buf.resetWriterIndex()
        }

        override fun discardReadBytes(): ByteBuf {
                return buf.discardReadBytes()
        }

        override fun discardSomeReadBytes(): ByteBuf {
                return buf.discardSomeReadBytes()
        }

        override fun ensureWritable(var1: Int): ByteBuf {
                return buf.ensureWritable(var1)
        }

        override fun ensureWritable(var1: Int, var2: Boolean): Int {
                return buf.ensureWritable(var1, var2)
        }

        override fun getBoolean(var1: Int): Boolean {
                return buf.getBoolean(var1)
        }

        override fun getByte(var1: Int): Byte {
                return buf.getByte(var1)
        }

        override fun getUnsignedByte(var1: Int): Short {
                return buf.getUnsignedByte(var1)
        }

        override fun getShort(var1: Int): Short {
                return buf.getShort(var1)
        }

        override fun getUnsignedShort(var1: Int): Int {
                return buf.getUnsignedShort(var1)
        }

        override fun getMedium(var1: Int): Int {
                return buf.getMedium(var1)
        }

        override fun getUnsignedMedium(var1: Int): Int {
                return buf.getUnsignedMedium(var1)
        }

        override fun getInt(var1: Int): Int {
                return buf.getInt(var1)
        }

        override fun getUnsignedInt(var1: Int): Long {
                return buf.getUnsignedInt(var1)
        }

        override fun getLong(var1: Int): Long {
                return buf.getLong(var1)
        }

        override fun getChar(var1: Int): Char {
                return buf.getChar(var1)
        }

        override fun getFloat(var1: Int): Float {
                return buf.getFloat(var1)
        }

        override fun getDouble(var1: Int): Double {
                return buf.getDouble(var1)
        }

        override fun getBytes(var1: Int, var2: ByteBuf): ByteBuf {
                return buf.getBytes(var1, var2)
        }

        override fun getBytes(var1: Int, var2: ByteBuf, var3: Int): ByteBuf {
                return buf.getBytes(var1, var2, var3)
        }

        override fun getBytes(var1: Int, var2: ByteBuf, var3: Int, var4: Int): ByteBuf {
                return buf.getBytes(var1, var2, var3, var4)
        }

        override fun getBytes(var1: Int, var2: ByteArray): ByteBuf {
                return buf.getBytes(var1, var2)
        }

        override fun getBytes(var1: Int, var2: ByteArray, var3: Int, var4: Int): ByteBuf {
                return buf.getBytes(var1, var2, var3, var4)
        }

        override fun getBytes(var1: Int, var2: ByteBuffer): ByteBuf {
                return buf.getBytes(var1, var2)
        }

        @Throws(IOException::class)
        override fun getBytes(var1: Int, var2: OutputStream, var3: Int): ByteBuf {
                return buf.getBytes(var1, var2, var3)
        }

        @Throws(IOException::class)
        override fun getBytes(var1: Int, var2: GatheringByteChannel, var3: Int): Int {
                return buf.getBytes(var1, var2, var3)
        }

        override fun setBoolean(var1: Int, var2: Boolean): ByteBuf {
                return buf.setBoolean(var1, var2)
        }

        override fun setByte(var1: Int, var2: Int): ByteBuf {
                return buf.setByte(var1, var2)
        }

        override fun setShort(var1: Int, var2: Int): ByteBuf {
                return buf.setShort(var1, var2)
        }

        override fun setMedium(var1: Int, var2: Int): ByteBuf {
                return buf.setMedium(var1, var2)
        }

        override fun setInt(var1: Int, var2: Int): ByteBuf {
                return buf.setInt(var1, var2)
        }

        override fun setLong(var1: Int, var2: Long): ByteBuf {
                return buf.setLong(var1, var2)
        }

        override fun setChar(var1: Int, var2: Int): ByteBuf {
                return buf.setChar(var1, var2)
        }

        override fun setFloat(var1: Int, var2: Float): ByteBuf {
                return buf.setFloat(var1, var2)
        }

        override fun setDouble(var1: Int, var2: Double): ByteBuf {
                return buf.setDouble(var1, var2)
        }

        override fun setBytes(var1: Int, var2: ByteBuf): ByteBuf {
                return buf.setBytes(var1, var2)
        }

        override fun setBytes(var1: Int, var2: ByteBuf, var3: Int): ByteBuf {
                return buf.setBytes(var1, var2, var3)
        }

        override fun setBytes(var1: Int, var2: ByteBuf, var3: Int, var4: Int): ByteBuf {
                return buf.setBytes(var1, var2, var3, var4)
        }

        override fun setBytes(var1: Int, var2: ByteArray): ByteBuf {
                return buf.setBytes(var1, var2)
        }

        override fun setBytes(var1: Int, var2: ByteArray, var3: Int, var4: Int): ByteBuf {
                return buf.setBytes(var1, var2, var3, var4)
        }

        override fun setBytes(var1: Int, var2: ByteBuffer): ByteBuf {
                return buf.setBytes(var1, var2)
        }

        @Throws(IOException::class)
        override fun setBytes(var1: Int, var2: InputStream, var3: Int): Int {
                return buf.setBytes(var1, var2, var3)
        }

        @Throws(IOException::class)
        override fun setBytes(var1: Int, var2: ScatteringByteChannel, var3: Int): Int {
                return buf.setBytes(var1, var2, var3)
        }

        override fun setZero(var1: Int, var2: Int): ByteBuf {
                return buf.setZero(var1, var2)
        }

        override fun readBoolean(): Boolean {
                return buf.readBoolean()
        }

        override fun readByte(): Byte {
                return buf.readByte()
        }

        override fun readUnsignedByte(): Short {
                return buf.readUnsignedByte()
        }

        override fun readShort(): Short {
                return buf.readShort()
        }

        override fun readUnsignedShort(): Int {
                return buf.readUnsignedShort()
        }

        override fun readMedium(): Int {
                return buf.readMedium()
        }

        override fun readUnsignedMedium(): Int {
                return buf.readUnsignedMedium()
        }

        override fun readInt(): Int {
                return buf.readInt()
        }

        override fun readUnsignedInt(): Long {
                return buf.readUnsignedInt()
        }

        override fun readLong(): Long {
                return buf.readLong()
        }

        override fun readChar(): Char {
                return buf.readChar()
        }

        override fun readFloat(): Float {
                return buf.readFloat()
        }

        override fun readDouble(): Double {
                return buf.readDouble()
        }

        override fun readBytes(var1: Int): ByteBuf {
                return buf.readBytes(var1)
        }

        override fun readSlice(var1: Int): ByteBuf {
                return buf.readSlice(var1)
        }

        override fun readBytes(var1: ByteBuf): ByteBuf {
                return buf.readBytes(var1)
        }

        override fun readBytes(var1: ByteBuf, var2: Int): ByteBuf {
                return buf.readBytes(var1, var2)
        }

        override fun readBytes(var1: ByteBuf, var2: Int, var3: Int): ByteBuf {
                return buf.readBytes(var1, var2, var3)
        }

        override fun readBytes(var1: ByteArray): ByteBuf {
                return buf.readBytes(var1)
        }

        override fun readBytes(var1: ByteArray, var2: Int, var3: Int): ByteBuf {
                return buf.readBytes(var1, var2, var3)
        }

        override fun readBytes(var1: ByteBuffer): ByteBuf {
                return buf.readBytes(var1)
        }

        @Throws(IOException::class)
        override fun readBytes(var1: OutputStream, var2: Int): ByteBuf {
                return buf.readBytes(var1, var2)
        }

        @Throws(IOException::class)
        override fun readBytes(var1: GatheringByteChannel, var2: Int): Int {
                return buf.readBytes(var1, var2)
        }

        override fun skipBytes(var1: Int): ByteBuf {
                return buf.skipBytes(var1)
        }

        override fun writeBoolean(var1: Boolean): ByteBuf {
                return buf.writeBoolean(var1)
        }

        override fun writeByte(var1: Int): ByteBuf {
                return buf.writeByte(var1)
        }

        override fun writeShort(var1: Int): ByteBuf {
                return buf.writeShort(var1)
        }

        override fun writeMedium(var1: Int): ByteBuf {
                return buf.writeMedium(var1)
        }

        override fun writeInt(var1: Int): ByteBuf {
                return buf.writeInt(var1)
        }

        override fun writeLong(var1: Long): ByteBuf {
                return buf.writeLong(var1)
        }

        override fun writeChar(var1: Int): ByteBuf {
                return buf.writeChar(var1)
        }

        override fun writeFloat(var1: Float): ByteBuf {
                return buf.writeFloat(var1)
        }

        override fun writeDouble(var1: Double): ByteBuf {
                return buf.writeDouble(var1)
        }

        override fun writeBytes(var1: ByteBuf): ByteBuf {
                return buf.writeBytes(var1)
        }

        override fun writeBytes(var1: ByteBuf, var2: Int): ByteBuf {
                return buf.writeBytes(var1, var2)
        }

        override fun writeBytes(var1: ByteBuf, var2: Int, var3: Int): ByteBuf {
                return buf.writeBytes(var1, var2, var3)
        }

        override fun writeBytes(var1: ByteArray): ByteBuf {
                return buf.writeBytes(var1)
        }

        override fun writeBytes(var1: ByteArray, var2: Int, var3: Int): ByteBuf {
                return buf.writeBytes(var1, var2, var3)
        }

        override fun writeBytes(var1: ByteBuffer): ByteBuf {
                return buf.writeBytes(var1)
        }

        @Throws(IOException::class)
        override fun writeBytes(var1: InputStream, var2: Int): Int {
                return buf.writeBytes(var1, var2)
        }

        @Throws(IOException::class)
        override fun writeBytes(var1: ScatteringByteChannel, var2: Int): Int {
                return buf.writeBytes(var1, var2)
        }

        override fun writeZero(var1: Int): ByteBuf {
                return buf.writeZero(var1)
        }

        override fun indexOf(var1: Int, var2: Int, var3: Byte): Int {
                return buf.indexOf(var1, var2, var3)
        }

        override fun bytesBefore(var1: Byte): Int {
                return buf.bytesBefore(var1)
        }

        override fun bytesBefore(var1: Int, var2: Byte): Int {
                return buf.bytesBefore(var1, var2)
        }

        override fun bytesBefore(var1: Int, var2: Int, var3: Byte): Int {
                return buf.bytesBefore(var1, var2, var3)
        }

        override fun forEachByte(byteBufProcessor: ByteBufProcessor): Int {
                return 0
        }

        override fun forEachByte(i: Int, i1: Int, byteBufProcessor: ByteBufProcessor): Int {
                return 0
        }

        override fun forEachByteDesc(byteBufProcessor: ByteBufProcessor): Int {
                return 0
        }

        override fun forEachByteDesc(i: Int, i1: Int, byteBufProcessor: ByteBufProcessor): Int {
                return 0
        }

        override fun copy(): ByteBuf {
                return buf.copy()
        }

        override fun copy(var1: Int, var2: Int): ByteBuf {
                return buf.copy(var1, var2)
        }

        override fun slice(): ByteBuf {
                return buf.slice()
        }

        override fun slice(var1: Int, var2: Int): ByteBuf {
                return buf.slice(var1, var2)
        }

        override fun duplicate(): ByteBuf {
                return buf.duplicate()
        }

        override fun nioBufferCount(): Int {
                return buf.nioBufferCount()
        }

        override fun nioBuffer(): ByteBuffer {
                return buf.nioBuffer()
        }

        override fun nioBuffer(var1: Int, var2: Int): ByteBuffer {
                return buf.nioBuffer(var1, var2)
        }

        override fun internalNioBuffer(var1: Int, var2: Int): ByteBuffer {
                return buf.internalNioBuffer(var1, var2)
        }

        override fun nioBuffers(): Array<ByteBuffer> {
                return buf.nioBuffers()
        }

        override fun nioBuffers(var1: Int, var2: Int): Array<ByteBuffer> {
                return buf.nioBuffers(var1, var2)
        }

        override fun hasArray(): Boolean {
                return buf.hasArray()
        }

        override fun array(): ByteArray {
                return buf.array()
        }

        override fun arrayOffset(): Int {
                return buf.arrayOffset()
        }

        override fun hasMemoryAddress(): Boolean {
                return buf.hasMemoryAddress()
        }

        override fun memoryAddress(): Long {
                return buf.memoryAddress()
        }

        override fun toString(var1: Charset): String {
                return buf.toString(var1)
        }

        override fun toString(var1: Int, var2: Int, var3: Charset): String {
                return buf.toString(var1, var2, var3)
        }

        override fun hashCode(): Int {
                return buf.hashCode()
        }

        override fun equals(var1: Any?): Boolean {
                return buf == var1
        }

        override fun compareTo(var1: ByteBuf): Int {
                return buf.compareTo(var1)
        }

        override fun toString(): String {
                return buf.toString()
        }

        override fun retain(var1: Int): ByteBuf {
                return buf.retain(var1)
        }

        override fun retain(): ByteBuf {
                return buf.retain()
        }

        override fun refCnt(): Int {
                return buf.refCnt()
        }

        override fun release(): Boolean {
                return buf.release()
        }

        override fun release(var1: Int): Boolean {
                return buf.release(var1)
        }

        companion object {
                const val MAX_STRING_LENGTH = 8191
        }
}

