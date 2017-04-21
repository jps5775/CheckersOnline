/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline;

import checkersonline.Space.Piece;

/**
 * Model that represents a checker board and the contents of it's spaces.
 * @author JoeSema
 */
public class Board {
    private static final int X = 8;
    private static final int Y = 8;
    
    Space[][] spaces = new Space[X][Y];
    
    public Board() {
        this(false);
    }
    
    public Board(boolean empty) {
        /* Initialize an empty board. */
        for (int x = 0; x < X; x++) {
            for (int y = 0; y < Y; y++) {
                spaces[x][y] = new Space();
            }
        }
        
        if (!empty) {
            /* Place the red pieces in the top three rows of the board */
            for (int y = 0; y < 3; y++) {
                for (int x = 1 - y % 2; x < 8; x += 2) {
                    spaces[x][y].setPiece(Space.Piece.RED);
                }
            }

            /* Place the black pieces in the bottom three rows of the board */
            for (int y = 5; y < 8; y++) {
                for (int x = 1 - y % 2; x < 8; x += 2) {
                    spaces[x][y].setPiece(Space.Piece.BLACK);
                }
            }
        }
    }
    
    /**
     * Get the space at (x,y) on the board. If (x,y) is not on the board, this
     * method returns null.
     * @param x
     * @param y
     * @return 
     */
    public Space getSpace(int x, int y){
        /*
        We need to make sure that (x,y) is inside the board. If it isn't, return
        null to signify that it isn't.
        */
        if (x < 0 || x > X || y < 0 || y > Y) {
            return null;
        }
        
        return spaces[x][y];  
    }
    
    /**
     * Sets the space at (x,y) to the indicated piece. If (x,y) is not on the
     * board, this method will throw an IllegalArgumentException to indicate
     * that a glitch has occured.
     * @param x X coordinate of the space to change.
     * @param y Y coordinate of the space to change.
     * @param piece The piece to place in the space.
     */
    public void setSpace(int x, int y, Space.Piece piece){
        if (x < 0 || x > X || y < 0 || y > Y) {   // This should never happen
            throw new IllegalArgumentException(); // so throw an exception to
        }                                         // signify a glitch.
        
        spaces[x][y].setPiece(piece);
    }
    
    /**
     * @return A text representation of the game board and it's pieces.
     */
    public String printBoard(){
        String board = "    0  1  2  3  4  5  6  7 \n"; // X coordinates
        
        /*
        Creates representation of the board in a String. Each space looks like
        this: [ ], [B], [R] for empty, black, and red.
        */
        for (int y = 0; y < Y; y++) {       // For each row...
            
            board += " " + Integer.toString(y) + " ";  // Output Y coordinate
            
            for (int x = 0; x < X; x++) {   // For each space in the row...
                String space = "[";         // Starts a new space
                
                switch (spaces[x][y].getPiece()) {  // What's in the space?
                    case NONE:
                        space += " ";               // Space is empty "[ "
                        break;
                    case BLACK:
                        space += "B";               // Space is black "[B"
                        break;
                    case RED:
                        space += "R";               // Space is red "[R"
                        break;
                }
                
                space += "]";   // Finish the space "[ ]" or "[B]" or "[R]"
                
                board += space; // Add the space to the board.
            }
            
            board += "\n";      // End of the row, go down to the next line.
        }
        
        return board;
    }
    
    /**
     * @return The number of red pieces left on the board.
     */
    public int getNumRed(){
        int num = 0;
        
        for (int y = 0; y < Y; y++) {
            for (int x = 0; x < X; x++) {
                if (spaces[x][y].getPiece() == Space.Piece.RED) {
                    num++;
                }
            }
        }
        
        return num;
    }
    
    /**
     * @return The number of black pieces left on the board.
     */
    public int getNumBlack(){
        int num = 0;
        
        for (int y = 0; y < Y; y++) {
            for (int x = 0; x < X; x++) {
                if (spaces[x][y].getPiece() == Space.Piece.BLACK) {
                    num++;
                }
            }
        }
        
        return num;
    }
    
    /**
     * This function will encode this Board into a string that can be decoded
     * by Board.decode()
     * @return this Board encoded as a String
     */
    public String encode() {
        String board = "";
        
        for (int x = 0; x < X; x++) {
            for (int y = 0; y < Y; y++) {
                Piece piece = spaces[x][y].getPiece();
                
                if (piece != Piece.NONE) {
                    board += x + "," + y + "," + piece.name() + ";";
                }
            }
        }
        
        return board;
    }
    
    /**
     * Decodes a Board from a String
     * @param s
     * @return 
     */
    public static Board decode(String s) {
        Board board = new Board(true);
        int cursor = 0;
        
        try {
            while (cursor < s.length()) {
                int end = s.indexOf(",", cursor);
                int x = Integer.parseInt(s.substring(cursor, end));
                cursor = end + 1;

                end = s.indexOf(",", cursor);
                int y = Integer.parseInt(s.substring(cursor, end));
                cursor = end + 1;

                end = s.indexOf(";", cursor);
                Piece piece = Piece.valueOf(s.substring(cursor, end));
                cursor = end + 1;

                board.setSpace(x, y, piece);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Encoded string is not properly formatted.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Encoded string is not properly formatted.");
        }
        
        return board;
    }
}
