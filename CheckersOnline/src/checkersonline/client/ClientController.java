/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline.client;

import checkersonline.DataPacket;
import checkersonline.GameController.D;
import checkersonline.ReceiveThread;
import checkersonline.SendThread;
import checkersonline.Space.Piece;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author bqb5176
 */
public class ClientController extends Thread {
    private boolean running;
    
    private String username = "";
    private String opponent = "";
    
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
        move.setUsername(username);
        send.sendPacket(move);
    }
    
    public String getUsername() {
        return username;
    }
    
    public void saveGameToFile(String winner) {
        try {
            String path = System.getProperty("user.home");
            
            File folder = new File(path + File.separator + "Documents");
            
            if (!folder.exists()) {
                folder.mkdir();
            }
            
            File saveFile = new File(path + File.separator + "Documents" + File.separator + "games.txt");
            FileWriter fw;
            BufferedWriter bw;

            if (!saveFile.exists()) {
                saveFile.createNewFile();
                
                fw = new FileWriter(saveFile, true);
                bw = new BufferedWriter(fw);
                
                bw.write("-- CHECKERS ONLINE GAMES --\n");
            } else {
                fw = new FileWriter(saveFile, true);
                bw = new BufferedWriter(fw);
            }
            
            String red, black;
            
            if (me == Piece.RED) {
                red = username;
                black = opponent;
            } else {
                red = opponent;
                black = username;
            }
            
            bw.write("RED: " + red + " BLACK: " + black + " WINNER: " + winner + "\n");
            bw.close();
        } catch (IOException ex) {
            System.out.println("Could not save file.");
        }
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
            
            receive = new ReceiveThread(socket) {
                @Override
                public void onReceive(DataPacket packet) {
                    ClientController.this.onReceive(packet);
                }
            };
            
            receive.start();
            
            send = new SendThread(socket);
            send.start();
        } catch (IOException ex) {
            System.out.println("Socket could not be initialized.");
            running = false;
        }
        
        System.out.println("Starting game...");
        
        // Send username
        DataPacket username = new DataPacket();
        username.setStatus(DataPacket.UPDATE);
        username.setUsername(this.username);
        send.sendPacket(username);
    }
    
    public void onReceive(DataPacket packet) {
        switch (packet.getStatus()) {
            case DataPacket.NEED_MOVE: // It's my turn
            case DataPacket.BAD_MOVE:  // or my move was bad.
                if (this.eventHandler != null) {
                    this.eventHandler.onMyTurn(packet.getStatus() == DataPacket.BAD_MOVE);
                }
                break;
            case DataPacket.UPDATE: // Update (board, opponent, color) received.
                if (this.me == Piece.NONE && packet.getColor() != Piece.NONE) {
                    this.me = packet.getColor();
                    
                    if (this.eventHandler != null) {
                        this.eventHandler.onColorAssigned(me);
                    }
                }

                if (this.opponent.equals("")) {
                    if (me == Piece.BLACK) {
                        opponent = packet.getRedUsername();
                    } else {
                        opponent = packet.getBlackUsername();
                    }

                    if (opponent == null) {
                        opponent = "";
                    }

                    if (this.eventHandler != null) {
                        this.eventHandler.onOpponentDiscovered(opponent);
                    }
                }
                
                if (this.eventHandler != null && packet.getBoard() != null) {
                    this.eventHandler.onNewBoard(packet.getBoard());
                }
                break;
            case DataPacket.GAME_OVER: // Game is over and there is a winner.
                if (this.eventHandler != null) {
                    this.eventHandler.onGameOver(packet.getWinner());
                }

                this.saveGameToFile(packet.getWinner().toString());

                quit();
                break;
            default:
                break;
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
