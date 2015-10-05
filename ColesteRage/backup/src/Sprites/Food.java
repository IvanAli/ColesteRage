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
    
    public Food(ImagesLoader imsLoader, String name, int rail) {
        super(imsLoader, name, rail);
    }
    
    public Food(ImagesLoader imsLoader, String name, int rail, int value) {
        super(imsLoader, name, rail);
        this.value = value;
        setDimensions(name);
        
    }
    
    /*
    public Food(Vector pos, String nom, int r, int vel, ImagesLoader ci, CargarComida cc, Bodoque b){
        value = 0;
    }
    */
    public int getValue() { return value; }
    public void setValue(int i) { value = i; }
    
    
    
    
}
