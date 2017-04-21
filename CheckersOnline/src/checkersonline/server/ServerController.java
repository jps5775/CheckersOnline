/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline.server;

import checkersonline.GameController;
import checkersonline.ReceiveThread;
import java.net.Socket;

/**
 *
 * @author bqb5176
 */
public class ServerController extends Thread {
    private GameController gameController;
    private Socket redClient;
    private Socket blackClient;
    
    private ReceiveThread redReceive;
    private ReceiveThread blackReceive;
    
    public ServerController() {
        gameController = new GameController();
    }
    
    @Override
    public void run() {
        GetConnectionsThread getConnections = new GetConnectionsThread(5555);
        getConnections.start();
        
        while(redClient == null && blackClient == null) {
            redClient = getConnections.getRed();
            blackClient = getConnections.getBlack();
        }
        
        redReceive = new ReceiveThread(redClient);
        blackReceive = new ReceiveThread(blackClient);
        
        redReceive.start();
        blackReceive.start();
    }
    
    public static void main(String[] args) {
        ServerController cntl = new ServerController();
        cntl.start();
    }
}
