/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline;

import checkersonline.GameController.D;
import checkersonline.Space.Piece;

/**
 *
 * @author bqb5176
 */
public class DataPacket {
    public static final int UPDATE = 0;
    public static final int BAD_MOVE = 1;
    public static final int NEED_MOVE = 2;
    public static final int IS_MOVE = 3;
    public static final int GAME_OVER = 4;
    
    private Board board;         // The playing board
    private int x;               // The x position of the piece to move
    private int y;               // The y position of the piece to move
    private D direction = D.DL;  // The direction in which to move the piece
    private Piece turn = Piece.NONE;   // Whose turn it is.
    private Piece winner = Piece.NONE; // Who the winner is.
    private Piece color = Piece.NONE;  // What color is the client.
    private int status = UPDATE;    // Status code.
    private String username;           // Username submitted by client.
    private String red;                // RED's username
    private String black;              // BLACK's username
    
    public DataPacket() {
        
    }
    
    /**
     * Encode this DataPacket into a String that can be decoded into a
     * DataPacket object by DataPacket.decode()
     * @return Encoded String form of this DataPacket
     */
    public String encode() {
        String x = encodeValue("x", this.x);
        String y = encodeValue("y", this.y);
        String d = encodeValue("d", this.direction);
        String turn = encodeValue("turn", this.turn);
        String winner = encodeValue("winner", this.winner);
        String color = encodeValue("color", this.color);
        String status = encodeValue("status", this.status);
        String board = "";
        
        if (this.board != null) {
            board = encodeValue("board", this.board.encode());
        }
        
        return x + y + d + board + turn + winner + color + status
               + encodeValue("username", this.username)
               + encodeValue("red", this.red)
               + encodeValue("black", this.black);
    }
    
    public static DataPacket decode(String string) {
        DataPacket packet = new DataPacket();
        
        packet.setX(Integer.parseInt(getValueAsString(string, "x")));
        packet.setY(Integer.parseInt(getValueAsString(string, "y")));
        packet.setDirection(GameController.D.valueOf(getValueAsString(string, "d")));
        packet.setTurn(Piece.valueOf(getValueAsString(string, "turn")));
        packet.setWinner(Piece.valueOf(getValueAsString(string, "winner")));
        packet.setColor(Piece.valueOf(getValueAsString(string, "color")));
        packet.setBoard(Board.decode(getValueAsString(string, "board")));
        packet.setStatus(Integer.parseInt(getValueAsString(string, "status")));
        packet.setUsername(getValueAsString(string, "username"));
        packet.setRedUsername(getValueAsString(string, "red"));
        packet.setBlackUsername(getValueAsString(string, "black"));
        
        return packet;
    }
    
    /**
     * Encode the specified value with the specified name to retrieve later with
     * getValueAsString()
     * @param name The name used to retrieve the value later.
     * @param value The value to encode
     * @return name and value pair encoded into a String
     */
    private static String encodeValue(String name, Object value) {
        if (value == null) value = "";
        return "<" + name + ">" + value.toString() + "</" + name + ">";
    }
    
    /**
     * Retrieve the value with the given name in the encoded String as a String.
     * @param encodedString the DataPacket encoded as a String
     * @param name The name of the value to retrieve
     * @return The value of name as a String
     */
    private static String getValueAsString(String encodedString, String name) {
        int start = encodedString.indexOf("<" + name +">") + name.length() + 2;
        int end = encodedString.indexOf("</" + name +">");
        
        if (start < 0 || end < 0) {
            return null;
        }
        
        return encodedString.substring(start, end);
    }

    /**
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @param board the board to set
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the direction
     */
    public GameController.D getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(GameController.D direction) {
        this.direction = direction;
    }
    
    public Piece getTurn() {
        return this.turn;
    }
    
    public void setTurn(Piece piece) {
        this.turn = piece;
    }
    
    public Piece getWinner() {
        return this.winner;
    }
    
    public void setWinner(Piece piece) {
        this.winner = piece;
    }
    
    public Piece getColor() {
        return this.color;
    }
    
    public void setColor(Piece piece) {
        this.color = piece;
    }
    
    public int getStatus() {
        return this.status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getRedUsername() {
        return this.red;
    }
    
    public void setRedUsername(String username) {
        this.red = username;
    }
    
    public String getBlackUsername() {
        return this.black;
    }
    
    public void setBlackUsername(String username) {
        this.black = username;
    }
}
