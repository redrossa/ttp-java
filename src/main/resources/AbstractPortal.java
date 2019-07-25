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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

/**
 * This class provides skeletal implementation of the {@code AbstractPortal}
 * interface, to minimise the effort required to implement this
 * interface, as well as the necessary instance resources an implementation
 * of {@code AbstractPortal} should all have.
 * <p>
 * To implement a blocking singleplexed {@code AbstractPortal}, the programmer needs
 * to extend this class and simply provide implementations for the {@code transfer}
 * methods and the {@code close} method.
 * <p>
 * To implement a non-blocking multiplexed {@code AbstractPortal}, please see the
 * {@link MultiplexedPortal} abstract class, a subclass of this class modified
 * especially for providing multiplexing of {@code AbstractPortal}.
 *
 * @author  Adriano Raksi
 * @since   2019-07-08
 */
public abstract class AbstractPortal implements Portal
{
    /** Closed state of this portal */
    protected volatile boolean closed;

    /** AbstractPortal getName */
    private String name;

    /** The underlying socket of this portal */
    protected Socket socket;

    /** The underlying output stream of the socket of this portal */
    protected PacketOutputStream out;

    /** The underlying input stream of the socket of this portal */
    protected PacketInputStream in;

    /** Constructor for use by subclasses */
    protected AbstractPortal() { }

    /**
     * Creates a new AbstractPortal with the specified socket.
     *
     * @param   socket  the underlying socket.
     * @param   name    the getName of this portal.
     * @throws  IOException if an I/O error occurs when creating the
     *          output stream or if the socket is not connected.
     */
    protected AbstractPortal(@NotNull Socket socket, String name) throws IOException
    {
        this.name = name;
        this.socket = socket;
        out = new PacketOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        in = new PacketInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    /**
     * Writes a packet into the underlying output stream then
     * flushes the stream.
     *
     * @param   p   the packet to send.
     * @throws  IOException the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     */
    public void send(@NotNull Packet p) throws IOException
    {
        out.writePacket(p);
        out.flush();
    }

    /**
     * Returns a packet read from the underlying input stream.
     *
     * @return  the packet received
     * @throws  EOFException  if this input stream has reached the end.
     * @throws  IOException   the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     */
    public Packet receive() throws IOException
    {
        return in.readPacket();
    }

    /**
     * Returns the name of this portal.
     *
     * @return the name of this portal.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the closed status of this portal.
     *
     * @return  the closed status.
     */
    public final boolean isClosed()
    {
        return closed;
    }
}
