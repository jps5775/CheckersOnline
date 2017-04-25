/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * A thread that receives data over a specified socket connection.
 * @author bqb5176
 */
public class ReceiveThread extends Thread {
    private final Socket connection;  // Socket connection to the player or server.
    private DataPacket latest;        // Latest data recieved from the player or server.
    private boolean hasNew;           // Flag indicates that a new packet as been received.
    private boolean running;          // Flag indicates this thread should keep running.
    
    public ReceiveThread(Socket connection) {
        this.connection = connection;
        this.running = false;
    }
    
    /**
     * @return True if a new data packet has been received from the connection
     * since the last time that latestPacket() was called.
     */
    public synchronized boolean hasNewData() {
        return hasNew;
    }
    
    /**
     * @return The latest data packet received from the connection.
     */
    public synchronized DataPacket latestPacket() {
        hasNew = false;
        return latest;
    }
    
    /**
     * Stop this thread and close the connection.
     */
    public void quit() {
        this.running = false;
        
        try {
            this.connection.close();
        } catch (IOException ex) {
            System.out.println("Connection closed.");
        }
    }
    
    @Override
    public void run() {
        this.running = true;
        
        while(running) {
            if (connection != null) {
                try {
                    DataInputStream in = new DataInputStream(connection.getInputStream());
                    String data = in.readUTF();

                    synchronized (this) {
                        latest = DataPacket.decode(data);
                        hasNew = true;
                    }
                } catch (IOException ex) {
                    System.out.println("Exception while recieveing data. Was the socket closed?");
                    quit();
                }
            }
        }
    }
}
