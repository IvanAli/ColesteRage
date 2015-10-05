/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sprites;
import Images.ImagesLoader;
/**
 *
 * @author Ivan
 */
public class Dish extends Entity {
    protected String name;
    
    public Dish(String n, int rail) {
        
        name = n;
        
        // set animation for one frame food
        setAnimation(n, -1);
        
        setDimensions(name);
        
        //dx = -3;
        currentRail = rail;
        
    }
    
    public boolean shouldRemove() {
        if(x + camera.getx() + width / 2 < 0) return true;
        return false;
    }
    
    public String getName() { return name; }
    public void scaleSpeed(double s) { dx *= s; }
}
