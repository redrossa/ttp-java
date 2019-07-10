package com.github.redrossa.ttp.server;

import com.github.redrossa.ttp.MultiplexedPortal;
import com.github.redrossa.ttp.Packet;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

public class MultiplexedPortalImpl extends MultiplexedPortal
{
    /**
     * Creates a new Portal with the specified socket.
     *
     * @param socket the underlying socket.
     * @param name   name of this portal.
     * @param chount the number of underlying channels.
     * @throws IOException     if an I/O error occurs when creating the
     *                         output stream or if the socket is not connected.
     */
    public MultiplexedPortalImpl(@NotNull Socket socket, String name, int chount) throws IOException
    {
        super(socket, name, chount);
    }

    @Override
    public Packet transfer(boolean v)
    {
        getChannel(0).send(new Packet(v));
        getChannel(0).awaitInput();
        return getChannel(0).receive();
    }

    @Override
    public Packet transfer(int v)
    {
        getChannel(0).send(new Packet(v));
        getChannel(0).awaitInput();
        return getChannel(0).receive();
    }

    @Override
    public Packet transfer(double v)
    {
        getChannel(0).send(new Packet(v));
        getChannel(0).awaitInput();
        return getChannel(0).receive();
    }

    @Override
    public Packet transfer(@NotNull String v)
    {
        getChannel(0).send(new Packet(v));
        getChannel(0).awaitInput();
        return getChannel(0).receive();
    }

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
