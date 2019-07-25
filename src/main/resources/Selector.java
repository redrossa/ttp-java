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

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

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
 *
 * @author  Adriano Raksi
 * @since   2019-07-08
 */
public abstract class Selector implements Runnable
{
    /** The cycles count */
    protected AtomicInteger cycles = new AtomicInteger();

    /** The underlying multiplexed portal */
    protected MultiplexedPortal portal;

    /** Thread that runs this selector */
    private Thread thread = new Thread(this);

    /**
     * Creates a new Selector with the specified multiplexed portal.
     *
     * @param   portal  the multiplexed portal.
     */
    public Selector(MultiplexedPortal portal)
    {
        this.portal = portal;
    }

    /**
     * Returns the number of cycles this selector has performed
     * (essentially the number of times {@code cycle} has been
     * called).
     *
     * @return  the number of cycles performed.
     */
    public AtomicInteger getCycles()
    {
        return cycles;
    }

    /**
     * Write selector implementation of packet to output stream.
     * The packet buffered in the specified channel is written after
     * a {@code Header.INTEGER} type packet indicating the channel ID
     * is written.
     * <p>
     * This method provides a fully functioning implementation therefore
     * it is not necessary to override this method.
     *
     * @param   channel the channel which packet is to be written.
     * @throws  IOException the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     */
    protected void output(@NotNull Channel channel) throws IOException
    {
        if (channel.outputSize() == 0)
            return;
        Packet p = channel.get();
        portal.out.writePacket(new Packet(channel.id));
        portal.out.writePacket(p);
        portal.out.flush();
    }

    /**
     * Read selector implementation of packet from input stream.
     * Reads a {@code Header.INTEGER} type packet then reads another
     * packet which contains the actual application data. The latter
     * packet is put into the buffer of the channel with the ID matching
     * the integer in the former packet.
     * <p>
     * This method provides a fully functioning implementation therefore
     * it is not necessary to override this method.
     *
     * @throws  NumberFormatException   if the first packet read cannot
     *          be formatted into a number.
     * @throws  IOException the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     * @throws  ArrayIndexOutOfBoundsException  if the integer in the first
     *          packet read does not match with any of the channel IDs in
     *          this portal.
     */
    protected void input() throws IOException
    {
        try
        {
            int id = Integer.valueOf(portal.in.readPacket().format());
            Packet p = portal.in.readPacket();
            portal.channels[id].put(p);
        }
        catch (SocketTimeoutException | SocketException | EOFException ignored) { }
    }

    /**
     * The IO cycle pattern in which {@code run()} will continuously call
     * in a loop. All IO operations of a selector is recommended to be
     * performed by calling this object's {@code output} and {@code input}.
     * Always increment the cycles count for each call of this method.
     */
    protected void cycle()
    {
        cycles.incrementAndGet();
    }

    /**
     * Returns true if any of the channels in the underlying portal contains
     * buffered packets awaiting to be sent.
     *
     * @return  the condition status
     */
    private boolean awaits()
    {
        for (Channel c : portal.channels)
            if (c.outputSize() > 0)
                return true;
        return false;
    }

    /**
     * Performs a loop of IO operations (cycle) until current thread is interrupted
     * (by calling {@code stop}). The loop does not break immediately when this
     * selector is stopped, however, as it continues until all packets in all the
     * channels of this underlying portal awaiting to be sent are sent.
     * <p>
     * If possible, do not override this method. Simply override the
     * {@code cycle} method to implement the IO operations pattern.
     */
    @Override
    public void run()
    {
        while (!Thread.currentThread().isInterrupted() || awaits())
            cycle();
        //noinspection ResultOfMethodCallIgnored resets interrupted status after loop ends
        Thread.interrupted();
    }

    /**
     * Starts this selector, causing this selector instance thread to begin execution.
     * <p>
     * This method simply performs {@code thread.start()}.
     */
    public void start()
    {
        thread.start();
    }

    /**
     * Stops this selector. Interrupts this selector instance thread then join it,
     * blocking the caller execution until the thread stops its task, that is when
     * all packets in all the channels of this underlying portal awaiting to be sent
     * are sent.
     *
     * @throws  InterruptedException    if any thread has interrupted the current thread. The
     *          <i>interrupted status</i> of the current thread is
     *          cleared when this exception is thrown.
     */
    public void stop() throws InterruptedException
    {
        thread.interrupt();
        thread.join();
    }
}
