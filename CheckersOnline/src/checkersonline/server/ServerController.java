/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline.server;

import checkersonline.DataPacket;
import checkersonline.GameController;
import checkersonline.ReceiveThread;
import checkersonline.SendThread;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author bqb5176
 */
public class ServerController extends Thread {
    private boolean running;
    
    private GameController gameController;
    private Socket redClient;
    private Socket blackClient;
    
    private ReceiveThread redReceive;
    private ReceiveThread blackReceive;
    
    private SendThread redSend;
    private SendThread blackSend;
    
    public ServerController() {
        gameController = new GameController();
        this.running = false;
    }
    
    public void quit() {
        this.running = false;
        
        try {
            this.redClient.close();
        } catch (IOException ex) {
            System.out.println("Red connection closed.");
        }
        
        try {
            this.blackClient.close();
        } catch (IOException ex) {
            System.out.println("Black connection closed.");
        }
    }
    
    @Override
    public void run() {
        this.running = true;
        
        GetConnectionsThread getConnections = new GetConnectionsThread(5555);
        getConnections.start();
        
        while (redClient == null && running) {
            redClient = getConnections.getRed();
        }
        
        while (blackClient == null && running) {
            blackClient = getConnections.getBlack();
        }
        
        redReceive = new ReceiveThread(redClient);
        blackReceive = new ReceiveThread(blackClient);
        
        redReceive.start();
        blackReceive.start();
        
        redSend = new SendThread(redClient);
        blackSend = new SendThread(blackClient);
        
        redSend.start();
        blackSend.start();
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            
        }
        
        System.out.println("Sending packet...");
        redSend.sendPacket(new DataPacket());
    }
    
    public static void main(String[] args) {
        ServerController cntl = new ServerController();
        cntl.start();
    }
}
