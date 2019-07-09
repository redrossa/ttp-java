package com.github.redrossa.ttp.server;

import com.github.redrossa.ttp.Header;
import com.github.redrossa.ttp.Headerable;
import com.github.redrossa.ttp.Packet;
import com.github.redrossa.ttp.SingleplexedPortal;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

class SingleplexedClientHandler extends Thread
{
    protected SingleplexedPortal portal;

    protected SingleplexedClientHandler() { }

    SingleplexedClientHandler(Socket socket) throws IOException
    {
        portal = new SingleplexedPortal(socket, socket.getRemoteSocketAddress().toString());
    }

    @Override
    public void run()
    {
        while (!Thread.interrupted())
        {
            try
            {
                Packet req = portal.receive();
                System.out.println(portal.getName() + " >>> " + req);
                Packet res =  evaluate(req);
                System.out.println(portal.getName() + " <<< " + res);
                portal.send(res);
                if (res.footer == 'c')
                {
                    portal.close();
                    break;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
                break;
            }
        }
    }

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
