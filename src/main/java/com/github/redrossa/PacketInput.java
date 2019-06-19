package com.github.redrossa;

import java.io.IOException;

/**
 * The {@code PacketInput} interface provides for reconstructing
 * a Packet object with data read from a binary stream.
 *
 * @author  Adriano Raksi
 * @version 1.0-SNAPSHOT
 * @since   2019-06-19
 */
public interface PacketInput
{
    /**
     * Using underlying DataInputStream, attempts to read all the
     * component instances of a packet object, starting from {@code int}
     * header mask to {@code char} footer. Once all components of a
     * complete packet object is read, it reconstructs the packet using
     * those components.
     *
     * @return  reference to Packet object constructed from data read
     *          from a binary stream.
     * @throws  IOException the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     * @see     java.io.DataInputStream
     */
    Packet readPacket() throws IOException;
}
