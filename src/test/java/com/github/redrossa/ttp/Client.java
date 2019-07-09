package com.github.redrossa.ttp;

import com.github.redrossa.ttp.server.MultiplexedPortalImpl;
import com.github.redrossa.ttp.server.MultiplexedPortalServer;
import com.github.redrossa.ttp.server.SelectorImpl;

import java.net.Socket;
import java.util.Scanner;

public class Client
{
    public static final String ADDRESS = "localhost";

    public static void main(String[] args) throws Exception
    {
        Socket socket = new Socket(ADDRESS, Portal.PORT);
        MultiplexedPortal portal = new MultiplexedPortalImpl(socket, socket.getRemoteSocketAddress().toString(),
                                                  MultiplexedPortalServer.CH_COUNT);
        Selector selector = new SelectorImpl(portal);
        selector.start();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 'exit' to quit.");
        while (!Thread.interrupted())
        {
            System.out.print("Enter: ");
            String input = sc.nextLine().trim();
            Packet req = evaluate(input);
            System.out.println(ADDRESS + " <<< " + req);
            portal.getChannel(0).send(req);
            portal.getChannel(0).awaitInput();
            Packet res = portal.getChannel(0).receive();
            System.out.println(ADDRESS + " >>> " + res);
            if (req.header == Header.BOOLEAN.getMask() || req.header == Header.INTEGER.getMask() || req.header == Header.DOUBLE.getMask() || req.header == Header.STRING.getMask())
            {
                portal.getChannel(1).awaitInput();
                System.out.println("Data: " + portal.getChannel(1).receive());
            }
            if (res.footer == 'c')
                break;
        }
        selector.stop();
        portal.close();
        sc.close();
    }

    public static Packet evaluate(String input)
    {
        try {
            return new Packet(Integer.valueOf(input));
        } catch (Exception ignored) { }
        try {
            return new Packet(Double.valueOf(input));
        } catch (Exception ignored) { }
        if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false"))
            return new Packet(Boolean.valueOf(input));
        if (input.equalsIgnoreCase("exit"))
            return new Packet(Header.OP, "", 'c');
        return new Packet(input);
    }
}
