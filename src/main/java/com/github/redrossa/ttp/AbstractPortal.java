package com.github.redrossa.ttp;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

/**
 * This class provides skeletal implementation of the {@code Portal}
 * interface, to minimise the effort required to implement this
 * interface, as well as the necessary instance resources an implementation
 * of {@code Portal} should all have.
 * <p>
 * To implement a blocking singleplexed {@code Portal}, the programmer needs
 * to extend this class and simply provide implementations for the {@code transfer}
 * methods and the {@code close} method.
 * <p>
 * To implement a non-blocking multiplexed {@code Portal}, please see the
 * {@link MultiplexedPortal} abstract class, a subclass of this class modified
 * especially for providing multiplexing of {@code Portal}.
 *
 * @author  Adriano Raksi
 * @since   2019-07-08
 */
public abstract class AbstractPortal implements Portal
{
    /** Closed state of this portal */
    protected volatile boolean closed;

    /** Portal name */
    private final String name;

    /** The underlying socket of this portal */
    protected final Socket socket;

    /** The underlying output stream of the socket of this portal */
    protected final PacketOutputStream out;

    /** The underlying input stream of the socket of this portal */
    protected final PacketInputStream in;

    /**
     * Creates a new Portal with the specified socket.
     *
     * @param   socket  the underlying socket.
     * @param   name    the name of this portal.
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
