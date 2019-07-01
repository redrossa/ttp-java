package com.github.redrossa;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public class Portal implements AutoCloseable
{
    /** Control channel ID */
    private static final int CTRL = 0;

    /** Data channel ID */
    private static final int DATA = 1;

    /** Various states of this portal */
    private volatile boolean multiplexed;
    private volatile boolean closed;

    /** The underlying socket of this portal */
    private final Socket socket;

    /** The underlying output stream of the socket of this portal */
    private final PacketOutputStream out;

    /** The underlying input stream of the socket of this portal */
    private final PacketInputStream in;

    /** The number of times this portal has performed IO and individual selector operations */
    private final AtomicInteger cycles;

    /** The channels bound to this portal */
    private final Channel[] channels;

    /** The underlying selector of this portal */
    private Selector selector;

    /**
     * Creates a new Portal with the specified socket. The newly created
     * portal is initially not multiplexed therefore all IO operations are
     * blocking.
     *
     * @param   socket  the underlying socket.
     * @throws  SocketException if there is an error in the underlying
     *          protocol, such as a TCP error.
     * @throws  IOException if an I/O error occurs when creating the
     *          output stream or if the socket is not connected.
     */
    public Portal(@NotNull Socket socket) throws IOException
    {
        socket.setSoTimeout(0);
        this.socket = socket;
        out = new PacketOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        in = new PacketInputStream(new BufferedInputStream(socket.getInputStream()));
        cycles = new AtomicInteger();
        channels = new Channel[2];
    }

    /**
     * Writes a packet into the underlying output stream then
     * flushes the stream.
     * <p>
     * If this portal is multiplexed, this method is equivalent to
     * sending a packet through the control channel.
     *
     * @param   p   the packet to send.
     * @throws  IOException the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     * @see     Channel#send(Packet)
     */
    public final void send(Packet p) throws IOException
    {
        if (multiplexed)
        {
            channels[CTRL].send(p);
            return;
        }
        cycles.incrementAndGet();
        out.writePacket(p);
        out.flush();
    }

    /**
     * Reads a packet from the underlying input stream then return it.
     * <p>
     * If this portal is multiplexed, this method is equivalent to receiving
     * a packet from the control channel.
     *
     * @return  the packet received
     * @throws  EOFException  if this input stream has reached the end.
     * @throws  IOException   the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     * @see     Channel#receive()
     */
    public final Packet receive() throws IOException
    {
        if (multiplexed)
            return channels[CTRL].receive();
        cycles.incrementAndGet();
        return in.readPacket();
    }

    /**
     * Returns the control channel of this portal.
     *
     * @return  the control channel of this portal. Null if not multiplexed.
     */
    public final Channel getControlChannel()
    {
        return channels[CTRL];
    }

    /**
     * Returns the data channel of this portal.
     *
     * @return  the data channel of this portal. Null if not multiplexed.
     */
    public final Channel getDataChannel()
    {
        return channels[DATA];
    }

    /**
     * Returns the IO cycles performed by this portal.
     *
     * @return  the cycles of this portal.
     */
    public int getCycles()
    {
        return cycles.get();
    }

    /**
     * Multiplexes this portal. Socket timeout is set to 1 to enable non-
     * blocking. Control and data channels are initialised and a selector
     * thread is started at the background.
     *
     * @param   <T> the class that extends Portal.
     * @return  this portal itself to enable chaining with the constructor.
     * @throws  SocketException if there is an error
     *          in the underlying protocol, such as a TCP error.
     */
    @SuppressWarnings("unchecked")
    public <T extends Portal> T multiplex() throws SocketException
    {
        socket.setSoTimeout(1);
        multiplexed = true;
        channels[CTRL] = new Channel(CTRL);
        channels[DATA] = new Channel(DATA);
        selector = new Selector();
        selector.start();
        return (T) this;
    }

    /**
     * Returns the multiplexed status of this portal.
     *
     * @return  the multiplexed status.
     */
    public boolean isMultiplexed()
    {
        return multiplexed;
    }

    /**
     * Closes this portal and its underlying IO and socket. If multiplexed,
     * interrupt the selector and blocks until it has finished its task.
     * Calling this method on a closed portal does no effect.
     *
     * @throws  InterruptedException if any thread has interrupted the current thread. The
     *          <i>interrupted status</i> of the current thread is
     *          cleared when this exception is thrown.
     * @throws  IOException if an I/O error occurs.
     */
    @Override
    public void close() throws InterruptedException, IOException
    {
        if (closed)
            return;
        closed = true;
        if (multiplexed)
        {
            selector.interrupt();
            selector.join();
        }
        out.close();
        in.close();
        socket.close();
    }

    /**
     * Returns the closed status of this portal.
     *
     * @return  the closed status.
     */
    public boolean isClosed()
    {
        return closed;
    }

    /**
     * A selector handles multiplexing outgoing and incoming data to be
     * written or read from the underlying output and input stream of the
     * portal respectively. Applications use channels to buffer their data
     * and a selector selects which channel has data ready to be written
     * or which channel an incoming data is to be buffered into for future
     * application use.
     * <p>
     * Each multiplexed portal creates a selector thread. The endpoint portal
     * must be multiplexed having the same number of channels as this portal
     * or not multiplexed at all. If the latter, however, an unwanted
     * {@code Header.INTEGER} type packet indicating the channel ID is written
     * every other intended packet. If the endpoint portal is multiplexed but
     * contains a different number of channels, this implementation is not
     * guaranteed to function properly and could throw an exception at the portal
     * with the lesser number of channels.
     */
    private class Selector extends Thread
    {
        /**
         * Write selector implementation of packet to output stream.
         * <p>
         * The packet buffered in the specified channel is written after
         * a {@code Header.INTEGER} type packet indicating the channel ID
         * is written.
         *
         * @param   channel the channel which packet is to be written.
         * @throws  IOException the stream has been closed and the contained
         *          input stream does not support reading after close, or
         *          another I/O error occurs.
         */
        private void write(@NotNull Channel channel) throws IOException
        {
            if (channel.outputSize() == 0)
                return;
            Packet p = channel.get();
            out.writePacket(new Packet(channel.id));
            out.writePacket(p);
            out.flush();
        }

        /**
         * Read selector implementation of packet from input stream.
         * <p>
         * Reads a {@code Header.INTEGER} type packet then reads another
         * packet which contains the actual application data. The latter
         * packet is put into the buffer of the channel with the ID matching
         * the integer in the former packet.
         *
         * @throws  IOException the stream has been closed and the contained
         *          input stream does not support reading after close, or
         *          another I/O error occurs.
         * @throws  ArrayIndexOutOfBoundsException  if the integer in the first
         *          packet read does not match with any of the channel IDs in
         *          this portal.
         */
        private void read() throws IOException
        {
            try
            {
                int id = Integer.valueOf(new String(in.readPacket().body, StandardCharsets.UTF_8));
                Packet p = in.readPacket();
                channels[id].put(p);
            }
            catch (SocketTimeoutException | SocketException | EOFException ignored) { }
        }

        /**
         * The selector IO loop cycle: writes data buffered in control channel,
         * reads data, writes data buffered in data channel, reads data.
         * <p>
         * When the selector is notified to stop task (interrupted), it will still continue
         * looping for as long as it needs until all packets buffered in control and data
         * channels are written to output stream. Only then will it exit the loop and
         * eventually this method.
         */
        @Override
        public void run()
        {
            while (!this.isInterrupted() || channels[CTRL].outputSize() > 0 || channels[DATA].outputSize() > 0)
            {
                cycles.incrementAndGet();
                try
                {
                    write(channels[CTRL]);
                    read();
                    write(channels[DATA]);
                    read();
                }
                catch (IOException e)
                {
                    Logger.getLogger(Portal.class.getName())
                          .log(Level.SEVERE, "portal-"+socket.getRemoteSocketAddress(), e);
                }
            }
            //noinspection ResultOfMethodCallIgnored
            Thread.interrupted();
        }
    }
}