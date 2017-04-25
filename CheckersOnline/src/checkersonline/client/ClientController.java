/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline.client;

import checkersonline.DataPacket;
import checkersonline.ReceiveThread;
import checkersonline.SendThread;
import checkersonline.Space.Piece;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author bqb5176
 */
public class ClientController extends Thread {
    private boolean running;
    
    private int port;
    private Socket socket;
    
    private ReceiveThread receive;
    private SendThread send;
    
    private Piece me;
    
    public ClientController(int port) {
        this.port = port;
        this.running = false;
    }
    
    public void quit() {
        this.running = false;
        
        try {
            this.socket.close();
        } catch (IOException ex) {
            System.out.println("Connection to server closed.");
        }
    }
    
    @Override
    public void run() {
        this.running = true;
        
        try {  // Connect
            System.out.println("Trying to connect...");
            socket = new Socket("localhost", port);
            
            System.out.println("Connected.");
            
            receive = new ReceiveThread(socket);
            receive.start();
            
            send = new SendThread(socket);
            send.start();
        } catch (IOException ex) {
            System.out.println("Socket could not be initialized.");
        }
        
        System.out.println("Starting game...");
        
        while(running) {  // Game Loop
            DataPacket packet;
            
            if (receive.hasNewData()) {
                packet = receive.latestPacket();
                
                if (packet.getTurn() == Piece.NONE) { // Game hasn't started yet. Get color.
                    me = packet.getYou();
                    
                    System.out.println("Me: " + me);
                }
                else if (packet.getTurn() == me) {
                    
                }
            }
            
            
            
            if (!receive.isAlive() || !send.isAlive()) {
                this.quit();
            }
        }
    }
    
    public static void main(String[] args) {
        ClientController cntl = new ClientController(5555);
        cntl.start();
    }
}
