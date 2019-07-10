package com.github.redrossa.ttp;

import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.IOException;

/**
 * A portal is an endpoint for communication through TTP, typically
 * to differentiate this client endpoint and the "realm" the server
 * implementing TTP. This class simply delegates the Socket class and
 * its underlying IO streams. Allowing an application to perform socket
 * IO operations and configuration through one object using TTP.
 *
 * @author  Adriano Raksi
 * @since   2019-06-30
 */
public interface Portal extends AutoCloseable
{
    /** Standard port */
    int PORT = 4020;

    /**
     * Writes a packet into the underlying output stream then
     * flushes the stream.
     *
     * @param   p   the packet to send.
     * @throws  IOException the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     */
    void send(@NotNull Packet p) throws IOException;

    /**
     * Returns a packet read from the underlying input stream.
     *
     * @return  the packet received
     * @throws  EOFException  if this input stream has reached the end.
     * @throws  IOException   the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     */
    Packet receive() throws IOException;

    /**
     * Writes a boolean type packet with the specified value into the
     * underlying output stream and flushes the stream. Blocks until
     * respond packet from the remote endpoint is available for reading
     * from the input stream. Returns this respond packet.
     *
     * @param   v   the specified boolean value
     * @return  the respond packet from the remote endpoint.
     * @throws  IOException the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     */
    Packet transfer(boolean v) throws IOException;

    /**
     * Writes a integer type packet with the specified value into the
     * underlying output stream and flushes the stream. Blocks until
     * respond packet from the remote endpoint is available for reading
     * from the input stream. Returns this respond packet.
     *
     * @param   v   the specified boolean value
     * @return  the respond packet from the remote endpoint.
     * @throws  IOException the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     */
    Packet transfer(int v) throws IOException;

    /**
     * Writes a double type packet with the specified value into the
     * underlying output stream and flushes the stream. Blocks until
     * respond packet from the remote endpoint is available for reading
     * from the input stream. Returns this respond packet.
     *
     * @param   v   the specified boolean value
     * @return  the respond packet from the remote endpoint.
     * @throws  IOException the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     */
    Packet transfer(double v) throws IOException;

    /**
     * Writes a string type packet with the specified value into the
     * underlying output stream and flushes the stream. Blocks until
     * respond packet from the remote endpoint is available for reading
     * from the input stream. Returns this respond packet.
     *
     * @param   v   the specified boolean value
     * @return  the respond packet from the remote endpoint.
     * @throws  IOException the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     */
    Packet transfer(@NotNull String v) throws IOException;

    /**
     * Closes this portal and its underlying IO streams and socket.
     *
     * @throws  Exception   if this portal cannot be closed.
     */
    @Override
    void close() throws Exception;
}