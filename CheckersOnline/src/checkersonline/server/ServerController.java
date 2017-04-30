/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline.server;

import checkersonline.DataPacket;
import checkersonline.GameController;
import checkersonline.GameController.D;
import checkersonline.ReceiveThread;
import checkersonline.SendThread;
import checkersonline.Space.Piece;
import java.net.Socket;

/**
 *
 * @author bqb5176
 */
public class ServerController extends Thread {
    private String red = "";
    private String black = "";
    
    private GameController gameController;
    private Socket redClient;
    private Socket blackClient;
    
    private ReceiveThread redReceive;
    private ReceiveThread blackReceive;
    
    private SendThread redSend;
    private SendThread blackSend;
    
    public ServerController(boolean quick) {
        gameController = new GameController(quick);
    }
    
    @Override
    public void run() {
        GetConnectionsThread getConnections = new GetConnectionsThread("localhost", 5555);
        getConnections.start();
        
        redClient = getConnections.getRed();
        blackClient = getConnections.getBlack();
        
        redReceive = new ReceiveThread(redClient) {
            @Override
            public void onReceive(DataPacket packet) {
                ServerController.this.onReceive(packet, Piece.RED);
            }
        };
        
        blackReceive = new ReceiveThread(blackClient) {
            @Override
            public void onReceive(DataPacket packet) {
                ServerController.this.onReceive(packet, Piece.BLACK);
            }
        };
        
        redSend = new SendThread(redClient);
        blackSend = new SendThread(blackClient);
        
        redReceive.start();
        blackReceive.start();
        
        redSend.start();
        blackSend.start();
        
        this.sendUpdate(redSend);
        this.sendUpdate(blackSend);
        
        this.sendMoveRequest(redSend, false);
    }
    
    public void sendMoveRequest(SendThread player, boolean badMove) {
        DataPacket moveRequest = new DataPacket();
        
        if (badMove) {
            moveRequest.setStatus(DataPacket.BAD_MOVE);
        } else {
            moveRequest.setStatus(DataPacket.NEED_MOVE);
        }
        
        player.sendPacket(moveRequest);
    }
    
    public void sendUpdate(SendThread player) {
        DataPacket update = new DataPacket();
        update.setStatus(DataPacket.UPDATE);
        update.setBoard(gameController.getBoard());
        update.setRedUsername(this.red);
        update.setBlackUsername(this.black);
        
        if (player == redSend) {
            update.setColor(Piece.RED);
        } else {
            update.setColor(Piece.BLACK);
        }
        
        player.sendPacket(update);
    }
    
    public void sendGameOver(Piece winner) {
        DataPacket gameOver = new DataPacket();
        gameOver.setStatus(DataPacket.GAME_OVER);
        gameOver.setWinner(winner);
        
        redSend.sendPacket(gameOver);
        blackSend.sendPacket(gameOver);
    }
    
    public void onReceive(DataPacket packet, Piece player) {
        SendThread playerSend;   // The socket this packet came from
        SendThread opponentSend; // The opponent's socket.
        
        if (player == Piece.RED) {
            playerSend = redSend;
            opponentSend = blackSend;
        } else {
            playerSend = blackSend;
            opponentSend = redSend;
        }
        
        switch (packet.getStatus()) {
            case DataPacket.IS_MOVE:
                if (gameController.getTurn() == player) { // Make sure it's their turn.
                    boolean success = false;

                    int x = packet.getX();
                    int y = packet.getY();
                    D direction = packet.getDirection();

                    success = gameController.movePiece(x, y, direction);

                    if (success) {
                        Piece winner = gameController.checkWinner();
                        
                        if (winner != Piece.NONE) {
                            this.sendGameOver(winner);
                        } else {
                            this.sendMoveRequest(opponentSend, false);

                            this.sendUpdate(playerSend);
                            this.sendUpdate(opponentSend);
                        }
                    } else {
                        this.sendMoveRequest(playerSend, true);
                    }
                }
                
                break;
            case DataPacket.UPDATE:
                if (player == Piece.RED && red.equals("")) {
                    red = packet.getUsername();
                }
                
                if (player == Piece.BLACK && black.equals("")) {
                    black = packet.getUsername();
                }
                
                this.sendUpdate(opponentSend); // Tell the other guy the opponent username
                break;
        }
    }
    
    public static void main(String[] args) {
        ServerController cntl = new ServerController(false);
        cntl.start();
    }
}
