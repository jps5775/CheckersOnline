/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline.client;

import checkersonline.Board;
import checkersonline.DataPacket;
import checkersonline.Space;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author bqb5176
 */
public class Controller {
    
    private Socket socket;
    
    public Controller(int port) {
        try {
            System.out.println("Trying to connect...");
            socket = new Socket("localhost", port);
            
            System.out.println("Connected.");
        } catch (IOException ex) {
            System.out.println("Socket could not be initialized.");
        }
    }
    
    public static void main(String[] args) {
        new Controller(5555);
    }
}
