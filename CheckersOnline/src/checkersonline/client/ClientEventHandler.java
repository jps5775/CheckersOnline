/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkersonline.client;

import checkersonline.Board;
import checkersonline.Space.Piece;

/**
 *
 * @author ben
 */
public interface ClientEventHandler {
    void onColorAssigned(Piece color);
    void onMyTurn(boolean badMove);
    void onGameOver(Piece winner);
    void onNewBoard(Board board);
}
