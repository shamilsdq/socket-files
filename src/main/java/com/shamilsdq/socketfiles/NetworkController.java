package com.shamilsdq.socketfiles;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class NetworkController 
{
    private final int PORT = 5023;
    
    private ServerSocket server;
    private Socket socket;
    
    
    public NetworkController() throws IOException
    {
        this.server = new ServerSocket(PORT);
        this.server.setSoTimeout(1000);
    }
    
    
    // INCOMING CONNECTIONS
    
    public Socket waitIncomingConnection() throws IOException, InterruptedException
    {       
        // Server waits for incoming requests
        Socket temp_socket = this.server.accept();
        return temp_socket;
    }
    
    public void acceptIncomingConnection(Socket peer)
    {
        try
        {
            // Send "ACCEPT
            peer.getOutputStream().write("ACCEPT".getBytes());
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }
    
    public void rejectIncomingConnection(Socket peer)
    {
        try
        {
            // Send "REJECT"
            peer.getOutputStream().write("REJECT".getBytes());
            peer.close();
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }
    
    
    public void receiveFile()
    {
        // Receive file
    }
    
    
    // OUTGOING CONNECTIONS
    
    public void startOutgoingConnection()
    {
        // Send request and wait for response
    }
    
    public void sendFile()
    {
        // Send file
    }
    
    
    public void close()
    {
        try
        {
            this.server.close();
            this.socket.close();
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }
    
}
