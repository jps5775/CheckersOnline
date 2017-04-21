/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bqb5176
 */
public class GetConnectionsThread extends Thread {
    private int port;
    
    private Socket red = null;
    private Socket black = null;
    
    private ServerSocket serverSocket;
    
    public GetConnectionsThread(int port) {
        this.port = port;
    }
    
    /**
     * @return The client socket belonging to the red player.
     */
    public synchronized Socket getRed() {
        return red;
    }
    
    /**
     * Must be synchronized the prevent the program from locking up.
     * @return The client socket belonging to the black player.
     */
    public synchronized Socket getBlack() {
        return black;
    }
    
    @Override
    public void run() {
        try {
            InetAddress addr = InetAddress.getByName("localhost");
            serverSocket = new ServerSocket(port, 0, addr);
        } catch (UnknownHostException ex) {
            Logger.getLogger(GetConnectionsThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GetConnectionsThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (serverSocket != null) {
            try {
                synchronized(this) { // These need to be synchronized or else the program might get stuck.
                    System.out.println("Listening on port " + serverSocket.getLocalPort() + " for red");
                    red = serverSocket.accept();
                    System.out.println("Connected to " + red.getRemoteSocketAddress() + " as red.");
                }

                synchronized(this) { // These need to be synchronized or else the program might get stuck.
                    System.out.println("Listening on port " + serverSocket.getLocalPort() + " for black");
                    black = serverSocket.accept();
                    System.out.println("Connected to " + red.getRemoteSocketAddress() + " as black");
                }
            } catch (IOException ex) {
                Logger.getLogger(GetConnectionsThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
