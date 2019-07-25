/*
 * MIT License
 *
 * Copyright (c) 2019 Adriano Raksi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * A blocking singleplexed minimal implementation of {@code AbstractPortal}.
 *
 * @author  Adriano Raksi
 * @since   2019-07-08
 */
public class SingleplexedPortal extends AbstractPortal
{
    /**
     * Creates a new AbstractPortal with the specified socket.
     *
     * @param   socket  the underlying socket.
     * @param   name    the name of this portal.
     * @throws  SocketException if there is an error in the underlying
     *                          protocol, such as a TCP error.
     * @throws  IOException     if an I/O error occurs when creating the
     *                          output stream or if the socket is not connected.
     */
    public SingleplexedPortal(@NotNull Socket socket, String name) throws IOException
    {
        super(socket, name);
        socket.setSoTimeout(0);
    }

    /**
     * Writes a boolean type packet with the specified value into the
     * underlying output stream and flushes the stream. Blocks until
     * respond packet from the remote endpoint is available for reading.
     * Returns this respond packet.
     *
     * @param   v   the specified boolean value
     * @return  the respond packet from the remote endpoint.
     * @throws  IOException the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     */
    @Override
    public Packet transfer(boolean v) throws IOException
    {
        Packet tmp = new Packet(v);
        out.writePacket(tmp);
        out.flush();
        return in.readPacket();
    }

    /**
     * Writes a integer type packet with the specified value into the
     * underlying output stream and flushes the stream. Blocks until
     * respond packet from the remote endpoint is available for reading.
     * Returns this respond packet.
     *
     * @param   v   the specified boolean value
     * @return  the respond packet from the remote endpoint.
     * @throws  IOException the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     */
    @Override
    public Packet transfer(int v) throws IOException
    {
        Packet tmp = new Packet(v);
        out.writePacket(tmp);
        out.flush();
        return in.readPacket();
    }

    /**
     * Writes a double type packet with the specified value into the
     * underlying output stream and flushes the stream. Blocks until
     * respond packet from the remote endpoint is available for reading.
     * Returns this respond packet.
     *
     * @param   v   the specified boolean value
     * @return  the respond packet from the remote endpoint.
     * @throws  IOException the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     */
    @Override
    public Packet transfer(double v) throws IOException
    {
        Packet tmp = new Packet(v);
        out.writePacket(tmp);
        out.flush();
        return in.readPacket();
    }

    /**
     * Writes a string type packet with the specified value into the
     * underlying output stream and flushes the stream. Blocks until
     * respond packet from the remote endpoint is available for reading.
     * Returns this respond packet.
     *
     * @param   v   the specified boolean value
     * @return  the respond packet from the remote endpoint.
     * @throws  IOException the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     */
    @Override
    public Packet transfer(@NotNull String v) throws IOException
    {
        Packet tmp = new Packet(v);
        out.writePacket(tmp);
        out.flush();
        return in.readPacket();
    }

    /**
     * Closes this portal and its underlying IO streams and socket.
     *
     * @throws  IOException   if an I/O error occurs.
     */
    @Override
    public void close() throws IOException
    {
        if (closed)
            return;
        closed = true;
        out.close();
        in.close();
        socket.close();
    }
}
