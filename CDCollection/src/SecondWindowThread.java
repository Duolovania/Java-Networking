//Source:
//  Creating a simple Chat Client1/Server Solution
//  http://pirate.shu.edu/~wachsmut/Teaching/CSAS2214/Virtual/Lectures/chat-client-server.html


import java.net.*;
import java.io.*;

public class SecondWindowThread extends Thread
{
    private Socket socket = null;
    private SecondaryWindow client2 = null;
    private DataInputStream streamIn = null;

    public SecondWindowThread(SecondaryWindow _client2, Socket _socket)
    {
        client2 = _client2;
        socket = _socket;
        open();
        start();
    }

    public void open()
    {
        try
        {
            streamIn = new DataInputStream(socket.getInputStream());
        }
        catch (IOException ioe)
        {
            System.out.println("Error getting input stream: " + ioe);
//            client2.stop();
            client2.close();
        }
    }

    public void close()
    {
        try
        {
            if (streamIn != null)
            {
                streamIn.close();
            }
        }
        catch (IOException ioe)
        {
            System.out.println("Error closing input stream: " + ioe);
        }
    }

    public void run()
    {
        while (true)
        {
            try
            {
                client2.handle(streamIn.readUTF());
            }
            catch (IOException ioe)
            {
                System.out.println("Listening error: " + ioe.getMessage());
//                client2.stop();
                client2.close();
            }
        }
    }
}