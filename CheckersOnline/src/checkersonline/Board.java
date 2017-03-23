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
    
    
    
    public Space getSpace(int x, int y){
      return spaces[x][y];  
    }
    
    public void setSpace(int x, int y, int piece){
        
    }
    
    public void printBoard(){
        
    }
    
    public int getNumRed(){
        return 0;
    }
    
    public int getNumBlack(){
        return 0;
    }    
}
