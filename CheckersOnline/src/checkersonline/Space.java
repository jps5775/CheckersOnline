/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline;

/**
 * A space on the game board that can have no piece (NONE), be occupied by a
 * black piece (BLACK), or be occupied by a red piece (RED).
 * @author JoeSema
 */
public class Space {
    
    /**
     * This enumeration defines the three different states that a Space can be
     * in.
     */
    public enum Piece {
        NONE, BLACK, RED
    }
    
    /**
     * The state of this space. An instance of the enum defined above
     */
    private Piece piece;
    
    public Space(Piece piece) {
        this.piece = piece;
    }
    
    public Space() {
        this(Piece.NONE);
    }
    
    public Piece getPiece(){
        return piece;
    }
    
    public void setPiece(Piece piece){
        this.piece = piece;
    }
}
