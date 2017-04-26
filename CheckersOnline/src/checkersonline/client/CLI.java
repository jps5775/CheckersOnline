/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline.client;

import checkersonline.Board;
import checkersonline.GameController;
import checkersonline.GameController.D;
import checkersonline.Space;
import java.util.Scanner;

/**
 *
 * @author ben
 */
public class CLI implements ClientEventHandler {
    private ClientController cntl;
    
    public CLI(ClientController cntl) {
        this.cntl = cntl;
        this.cntl.setEventHandler(this);
    }

    @Override
    public void onColorAssigned(Space.Piece color) {
        System.out.println("You are playing as " + color + "!");
    }

    @Override
    public void onMyTurn(boolean badMove) {
        Scanner scanner = new Scanner(System.in);
        boolean success = true;
        
        if (!badMove) {
            System.out.println("It's your turn!");
        } else {
            System.out.println("That move was invalid. Try again.");
        }
        
        do {
            success = true;
            System.out.print("Enter move (x y direction): ");

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

            D d = null;  // Create an instance of D for direction

            switch (in_d) {  // Figure out the input direction
                case "ul":
                    d = D.UL;
                    break;
                case "ur":
                    d = D.UR;
                    break;
                case "dl":
                    d = D.DL;
                    break;
                case "dr":
                    d = D.DR;
                    break;
                default:
                    success = false; // Invalid direction.
            }

            if (success) {
                cntl.sendMove(x, y, d);
            } else {
                System.out.println("Try again.");
            }
        } while (!success);
    }

    @Override
    public void onGameOver(Space.Piece winner) {
        System.out.println("GAME OVER. The winner is " + winner + "!");
    }

    @Override
    public void onNewBoard(Board board) {
        System.out.println("Latest board:");
        System.out.println(board.printBoard());
    }
}
