/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sprites;


import Game.GamePanel;
import Images.ImagesLoader;
import Sprites.Sprite;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author Ivan
 */
public class Food extends Dish {
    private int value;
    
    public Food(String name, int rail) {
        super(name, rail);
    }
    
    public Food(String name, int rail, int value) {
        super(name, rail);
        this.value = value;
        setDimensions(name);
        
    }
    
    public int getValue() { return value; }
    public void setValue(int i) { value = i; }
    
    
    
    
}
