package com.github.redrossa.ttp;

import java.io.IOException;

/**
 * The {@code PacketOutput} interface provides for deconstructing
 * a {@link Packet} object into a series of bytes to write into a
 * binary stream.
 *
 * @author  Adriano Raksi
 * @version 1.0-SNAPSHOT
 * @since   2019-06-19
 * @see     PacketInput
 * @see     PacketOutputStream
 */
public interface PacketOutput
{
    /**
     * Deconstructs a {@code Packet} into its component parts and writes
     * them into a binary stream as a series of bytes.
     * <p>
     * The first four bytes written represent the header {@code int}
     * value of the {@code Packet}. The next four bytes represent the
     * {@code int} length of the body byte array, followed by the actual
     * body byte array. Finally, the series of bytes concludes with
     * two bytes representing the footer {@code char} value.
     * </p>
     * This method should deconstruct the {@code Packet} by retrieving its
     * fields from {@link Packet#header}, {@link Packet#body} and
     * {@link Packet#footer}.
     *
     * @param  p the packet object to be written.
     * @throws IOException the stream has been closed and the contained
     *         input stream does not support reading after close, or
     *         another I/O error occurs.
     */
    void writePacket(Packet p) throws IOException;
}
