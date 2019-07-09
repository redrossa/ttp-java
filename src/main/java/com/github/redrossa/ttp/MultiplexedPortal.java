package com.github.redrossa.ttp;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * This class provides a nonblocking multiplexed skeletal implementation
 * of the {@code Portal} interface, to minimise the effort required to
 * implement this interface, as well as the necessary instance resources
 * an implementation of {@code MultiplexedPortal} should all have.
 * <p>
 * The underlying channels in a multiplexed portal provides abstraction
 * for user IO operations. Data are buffered in channels, which a {@link
 * Selector} performs the actual IO operations. Therefore, a {@code Selector}
 * must first be initialised then be given a reference to this
 * {@code MultiplexedPortal}. Without a {@code Selector} this portal would
 * function like a {@link SingleplexedPortal}, although, IO operations
 * called through its channels would not be performed.
 * <p>
 * A {@code Portal} implementation that extends this class must provide
 * implementation for the {@code transfer} methods, such that data will be
 * sent through the channels, unlike {@link SingleplexedPortal}, but still
 * blocking, by awaiting channel input.
 *
 * @author  Adriano Raksi
 * @since   2019-07-08
 */
public abstract class MultiplexedPortal extends AbstractPortal
{
    /** The channels bound to this portal */
    protected Channel[] channels;

    /**
     * Creates a new Portal with the specified socket.
     *
     * @param   socket  the underlying socket.
     * @throws  SocketException if there is an error in the underlying
     *          protocol, such as a TCP error.
     * @throws  IOException if an I/O error occurs when creating the
     *          output stream or if the socket is not connected.
     */
    protected MultiplexedPortal(@NotNull Socket socket, String name, int chount) throws IOException
    {
        super(socket, name);
        socket.setSoTimeout(1);
        channels = new Channel[chount];
        for (int i = 0; i < chount; i++)
            channels[i] = new Channel(i);
    }

    /**
     * Returns the channel in the underlying channel array at the specified index.
     *
     * @param   i the index of the channel in the array
     * @return  the channel in the array
     */
    public Channel getChannel(int i)
    {
        return channels[i];
    }
}
