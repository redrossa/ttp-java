package com.github.redrossa;

import java.io.IOException;

/**
 * The {@code PacketOutput} interface provides for converting Packet
 * objects into data from Java primitive types to be written to a
 * binary stream as a series of bytes.
 *
 * @author  Adriano Raksi
 * @version 1.0-SNAPSHOT
 * @since   2019-06-19
 */
public interface PacketOutput
{
    /**
     * This method breaks down the packet into its component instances.
     * Using underlying DataOutputStream, it writes those instances to
     * the binary stream according to its primitive types.
     *
     * @throws  IOException the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     * @see     java.io.DataOutputStream
     */
    void writePacket(Packet p) throws IOException;
}
