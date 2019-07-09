package com.github.redrossa.ttp.server;

import com.github.redrossa.ttp.Header;
import com.github.redrossa.ttp.Headerable;
import com.github.redrossa.ttp.MultiplexedPortal;
import com.github.redrossa.ttp.Packet;
import com.github.redrossa.ttp.Selector;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

class MultiplexedClientHandler extends SingleplexedClientHandler
{
    protected MultiplexedPortal portal;
    protected Selector selector;

    MultiplexedClientHandler(Socket socket) throws IOException
    {
        portal = new MultiplexedPortalImpl(socket, socket.getRemoteSocketAddress().toString(), MultiplexedPortalServer.CH_COUNT);
        selector = new SelectorImpl(portal);
        selector.start();
    }

    @Override
    public void run()
    {
        while (!Thread.interrupted())
        {
            try
            {
                portal.getChannel(0).awaitInput();
                Packet req = portal.getChannel(0).receive();
                System.out.println(portal.getName() + " >>> " + req);
                Packet res =  evaluate(req);
                System.out.println(portal.getName() + " <<< " + res);
                portal.getChannel(0).send(res);
                if (res.footer == 'c')
                {
                    selector.stop();
                    portal.close();
                    break;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                break;
            }
        }
    }

    @Override
    protected Packet evaluate(@NotNull Packet req)
    {
        Packet res;

        switch (Headerable.valueOf(req.header, Header.class))
        {
            case INTEGER:
            case BOOLEAN:
            case DOUBLE:
            case STRING:
                res = new Packet(Header.OK, "Received!", 0);
                portal.getChannel(1).send(req);
                break;
            case OP:
                if (req.footer == 'c')
                {
                    res = new Packet(Header.OK, "Disconnected.", 'c');
                    break;
                }
            default:
                res = new Packet(Header.BAD, "Invalid.", 0);
                break;
        }

        return res;
    }
}
