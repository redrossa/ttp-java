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
 * This class provides a nonblocking multiplexed skeletal implementation
 * of the {@code AbstractPortal} interface, to minimise the effort required to
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
 * A {@code AbstractPortal} implementation that extends this class must provide
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
     * Creates a new {@code MultiplexedPortal} with the specified socket.
     *
     * @param   socket  the underlying socket.
     * @param   name    name of this portal.
     * @param   chount  the number of underlying channels.
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

    /**
     * Returns the number of channels in the underlying channel array.
     *
     * @return  the number of 6channels in the array.
     */
    public int getChannelCount()
    {
        return channels.length;
    }

    @Override
    public abstract Packet transfer(boolean v);

    @Override
    public abstract Packet transfer(int v);

    @Override
    public abstract Packet transfer(double v);

    @Override
    public abstract Packet transfer(String v);

    @Override
    public void close() throws Exception
    {
        if (closed)
            return;
        closed = true;
        out.close();
        in.close();
        socket.close();
    }
}
