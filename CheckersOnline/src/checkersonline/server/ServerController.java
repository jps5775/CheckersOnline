/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline.server;

import checkersonline.GameController;
import java.net.Socket;

/**
 *
 * @author bqb5176
 */
public class ServerController {
    private GameController gameController;
    private Socket redClient;
    private Socket blackClient;
    
    public ServerController() {
        gameController = new GameController();
        
        GetConnectionsThread getConnections = new GetConnectionsThread(5555);
        getConnections.start();
        
        while(redClient == null && blackClient == null) {
            redClient = getConnections.getRed();
            blackClient = getConnections.getBlack();
        }
    }
    
    public static void main(String[] args) {
        new ServerController();
    }
}
