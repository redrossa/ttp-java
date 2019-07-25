package com.github.redrossa.ttp;

import com.github.redrossa.ttp.io.Packet;
import com.github.redrossa.ttp.net.DirectPortal;
import com.github.redrossa.ttp.net.LayeredPortal;
import com.github.redrossa.ttp.net.Portal;
import com.github.redrossa.ttp.net.SinglySelector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;

import static java.lang.System.out;

public class Test
{
    public static Thread server = new Thread(() ->
    {
        Future future = null;
        SinglySelector selector = new SinglySelector();
        try
        {
            ServerSocket ss = new ServerSocket(4020);
            Portal portal = new LayeredPortal(ss, 1);
            future = selector.select(portal);

            portal.getChannels()[0].awaitInput();
            out.println(portal.getChannels()[0].receive());
            out.println(portal.getChannels()[0].receive());

            selector.shutdown();
            portal.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        out.println("server done: " + selector.getCycles());
    }, "server");

    public static void main(String[] args) throws Exception
    {
        server.start();

        Portal portal = new LayeredPortal("localhost", 4020, 1);
        SinglySelector selector = new SinglySelector();
        Future future = selector.select(portal);

        portal.getChannels()[0].send(new Packet("Test"));
        portal.getChannels()[0].send(new Packet("Follow up!"));
        portal.getChannels()[0].awaitOutput();

        selector.shutdown();
        portal.close();

        out.println("main done: " + selector.getCycles());
    }
}
