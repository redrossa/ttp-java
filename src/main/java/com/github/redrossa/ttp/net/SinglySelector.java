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

package com.github.redrossa.ttp.net;

import com.github.redrossa.ttp.io.Packet;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class SinglySelector
{
    private volatile boolean selected;
    private volatile boolean shutdown;
    private AtomicInteger cycles = new AtomicInteger();
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private void receive(Portal portal) throws IOException
    {
        Packet packet, encapped;
        while (true)
        {
            try
            {
                encapped = portal.in.readPacket();
                packet = (Packet) Packet.format(encapped);
                portal.getChannels()[encapped.getFooter()].put(packet);
            }
            catch (SocketTimeoutException ignored)
            {
                // Expected exceptions are not useful
                break;
            }
        }
    }

    private void send(Portal portal, int ch) throws IOException
    {
        Channel channel = portal.getChannels()[ch];
        int limit = channel.outputSize();
        if (limit == 0)
            return;
        Packet packet, encapped;
        for (int i = 0; i < limit; i++)
        {
            packet = i != limit - 1 ? channel.get() : channel.look();
            encapped = new Packet(packet, ch);
            portal.out.writePacket(encapped);
        }
        portal.out.flush();
        channel.get();
    }

    public void cycle(Portal portal) throws IOException
    {
        cycles.incrementAndGet();
        for (int i = 0; i < portal.getChannels().length; i++)
        {
            send(portal, i);
            receive(portal);
        }
    }

    public int getCycles()
    {
        return cycles.get();
    }

    public boolean isSelected()
    {
        return selected;
    }

    public Future<Void> select(Portal portal)
    {
        if (selected)
            return null;
        selected = true;
        return executor.submit(() ->
        {
            while (!(shutdown || Thread.currentThread().isInterrupted()))
               cycle(portal);
            shutdown = true;
            return null;
        });
    }

    public boolean isShutdown()
    {
        return shutdown;
    }

    public void shutdown()
    {
        if (shutdown)
            return;
        shutdown = true;
        executor.shutdown();
    }
}
