package com.github.redrossa.ttp;

import com.github.redrossa.ttp.server.MultiplexedPortalServer;
import com.github.redrossa.ttp.server.SingleplexedPortalServer;

public class ServerRunner
{
    public static void main(String[] args) throws Exception
    {
        SingleplexedPortalServer server = new MultiplexedPortalServer();
        server.start();
        System.out.println("Press ^C to stop.");
        //noinspection StatementWithEmptyBody
        while (!Thread.interrupted());
        server.stop();
    }
}
