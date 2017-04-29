/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline.client;

import checkersonline.Board;
import checkersonline.DataPacket;
import checkersonline.GameController.D;
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
    
    private String username;
    
    private String host;
    private int port;
    private Socket socket;
    
    private ReceiveThread receive;
    private SendThread send;
    
    private Piece me = Piece.NONE;
    private ClientEventHandler eventHandler;
    
    public ClientController(String host, int port, String username) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.running = false;
    }
    
    public void setEventHandler(ClientEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }
    
    public void sendMove(int x, int y, D direction) {
        DataPacket move = new DataPacket();
        move.setStatus(DataPacket.IS_MOVE);
        move.setX(x);
        move.setY(y);
        move.setDirection(direction);
        send.sendPacket(move);
    }
    
    public String getUsername() {
        return username;
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
            socket = new Socket(host, port);
            
            System.out.println("Connected.");
            
            receive = new ReceiveThread(socket);
            receive.start();
            
            send = new SendThread(socket);
            send.start();
        } catch (IOException ex) {
            System.out.println("Socket could not be initialized.");
            running = false;
        }
        
        System.out.println("Starting game...");
        
        while(running) {  // Game Loop
            DataPacket packet;
            
            if (receive.hasNewData()) {
                packet = receive.getNextPacket();
                
                if (me == Piece.NONE) { // Game hasn't started yet. Get color.
                    me = packet.getYou();
                    
                    if (this.eventHandler != null) {
                        this.eventHandler.onColorAssigned(me);
                    }
                }
                
                switch (packet.getStatus()) {
                    case DataPacket.NEED_MOVE: // It's my turn
                    case DataPacket.BAD_MOVE:  // or my move was bad.
                        if (this.eventHandler != null) {
                            this.eventHandler.onMyTurn(packet.getStatus() == DataPacket.BAD_MOVE);
                        }
                        break;
                    case DataPacket.NEW_BOARD: // Updated board received.
                        if (this.eventHandler != null) {
                            this.eventHandler.onNewBoard(packet.getBoard());
                        }
                        break;
                    case DataPacket.GAME_OVER: // Game is over and there is a winner.
                        if (this.eventHandler != null) {
                            this.eventHandler.onGameOver(packet.getWinner());
                        }
                        
                        quit();
                        break;
                    default:
                        break;
                }
            }
            
            if (!receive.isAlive() || !send.isAlive()) {
                this.quit();
            }
        }
    }
    
    public static void main(String[] args) {
        UsernameGUI usernameGUI = new UsernameGUI() {
            @Override
            public void onSubmit(String username) {
                ClientController cntl = new ClientController("localhost", 5555,
                                                             username);
                //CLI cli = new CLI(cntl);

                GUI gui = new GUI(cntl);
                gui.setVisible(true);

                cntl.start();
            }
        };
    }
}
