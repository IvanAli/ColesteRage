/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sprites;

import Images.ImagesLoader;
import StateMisc.Camera;

import java.awt.Graphics2D;

/**
 *
 * @author Home
 */
public class SpriteAnimation {
    private double x;
    private double y;
    private int width;
    private int height;
    private Animation animation;
    private String name;
    
    public SpriteAnimation(double x, double y, String name, long delay) {
    	this.x = x;
    	this.y = y;
        this.name = name;
        
        animation = new Animation();
        animation.setFrames(ImagesLoader.getFrames(name));
        animation.setDelay(delay);
        
        width = ImagesLoader.getImageAt(name, animation.getFrame()).getWidth();
        height = ImagesLoader.getImageAt(name, animation.getFrame()).getHeight();
    }
    
    public SpriteAnimation(String name, long delay) {
        this.name = name;
        
        animation = new Animation();
        animation.setFrames(ImagesLoader.getFrames(name));
        animation.setDelay(delay);
        
        width = ImagesLoader.getImageAt(name, animation.getFrame()).getWidth();
        height = ImagesLoader.getImageAt(name, animation.getFrame()).getHeight();
    }
    
    public SpriteAnimation(String name) {
        this.name = name;
        
        animation = new Animation();
        animation.setFrames(ImagesLoader.getFrames(name));
        
        width = ImagesLoader.getImageAt(name, animation.getFrame()).getWidth();
        height = ImagesLoader.getImageAt(name, animation.getFrame()).getHeight();
    }
    
    public SpriteAnimation(double x, double y, String name, long delay, Camera camera) {
    	this.x = x + camera.getx();
    	this.y = y + camera.gety();;
        this.name = name;
        
        animation = new Animation();
        animation.setFrames(ImagesLoader.getFrames(name));
        animation.setDelay(delay);
        
        width = ImagesLoader.getImageAt(name, animation.getFrame()).getWidth();
        height = ImagesLoader.getImageAt(name, animation.getFrame()).getHeight();
    }
    
    public void setDelay(long d) {
    	animation.setDelay(d);
    }
    
    public void update() {
        animation.update();
    }
    
    public void draw(Graphics2D g) {
        g.drawImage(
        		ImagesLoader.getImageAt(name, animation.getFrame()), 
        		(int)(x - width / 2), 
        		(int)(y - height / 2), 
        		null
		);
    }
    
    public void setPosition(double x, double y) {
    	this.x = x;
    	this.y = y;
    }
    
    public boolean hasPlayedOnce() { return animation.hasPlayedOnce(); }
}
