/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sprites;


import Images.ImagesLoader;
import Sprites.Sprite;

/**
 *
 * @author Ivan
 */
public class Power extends Dish {
    
    private int type;
    
    public static final int NOPOWER = 0;
    public static final int FREEZEPOWER = 1;
    public static final int DOUBLESCOREPOWER = 2;
    public static final int DOUBLESPEEDPOWER = 3;
    public static final int HALFSPEEDPOWER = 4;
    
    
    public Power(String name, int rail) {
       super(name, rail);
    }
    
    public Power(String name, int rail, int type) {
       super(name, rail);
       this.type = type;
    }
    
    public int getType() { return type; }
    public void setType(int i) { type = i; }
   
   
}
