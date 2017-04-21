
import checkersonline.Board;
import checkersonline.DataPacket;
import checkersonline.Space;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ben
 */
public class DataPacketTest {
    public static void main(String[] args) {
        DataPacket d = new DataPacket();
        Board a = new Board();
        
        a.setSpace(2, 4, Space.Piece.RED);
        
        d.setX(9873498);
        d.setBoard(a);
        
        System.out.println(d.encode());
        
        DataPacket b = DataPacket.decode(d.encode());
        
        System.out.println(b.encode());
        System.out.println(b.getBoard().printBoard());
    }
}
