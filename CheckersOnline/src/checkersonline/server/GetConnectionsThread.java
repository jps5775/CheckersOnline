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
    private Socket red = null;
    private Socket black = null;
    
    private ServerSocket serverSocket;
    
    public GetConnectionsThread(int port) {
        try {
            InetAddress addr = InetAddress.getByName("localhost");
            serverSocket = new ServerSocket(port, 0, addr);
        } catch (UnknownHostException ex) {
            Logger.getLogger(GetConnectionsThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GetConnectionsThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Socket getRed() {
        return red;
    }
    
    public Socket getBlack() {
        return black;
    }
    
    @Override
    public void run() {
        try {
            System.out.println("Listening on port " + serverSocket.getLocalPort() + " for red");
            red = serverSocket.accept();
            System.out.println("Connected to " + red.getRemoteSocketAddress() + " as red.");
            
            System.out.println("Listening on port " + serverSocket.getLocalPort() + " as black");
            black = serverSocket.accept();
            System.out.println("Connected to " + red.getRemoteSocketAddress() + " for black");
        } catch (IOException ex) {
            Logger.getLogger(GetConnectionsThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
