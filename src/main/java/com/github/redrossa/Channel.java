package com.github.redrossa;

import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A channel allows for a multiplexing of a TTP session. This makes possible of
 * TTP's unique feature of allowing applications to send commands to a server
 * while simultaneously receiving data from it. Data packets for writing to
 * and reading from an I/O stream are buffered in a channel, which allows a
 * selector in a {@code Session} object to pick and ultimately control
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
    private Queue<Packet> outBuffer = new ConcurrentLinkedQueue<>();

    /** Packets to be read from input stream are first buffered in this queue */
    private Queue<Packet> inBuffer = new ConcurrentLinkedQueue<>();

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
     * Inserts packet to out buffer for writing to output stream.
     *
     * @param   p   the packet to insert to out buffer.
     */
    public void send(@NotNull Packet p)
    {
        outBuffer.offer(p);
    }

    /**
     * Retrieves packet and remove from in buffer for application use.
     *
     * @return  the packet stored in in buffer.
     */
    public Packet receive()
    {
        return inBuffer.poll();
    }

    /**
     * Retrieves packet without removing from in buffer for application use.
     *
     * @return  the packet stored in in buffer.
     */
    public Packet peek()
    {
        return inBuffer.peek();
    }

    /**
     * Inserts packet to in buffer by selector for application use.
     *
     * @param   p   the packet to insert to in buffer.
     */
    protected void put(Packet p)
    {
        inBuffer.offer(p);
    }

    /**
     * Retrieves packet and remove from out buffer by selector for writing to output stream.
     *
     * @return  the packet stored in out buffer.
     */
    protected Packet get()
    {
        return outBuffer.poll();
    }

    /**
     * Retrieves packet without removing from out buffer by selector.
     *
     * @return  the packet stored in out buffer.
     */
    protected Packet look()
    {
        return outBuffer.peek();
    }

    /**
     * Returns the size of the out buffer.
     *
     * @return  the size of the out buffer.
     */
    public int outBufferSize()
    {
        return outBuffer.size();
    }

    /**
     * Returns the size of the in buffer.
     *
     * @return  the size of the in buffer.
     */
    public int inBufferSize()
    {
        return inBuffer.size();
    }
}
