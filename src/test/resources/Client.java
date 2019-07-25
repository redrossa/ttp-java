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

import com.github.redrossa.ttp.io.Header;
import com.github.redrossa.ttp.io.Packet;
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
        Socket socket = new Socket(ADDRESS, AbstractPortal.PORT);
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
