/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline;

/**
 * Controls a checkers board.
 * @author JoeSema
 */
public class GameController {
    
    /**
     * This enumeration defines all the directions that a piece can move in.
     * UL - up left
     * UR - up right
     * DL - down left
     * DR - down right
     */
    public enum D {
        UL, UR, DL, DR
    }
    
    private Board gameBoard;  // The game board.
    
    public GameController(){
        this.gameBoard = new Board();
    }
    
    /**
     * @return The winner of the game as either Space.Piece.RED or BLACK, or
     * NONE if no one has won yet.
     */
    public Space.Piece checkWinner(){
        Space.Piece winner = Space.Piece.NONE;
        
        if (gameBoard.getNumBlack() == 0) {
            winner = Space.Piece.RED;
        }
        else if (gameBoard.getNumRed() == 0) {
            winner = Space.Piece.BLACK;
        }
        
        return winner;
    }
    
    /**
     * Tries to move the piece at (x,y) on the board in the indicated direction.
     * @param x X coordinate of the piece to move
     * @param y Y coordinate of the piece to move
     * @param direction Direction to move the piece in
     * @return true if the move was valid, false otherwise.
     */
    public boolean movePiece(int x, int y, D direction){
        boolean success = false;
        Space current = gameBoard.getSpace(x, y);
        Space desired = null;
        
        /*
        Figure out which space we want to go to based on direction. (0,0) is the
        top left corner of the board. So remember, going up the board is
        actually going in the minus direction for Y.
        */
        switch (direction) {
            case UL:
                desired = gameBoard.getSpace(x - 1, y - 1);
                break;
            case UR:
                desired = gameBoard.getSpace(x + 1, y - 1);
                break;
            case DL:
                desired = gameBoard.getSpace(x - 1, y + 1);
                break;
            case DR:
                desired = gameBoard.getSpace(x + 1, y + 1);
                break;
        }
        
        if (desired == null) { // Can't move this way because it's off the board
            success = false;
        }
        else {
            if (desired.getPiece() == current.getPiece()) { // Bro, same team.
                success = false;
            }
            else if (desired.getPiece() == Space.Piece.NONE) { // It's open
                desired.setPiece(current.getPiece()); // Move piece to new space
                current.setPiece(Space.Piece.NONE); // Get rid of piece in old
                success = true;                     // Valid move!
            }
            else { // Only other option is that it's the other player's piece.
                Space jump = null;  // We'll try to jump and take the piece.
                
                switch (direction) {
                    case UL:
                        jump = gameBoard.getSpace(x - 2, y - 2);
                        break;
                    case UR:
                        jump = gameBoard.getSpace(x + 2, y - 2);
                        break;
                    case DL:
                        jump = gameBoard.getSpace(x - 2, y + 2);
                        break;
                    case DR:
                        jump = gameBoard.getSpace(x + 2, y + 2);
                        break;
                }
                
                /* Check if the space is open */
                if (jump != null && jump.getPiece() == Space.Piece.NONE) {
                    jump.setPiece(current.getPiece());  // Move our piece
                    current.setPiece(Space.Piece.NONE); // Remove it from old
                    desired.setPiece(Space.Piece.NONE); // Remove jumped piece
                    success = true; // Valid move!
                } else { // It's not open
                    success = false;
                }
            }
        }
        
        return success;
    }
    
    /**
     * @return The game board.
     */
    public Board getBoard() {
        return gameBoard;
    }
}
