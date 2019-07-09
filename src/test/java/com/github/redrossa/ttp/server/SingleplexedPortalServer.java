package com.github.redrossa.ttp.server;

import com.github.redrossa.ttp.Portal;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class SingleplexedPortalServer implements Runnable
{
    protected ServerSocket serverSocket;
    private Thread thread;

    public SingleplexedPortalServer() throws IOException
    {
        serverSocket = new ServerSocket(Portal.PORT);
        thread = new Thread(this);
    }

    @Override
    public void run()
    {
        while (!Thread.interrupted())
        {
            try
            {
                Socket socket = serverSocket.accept();
                new SingleplexedClientHandler(socket).start();
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

    public void start()
    {
        System.out.println("Server started at port: " + Portal.PORT);
        thread.start();
    }

    public void stop() throws IOException
    {
        thread.interrupt();
        serverSocket.close();
    }
}
