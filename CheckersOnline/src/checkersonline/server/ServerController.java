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
import checkersonline.Space.Piece;
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
        
        GetConnectionsThread getConnections = new GetConnectionsThread("localhost", 5555);
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
        
        // Tell the clients their color
        DataPacket red = new DataPacket();
        red.setYou(Piece.RED);
        redSend.sendPacket(red);
        
        DataPacket black = new DataPacket();
        black.setYou(Piece.BLACK);
        blackSend.sendPacket(black);
        
        // Game loop
        
        while (running) {
            Piece turn = gameController.getTurn();
            
            // Ask client for move.
            DataPacket packet = new DataPacket();
            packet.setTurn(turn);
            packet.setBoard(gameController.getBoard());
            
            switch (turn) {
                case RED:
                    redSend.sendPacket(packet);
                    break;
                case BLACK:
                    blackSend.sendPacket(packet);
                    break;
            }
        }
    }
    
    public static void main(String[] args) {
        ServerController cntl = new ServerController();
        cntl.start();
    }
}
