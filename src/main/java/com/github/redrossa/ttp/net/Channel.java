package com.github.redrossa.ttp.net;/*
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

import com.github.redrossa.ttp.io.Packet;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * TODO FIX DOCUMENTATION PLSSSSS
 *
 * A channel allows for a multiplexing of a TTP session. This makes possible of
 * TTP's unique feature of allowing applications to send commands to a server
 * while simultaneously receiving data from it. Data packets for writing to
 * and reading from an I/O stream are buffered in a channel, which allows a
 * selector in a {@code AbstractPortal} object to pick and ultimately control
 * incoming and outgoing traffic.
 *
 * @author  Adriano Raksi
 * @version 1.0-SNAPSHOT
 * @since   2019-06-25
 */
public class Channel implements Receivable, Sendable
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

    /** Blocks until all packets in output buffer are sent */
    public synchronized void awaitOutput() throws InterruptedException
    {
        while (output.size() > 0)
            wait();
    }

    /** Blocks until at least {@code 1} packet in input buffer is available. */
    public synchronized void awaitInput() throws InterruptedException
    {
        while (input.size() == 0)
            wait();
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
    protected synchronized void put(Packet p)
    {
        input.offer(p);
        notify();
    }

    /**
     * Retrieves packet and remove from output buffer by selector for writing to output stream.
     *
     * @return  the packet stored in output buffer.
     */
    protected synchronized Packet get()
    {
        Packet p = output.poll();
        notify();
        return p;
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
