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
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        
        //while (redClient == null && running) {
        redClient = getConnections.getRed();
        //}
        
        //while (blackClient == null && running) {
        blackClient = getConnections.getBlack();
        //}
        
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
        red.setStatus(DataPacket.NEW_BOARD);
        red.setBoard(gameController.getBoard());
        red.setYou(Piece.RED);
        redSend.sendPacket(red);
        
        DataPacket black = new DataPacket();
        black.setStatus(DataPacket.NEW_BOARD);
        black.setBoard(gameController.getBoard());
        black.setYou(Piece.BLACK);
        blackSend.sendPacket(black);
        
        // Game loop
        
        while (running) {
            Piece turn = gameController.getTurn();
            
            // Ask client for move.
            
            DataPacket moveRequest = new DataPacket();
            moveRequest.setTurn(turn);
            moveRequest.setStatus(DataPacket.NEED_MOVE);
            moveRequest.setBoard(gameController.getBoard());
            
            switch (turn) {
                case RED:
                    moveRequest.setYou(Piece.RED);
                    redSend.sendPacket(moveRequest);
                    break;
                case BLACK:
                    moveRequest.setYou(Piece.BLACK);
                    blackSend.sendPacket(moveRequest);
                    break;
            }
            
            // Wait for response.
            
            DataPacket nextTurn = null;
            boolean success = false;
            
            do {
                switch (turn) {
                    case RED:
                        while (!redReceive.hasNewData()) {   // Just waiting...
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                                quit();
                            }
                        }
                        nextTurn = redReceive.getNextPacket();
                        break;
                    case BLACK:
                        while (!blackReceive.hasNewData()) { // waiting waiting waiting...
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                                quit();
                            }
                        }
                        nextTurn = blackReceive.getNextPacket();
                        break;
                }

                int x = nextTurn.getX();
                int y = nextTurn.getY();
                D direction = nextTurn.getDirection();

                success = gameController.movePiece(x, y, direction);
                
                if (!success) {
                    DataPacket requestNewMove = new DataPacket();
                    requestNewMove.setStatus(DataPacket.BAD_MOVE);
                    
                    switch (turn) {
                        case RED:
                            requestNewMove.setYou(Piece.RED);
                            redSend.sendPacket(requestNewMove);
                            break;
                        case BLACK:
                            requestNewMove.setYou(Piece.BLACK);
                            blackSend.sendPacket(requestNewMove);
                            break;
                    }
                }
            } while (!success);
            
            // Send updated board.
            DataPacket board = new DataPacket();
            board.setStatus(DataPacket.NEW_BOARD);
            board.setBoard(gameController.getBoard());
            
            board.setYou(Piece.RED);
            redSend.sendPacket(board);
            
            board.setYou(Piece.BLACK);
            blackSend.sendPacket(board);
            
            // Check winner
            if (gameController.checkWinner() != Piece.NONE) {
                // todo Tell both players the winner.
            }
        }
    }
    
    public static void main(String[] args) {
        ServerController cntl = new ServerController();
        cntl.start();
    }
}
