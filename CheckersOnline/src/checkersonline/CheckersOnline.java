/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline;

import java.util.Scanner;

/**
 * Plays a simplified checkers game in the command line.
 * @author bqb5176
 */
public class CheckersOnline {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GameController cntl = new GameController();  // Initialize our controller
        Board board = cntl.getBoard();   // Get a reference to the board for convenience.
        Space.Piece turn = Space.Piece.RED;  // We'll let RED start
        Scanner scanner = new Scanner(System.in); // Gets input
        
        /*
        The game will output the board and the number of pieces that each player
        has left at the beginning of each turn. It will then prompt "RED:" or
        "BLACK:" depending on who's turn it is. The player should input like
        this: "x y d" (note the spaces) where x is the x coordinate of the piece
        the player wants to move, y is the y coordinate, and d is one of the
        following: ul for up-left, ur for up-right, dl for down-left, and
        dr for up-right. So an example would look like this: "1 2 dr".
        */
        
        while (cntl.checkWinner() == Space.Piece.NONE) {  // While there is no winner
            System.out.print(board.printBoard());                  // Output the board
            System.out.println("Red: " + board.getNumRed());       // Output red's score
            System.out.println("Black: " + board.getNumBlack());   // Output black's score
            
            boolean success = true;  // Flag that indicates if the move was valid.
            
            do {
                success = true;  // Start with a clean slate every turn
                System.out.print(turn == Space.Piece.RED ? "RED: " : "BLACK: ");  // Output who's turn it is.

                String in_x = scanner.next();  // get x input
                String in_y = scanner.next();  // get y input
                String in_d = scanner.next();  // get d input

                int x = 0; // will be the x input converted to int
                int y = 0; // ^sames but y^
                        
                try {
                    x = Integer.parseInt(in_x);  // parse x
                    y = Integer.parseInt(in_y);  // and y
                } catch(NumberFormatException e) {  // User didn't enter numbers.
                    success = false;
                }
                
                GameController.D d = null;  // Create an instance of D for direction

                switch (in_d) {  // Figure out the input direction
                    case "ul":
                        d = GameController.D.UL;
                        break;
                    case "ur":
                        d = GameController.D.UR;
                        break;
                    case "dl":
                        d = GameController.D.DL;
                        break;
                    case "dr":
                        d = GameController.D.DR;
                        break;
                    default:
                        success = false; // Invalid direction.
                }
                
                if (success) { // If the user didn't screw up anything else.
                    success = cntl.movePiece(x, y, d);  // Try to do the move and store the result.
                }
            } while(!success); // User screwed up, try it again...
            
            turn = turn == Space.Piece.RED ? Space.Piece.BLACK : Space.Piece.RED; // Next user's turn.
        }
        
        String winner = cntl.checkWinner() == Space.Piece.RED ? "RED" : "BLACK";
        System.out.println("The winner is " + winner + "!");
    }
}
