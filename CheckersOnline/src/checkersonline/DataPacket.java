/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline;

import checkersonline.GameController.D;

/**
 *
 * @author bqb5176
 */
public class DataPacket {
    private Board board;         // The playing board
    private int x;               // The x position of the piece to move
    private int y;               // The y position of the piece to move
    private D direction = D.DL;  // The direction in which to move the piece
    private String status = "";  // General status condition.
    
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
        String status = encodeValue("status", this.status);
        String d = encodeValue("d", this.direction);
        String board = encodeValue("board", this.board.encode());
        
        return x + y + status + d + board;
    }
    
    public static DataPacket decode(String string) {
        DataPacket packet = new DataPacket();
        
        packet.setX(Integer.parseInt(getValueAsString(string, "x")));
        packet.setY(Integer.parseInt(getValueAsString(string, "y")));
        packet.setStatus(getValueAsString(string, "status"));
        packet.setDirection(GameController.D.valueOf(getValueAsString(string, "d")));
        packet.setBoard(Board.decode(getValueAsString(string, "board")));
        
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
        if (value == null) value = "null";
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

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
