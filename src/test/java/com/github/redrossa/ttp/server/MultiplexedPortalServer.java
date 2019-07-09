package com.github.redrossa.ttp.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class MultiplexedPortalServer extends SingleplexedPortalServer
{
    public final static int CH_COUNT = 2;

    public MultiplexedPortalServer() throws IOException
    {
        super();
    }

    @Override
    public void run()
    {
        while (!Thread.interrupted())
        {
            try
            {
                Socket socket = serverSocket.accept();
                new MultiplexedClientHandler(socket).start();
            }
            catch (SocketException e)
            {
                System.out.println("Server closed.");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
