/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

/**
 * A thread that receives data over a specified socket connection.
 * @author bqb5176
 */
public class ReceiveThread extends Thread {
    private final Socket connection;  // Socket connection to the player or server.
    private DataPacket latest;        // Latest data recieved from the player or server.
    private boolean hasNew;           // Flag indicates that a new packet as been received.
    private boolean running;          // Flag indicates this thread should keep running.
    
    private LinkedList<DataPacket> packets;  // Packets received and not get accessed.
    
    public ReceiveThread(Socket connection) {
        this.connection = connection;
        this.running = false;
        this.packets = new LinkedList();
    }
    
    /**
     * @return True if a new data packet has been received from the connection
     * since the last time that latestPacket() was called.
     */
    public boolean hasNewData() {
        return packets.size() > 0;
    }
    
    /**
     * @return The next unseen packet received. Once returned, the packet is removed.
     */
    public DataPacket getNextPacket() {
        return packets.pop();
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
                    DataPacket p = DataPacket.decode(data);
                    packets.add(p);
                    onReceive(p);
                } catch (IOException ex) {
                    System.out.println("Exception while receiveing data. Was the socket closed?");
                    quit();
                }
            }
        }
    }
    
    public void onReceive(DataPacket packet) {
        // Override for event handling
    }
}
