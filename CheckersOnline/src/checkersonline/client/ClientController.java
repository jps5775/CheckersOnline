/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline.client;

import checkersonline.ReceiveThread;
import checkersonline.SendThread;
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
        
        try {
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
        
        while(running) {
            if (receive.hasNewData()) {
                System.out.println(receive.latestPacket().encode());
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
