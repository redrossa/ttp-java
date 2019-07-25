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

public class DirectPortal extends Portal
{
    public DirectPortal(InetAddress address, int port) throws IOException
    {
        this(new Socket(address, port));
    }

    public DirectPortal(InetAddress address, int port, InetAddress localAddr, int localPort) throws IOException
    {
        this(new Socket(address, port, localAddr, localPort));
    }

    public DirectPortal(String host, int port) throws IOException
    {
        this(new Socket(host, port));
    }

    public DirectPortal(String host, int port, InetAddress localAddr, int localPort) throws IOException
    {
        this(new Socket(host, port, localAddr, localPort));
    }

    public DirectPortal(ServerSocket ss) throws IOException
    {
        this(ss.accept());
    }

    private DirectPortal(Socket socket) throws IOException
    {
        super(new BufferedInputStream(socket.getInputStream()), new BufferedOutputStream(socket.getOutputStream()));
        socket.setSoTimeout(0);
    }

    @Override
    public Packet receive(Class<? extends Headerable> clazz, int ch)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Packet receive(Class<? extends Headerable> clazz) throws IOException
    {
        Packet p = in.readPacket();
        Headerable.valueOf(p.getHeader(), clazz);
        return p;
    }

    @Override
    public void send(Packet p, Class<? extends Headerable> clazz, int ch)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void send(Packet p, Class<? extends Headerable> clazz) throws IOException
    {
        Headerable.valueOf(p.getHeader(), clazz);
        out.writePacket(p);
        out.flush();
    }

    @Override
    public Channel[] getChannels()
    {
        return new Channel[0];
    }
}
