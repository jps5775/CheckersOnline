/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline;

/**
 *
 * @author JoeSema
 */
public class Board {
    Space[][] spaces = new Space[8][8];
    
    public Board() {
        /* Initialize an empty board. */
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                spaces[x][y] = new Space();
            }
        }
        
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
        if (x < 0 || x > 8 || y < 0 || x > 8) {
            return null;
        }
        
        return spaces[x][y];  
    }
    
    public void setSpace(int x, int y, Space.Piece piece){
        if (x < 0 || x > 8 || y < 0 || x > 8) {   // This should never happen
            throw new IllegalArgumentException(); // so throw an exception to
        }                                         // signify a glitch.
        
        spaces[x][y].setPiece(piece);
    }
    
    public String printBoard(){
        String board = "    0  1  2  3  4  5  6  7 \n"; // X coordinates
        
        /*
        Creates representation of the board in a String. Each space looks like
        this: [ ], [B], [R] for empty, black, and red.
        */
        for (int y = 0; y < 8; y++) {       // For each row...
            
            board += " " + Integer.toString(y) + " ";  // Output Y coordinate
            
            for (int x = 0; x < 8; x++) {   // For each space in the row...
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
    
    public int getNumRed(){
        int num = 0;
        
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (spaces[x][y].getPiece() == Space.Piece.RED) {
                    num++;
                }
            }
        }
        
        return num;
    }
    
    public int getNumBlack(){
        int num = 0;
        
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (spaces[x][y].getPiece() == Space.Piece.BLACK) {
                    num++;
                }
            }
        }
        
        return num;
    }    
}
