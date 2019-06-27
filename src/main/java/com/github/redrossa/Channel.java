package com.github.redrossa;

import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A channel allows for a multiplexing of a TTP session. This makes possible of
 * TTP's unique feature of allowing applications to send commands to a server
 * while simultaneously receiving data from it. Data packets for writing to
 * and reading from an I/O stream are buffered in a channel, which allows a
 * selector in a {@code Portal} object to pick and ultimately control
 * incoming and outgoing traffic.
 *
 * @author  Adriano Raksi
 * @version 1.0-SNAPSHOT
 * @since   2019-06-25
 */
public class Channel
{
    /** Channel ID */
    public final int id;

    /** Packets to be sent to output stream are first buffered in this queue */
    private Queue<Packet> output = new ConcurrentLinkedQueue<>();

    /** Packets to be read from input stream are first buffered in this queue */
    private Queue<Packet> input = new ConcurrentLinkedQueue<>();

    /**
     * Creates a new channel with the specified ID.
     *
     * @param id the channel id.
     */
    public Channel(int id)
    {
        this.id = id;
    }

    /**
     * Inserts packet to output buffer for writing to output stream.
     *
     * @param   p   the packet to insert in output buffer.
     */
    public void send(@NotNull Packet p)
    {
        output.offer(p);
    }

    /**
     * Retrieves packet and remove from input buffer for application use.
     *
     * @return  the packet stored in input buffer.
     */
    public Packet receive()
    {
        return input.poll();
    }

    /**
     * Retrieves packet without removing from input buffer for application use.
     *
     * @return  the packet stored in input buffer.
     */
    public Packet peek()
    {
        return input.peek();
    }

    /**
     * Inserts packet to input buffer by selector for application use.
     *
     * @param   p   the packet to insert in input buffer.
     */
    protected void put(Packet p)
    {
        input.offer(p);
    }

    /**
     * Retrieves packet and remove from output buffer by selector for writing to output stream.
     *
     * @return  the packet stored in output buffer.
     */
    protected Packet get()
    {
        return output.poll();
    }

    /**
     * Retrieves packet without removing from output buffer by selector.
     *
     * @return  the packet stored in output buffer.
     */
    protected Packet look()
    {
        return output.peek();
    }

    /**
     * Returns the size of the output buffer.
     *
     * @return  the size of the output buffer.
     */
    public int outputSize()
    {
        return output.size();
    }

    /**
     * Returns the size of the input buffer.
     *
     * @return  the size of the input buffer.
     */
    public int inputSize()
    {
        return input.size();
    }
}
