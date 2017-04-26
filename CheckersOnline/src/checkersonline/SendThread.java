/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

/**
 * A thread that sends data over the specified socket connection.
 * @author bqb5176
 */
public class SendThread extends Thread {
    private final Socket connection;  // Connection to send data over
    private DataPacket toSend;        // A data packet that needs sent
    private boolean running;          // Flag indicates this thread should keep running.
    
    private final LinkedList<DataPacket> packets; // All packets to send.
    
    public SendThread(Socket connection) {
        this.connection = connection;
        this.running = false;
        this.packets = new LinkedList();
    }
    
    public void sendPacket (DataPacket toSend) {
        synchronized (packets) {
            this.packets.add(toSend);
        }
    }
    
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
            if (connection != null && !connection.isClosed()) {
                while (packets.size() > 0 && running) {
                    try {
                        synchronized (packets) {
                            DataPacket toSend = packets.pop();
                            new DataOutputStream(connection.getOutputStream()).writeUTF(toSend.encode());
                            System.out.println("Sending packet... " + toSend.encode());
                        }
                    } catch (IOException ex) {
                        System.out.println("Exception while sending data. Was the socket closed?");
                        quit();
                    }
                }
            }
            
            else if (connection.isClosed()) {
                quit();
            }
        }
    }
}
