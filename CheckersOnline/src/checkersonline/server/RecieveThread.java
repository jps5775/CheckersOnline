/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline.server;

import java.net.Socket;

/**
 *
 * @author bqb5176
 */
public class RecieveThread extends Thread {
    private Socket client;
    
    public RecieveThread(Socket client) {
        this.client = client;
    }
}
