/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline;

/**
 *
 * @author bqb5176
 */
public class DataPacket {
    private Board board;
    private int x;
    private int y;
    private GameController.D direction;
    private String status;
    
    public DataPacket() {
        
    }
    
    public String encode() {
        String string = "";
        
        String x = "<x>" + this.x + "</x>";
        String y = "<y>" + this.y + "</y>";
        String status = "<status>" + this.status + "</status>";
        
        String d = "";
        
        switch(direction) {
            case UL:
                d = "<d>ul</d>";
                break;
            case UR:
                d = "<d>ur</d>";
                break;
            case DL:
                d = "<d>dl</d>";
                break;
            case DR:
                d = "<d>dr</d>";
                break;
        }
        
        string = x + y + status + d;
        
        return string;
    }
    
    public static DataPacket decode(String string) {
        DataPacket packet = new DataPacket();
        
        int start = 0;
        int end = 0;
        
        start = string.indexOf("<x>") + 3;
        end = string.indexOf("</x>");
        packet.setX(Integer.parseInt(string.substring(start, end)));
        
        start = string.indexOf("<y>") + 3;
        end = string.indexOf("</y>");
        packet.setY(Integer.parseInt(string.substring(start, end)));
        
        start = string.indexOf("<status>") + 8;
        end = string.indexOf("</status>");
        packet.setStatus(string.substring(start, end));
        
        //start = string.indexOf("<d>") + 3; todo
        //end = string.indexOf("</d>");
        //packet.setX(Integer.parseInt(string.substring(start, end)));
        
        
        
        return packet;
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
