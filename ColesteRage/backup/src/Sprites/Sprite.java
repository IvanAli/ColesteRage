/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sprites;

import Game.GamePanel;
import GameStates.Level1State;
import Images.ImagesLoader;
import Images.ImagesPlayer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author Ivan
 */
public abstract class Sprite {

    
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;
    
    protected double xdest;
    protected double ydest;
    
    protected boolean up;
    protected boolean down;
    protected boolean left;
    protected boolean right;
    
    protected int currentRail;
    
    protected int width;
    protected int height;
    
    protected int cwidth;
    protected int cheight;
    
    private static int tamanoDefecto = 15;
    protected String name;
    
    private ImagesLoader imsLoader;
    private ImagesPlayer animation;
    
    private BufferedImage im;
    protected boolean loopImagen = false;
    
    
    // new stuff that hopefully replaces the old stuff
    protected final int UPPER_RAIL = 0;
    protected final int MIDDLE_RAIL = 1;
    protected final int LOWER_RAIL = 2;
    
    protected final int[] rails = {444, 552, 660}; // possibly to create a Rail class
    
    public Sprite(ImagesLoader imsLoader) {
        this.imsLoader = imsLoader;
        
    }
    
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
    public void setImage(String nom){
        name = nom;
        im = imsLoader.getImagen(name);
        if(im == null){
            System.out.println("Imagen inexistente");
            width = tamanoDefecto;
            height = tamanoDefecto;
        }
        else{
            width = im.getWidth();
            height = im.getHeight();
        }
        loopImagen = false;
    }
    
    public void animarSprite(String nom, long periodo, double duracion){
        if(imsLoader.numImagenes(nom) > 1){
            animation = new ImagesPlayer(nom, periodo, duracion, true, imsLoader);
            loopImagen = true;
        }
    }
    
    public void switchRail(int rail) {
        
    }
    
    private void checkRailChange() {
        
        ydest = y + dy;
        if(up) {
            if(currentRail == MIDDLE_RAIL) {
                if(ydest < rails[UPPER_RAIL]) {
                    currentRail = UPPER_RAIL;
                    y = rails[currentRail];
                    dy = 0;
                    up = false;
                }
            }
            if(currentRail == LOWER_RAIL) {
                if(ydest < rails[MIDDLE_RAIL]) {
                    currentRail = MIDDLE_RAIL;
                    y = rails[currentRail];
                    dy = 0;
                    up = false;
                }
            }
        }
        else if(down) {
            if(currentRail == MIDDLE_RAIL) {
                if(ydest > rails[LOWER_RAIL]) {
                    currentRail = LOWER_RAIL;
                    y = rails[currentRail];
                    dy = 0;
                    down = false;
                }
            }
            if(currentRail == UPPER_RAIL) {
                if(ydest > rails[MIDDLE_RAIL]) {
                    currentRail = MIDDLE_RAIL;
                    y = rails[currentRail];
                    dy = 0;
                    down = false;
                }
            }
        }
        
    }
    
    public void update() {
        if(loopImagen) animation.reproducir();
        checkRailChange();
    }
    
    
    public void actualizarSprite(){
        if(loopImagen){
            animation.reproducir();
        }
        x += dx;
        y += dy;

    }
    
    
    public void draw(Graphics2D g) {
        
        g.fillRect((int)x, (int)y, (int)(x - width / 2), (int)(y - height/2));
        
    }
    
    public void drawSprite(Graphics2D g){
        if(im == null){
            System.out.println("Imagen inexistente");
            g.setColor(Color.yellow);
            g.fillOval((int)x, (int)y, tamanoDefecto, tamanoDefecto);
        }
        else if(loopImagen)
            g.drawImage(animation.imagenActual(), (int)x, (int)y, null);
    }
    
    public void drawImagen(Graphics2D g, String name){
        if(im != null)
            g.drawImage(imsLoader.getImagen(name), 0, 0, null);
    }
    
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }
    
    public String getName(){
        return name;
    }
}
