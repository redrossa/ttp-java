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

import com.github.redrossa.ttp.io.Headerable;
import com.github.redrossa.ttp.io.Packet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class LayeredPortal extends Portal
{
    private Channel[] channels;

    public LayeredPortal(InetAddress address, int port, int size) throws IOException
    {
        this(new Socket(address, port), size);
    }

    public LayeredPortal(InetAddress address, int port, InetAddress localAddr, int localPort, int size) throws IOException
    {
        this(new Socket(address, port, localAddr, localPort), size);
    }

    public LayeredPortal(String host, int port, int size) throws IOException
    {
        this(new Socket(host, port), size);
    }

    public LayeredPortal(String host, int port, InetAddress localAddr, int localPort, int size) throws IOException
    {
        this(new Socket(host, port, localAddr, localPort), size);
    }

    public LayeredPortal(ServerSocket ss, int size) throws IOException
    {
        this(ss.accept(), size);
    }

    private LayeredPortal(Socket socket, int size) throws IOException
    {
        super(new BufferedInputStream(socket.getInputStream()), new BufferedOutputStream(socket.getOutputStream()));
        socket.setSoTimeout(1);
        multiplexable = true;
        channels = new Channel[size];
        for (int i = 0; i < size; i++)
            channels[i] = new Channel(i);
    }

    @Override
    public Packet receive(Class<? extends Headerable> clazz, int ch)
    {
        Packet p = channels[ch].receive();
        Headerable.valueOf(p.getHeader(), clazz);
        return p;
    }

    @Override
    public Packet receive(Class<? extends Headerable> clazz)
    {
        return receive(clazz, 0);
    }

    @Override
    public void send(Packet p, Class<? extends Headerable> clazz, int ch)
    {
        Headerable.valueOf(p.getHeader(), clazz);
        channels[ch].send(p);
    }

    @Override
    public void send(Packet p, Class<? extends Headerable> clazz)
    {
        send(p, clazz, 0);
    }

    @Override
    public Channel[] getChannels()
    {
        return channels;
    }
}
