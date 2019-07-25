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

import com.github.redrossa.ttp.io.Header;
import com.github.redrossa.ttp.io.Headerable;
import com.github.redrossa.ttp.io.Packet;
import com.github.redrossa.ttp.io.PacketInputStream;
import com.github.redrossa.ttp.io.PacketOutputStream;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class Portal implements Receivable, Sendable, Closeable
{
    protected PacketInputStream in;
    protected PacketOutputStream out;
    protected boolean multiplexable;
    protected boolean closed;

    protected Portal() { }

    protected Portal(InputStream in, OutputStream out)
    {
        this.in = new PacketInputStream(in);
        this.out = new PacketOutputStream(out);
    }

    public abstract Packet receive(Class<? extends Headerable> clazz, int ch);

    public Packet receive(int ch)
    {
        return receive(Header.class, ch);
    }

    public abstract Packet receive(Class<? extends Headerable> clazz) throws IOException;

    @Override
    public Packet receive() throws IOException
    {
        return receive(Header.class);
    }

    public abstract void send(Packet p, Class<? extends Headerable> clazz, int ch);

    public void send(Packet p, int ch)
    {
        send(p, Header.class, ch);
    }

    public abstract void send(Packet p, Class<? extends Headerable> clazz) throws IOException;

    @Override
    public void send(Packet p) throws IOException
    {
        send(p, Header.class);
    }

    public abstract Channel[] getChannels();

    public boolean isMultiplexable()
    {
        return multiplexable;
    }

    public boolean isClosed()
    {
        return closed;
    }

    @Override
    public void close() throws IOException
    {
        if (closed)
            return;
        closed = true;
        out.close();
        in.close();
    }
}
