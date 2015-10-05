/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sprites;

/**
 *
 * @author Ivan
 */
public class Rail {
    
    private int y;
    private int spritey;
    
    public Rail(int y, Entity e) {
        this.y = y;
        spritey = y - e.getHeight() / 2;
    }
    
    public int gety() { return y; }
    public int getspritey() { return spritey; }
    public void sety(int i) { y = i; }
    public void setspritey(int i) { spritey = i; }
    
}
