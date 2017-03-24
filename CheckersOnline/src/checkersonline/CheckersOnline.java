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
public class CheckersOnline {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GameController cntl = new GameController();
        Board board = cntl.getBoard();
        
        cntl.movePiece(1, 2, GameController.D.DR);
        cntl.movePiece(4, 5, GameController.D.UL);
        cntl.movePiece(2, 3, GameController.D.DR);
        cntl.movePiece(5, 6, GameController.D.UL);
        
        System.out.print(board.printBoard());
        
        System.out.println("Red: " + board.getNumRed());
        System.out.println("Black: " + board.getNumBlack());
    }
    
}
