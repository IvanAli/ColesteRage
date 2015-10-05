/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sprites;

import Background.BackgroundManager;
import Images.ImagesLoader;
import Images.ImagesPlayer;
import StateMisc.Camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
/**
 *
 * @author Ivan
 */
public abstract class Entity {
    
    // positions
    protected double x;
    protected double y;
    protected double dx;
    protected double initSpeed;
    protected double dy;
    protected double xdest;
    protected double ydest;
    
    protected Camera camera;
    
    // size
    protected int width;
    protected int height;
    protected int cwidth;
    protected int cheight;
    
    // rail position
    protected static final int UPPER_RAIL = 0;
    protected static final int MIDDLE_RAIL = 1;
    protected static final int LOWER_RAIL = 2;
    protected static final int NUM_RAILS = 3;
    
    protected int currentRail;
    protected int nextRail;
    protected boolean moving;
    protected boolean changingUpperRail;
    protected boolean changingLowerRail;
    protected final int[] rails = {474, 582, 690}; // possibly create a Rail class
    protected int[] positionsy;
    
    // physics
    protected double moveSpeed;
    protected double maxSpeed;
    protected double jumpStart;
    protected double fallSpeed;
    protected double maxFallSpeed;
    
    // moving
    protected boolean left;
    protected boolean right;
    protected boolean up;
    protected boolean down;
    protected boolean jumping;
    protected boolean falling;
    
    // reference to backgrounds
    protected BackgroundManager bgManager;
    
    // images from ImagesLoader
    protected ArrayList<ArrayList<BufferedImage>> sprites;
    protected ArrayList<Integer> delays;
    
    // animation
    protected Animation animation;
    protected long animationDelay;
    protected int currentAction;
    
    // standard speeds
    public final long stddelay = 150;
    public final double stdspeed = 8;
    
    public Entity() {
        
        animation = new Animation();
        
        sprites = new ArrayList<>();
        delays = new ArrayList<>();
        
    }
    
    private void checkRailChange() {
        
        if(jumping || falling) return;
        
        ydest = y + dy;
        
        if(up) {
            if(currentRail != UPPER_RAIL) {
                nextRail--;
                if(nextRail < LOWER_RAIL) nextRail = LOWER_RAIL;
                changingUpperRail = moving = true;
                changingLowerRail = false;
                dy += -moveSpeed;
                if(dy < -maxSpeed) dy = -maxSpeed;
                if(ydest < positionsy[currentRail - 1]) {
                    currentRail--;
                    y = positionsy[currentRail];
                    dy = 0;
                    up = moving = changingUpperRail = false;
                }
            }
            else up = false;
        }
        else if(down) {
            if(currentRail != LOWER_RAIL) {
                nextRail++;
                if(nextRail > UPPER_RAIL) nextRail = UPPER_RAIL;
                changingLowerRail = moving = true;
                changingUpperRail = false;
                dy += moveSpeed;
                if(dy > maxSpeed) dy = maxSpeed;
                if(ydest > positionsy[currentRail + 1]) {
                    currentRail++;
                    y = positionsy[currentRail];
                    dy = 0;
                    down = moving = changingLowerRail = false;
                }
            }
            else down = false;
        }
    }
    
    public boolean isMoving() { return moving; }
    
    /*private void setWorldPosition(Camera camera) {
        xworld = camera.getx();
        yworld = camera.gety();
    }*/
    
    public void setCamera(Camera camera) {
        this.camera = camera;
    }
    
    public void setAnimation(String name, long delay) {
        animation.setFrames(ImagesLoader.getFrames(name));
        animation.setDelay(delay);
        animation.setPlayedOnce(false);
    }
    
    public void setAnimation(int action) { // not working properly
        currentAction = action;
        animation.setFrames(sprites.get(action));
        animation.setDelay(delays.get(action));
        setDimensions(action);
    }
    
    public void setDimensions(String name) {
        
        width = ImagesLoader.getWidth(name);
        height = ImagesLoader.getHeight(name);
        cwidth = (int)(width * 0.6);
        cheight = (int)(height * 0.6);
        
        positionsy = new int[3];
        for(int rail = UPPER_RAIL; rail < NUM_RAILS; rail++) 
            positionsy[rail] = rails[rail] - height / 2;
        
        //System.out.println("This is my current height: " + height);
        
    }
    
    public void setDimensions(int action) {
        width = sprites.get(action).get(0).getWidth();
        height = sprites.get(action).get(0).getHeight();
        cwidth = (int)(width * 0.6);
        cheight = (int)(height * 0.6);
    }
    
    public void checkJumping() {
        if(moving) {
            jumping = false;
            return;
        }
        
        if(falling) jumping = false;
        
        if(jumping && !falling) {
            falling = true;
            dy += jumpStart;
            jumping = false;
        }
        else if(falling) {
            dy += fallSpeed;
            if(dy > maxFallSpeed) dy = maxFallSpeed;
            ydest = y + dy;
            if(ydest > positionsy[currentRail]) {
                y = positionsy[currentRail];
                dy = 0;
                falling = false;
            }
        }
    }
    
    public void stop() {
        dx = dy = 0;
    }
    
    public void update() {
        
        checkJumping();
        checkRailChange();
        
        animationDelay = (long)(150 * (stdspeed / dx));
        
        x += dx;
        y += dy;
        
    }
    
    public void draw(Graphics2D g) {
        //g.fillRect((int)(x - width / 2), (int)(y - height / 2), width, height);
        
        if(animation.getImage() == null)
            g.fillRect((int)(x + camera.getx() - width / 2), (int)(y + camera.gety() - height / 2), width, height);
        else
            g.drawImage(animation.getImage(), 
                    (int)(x + camera.getx() - width / 2), 
                    (int)(y + camera.gety() - height / 2), 
                    null);
        
    }
    
    // setters
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void setPositionInRail(double x, int rail) {
        this.x = x;
        y = rails[rail] - getHeight() / 2;
    }
    
    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
    public void setSpeed(double dx, double dy, double ms) {
    	this.dx = dx;
    	this.dy = dy;
    	initSpeed = dx;
    	moveSpeed = ms;
    	maxSpeed = moveSpeed * 5;
    	animationDelay = (long)(150 * (stdspeed / dx));
    	animation.setDelay(animationDelay);
    }
    
    public void setSpeed(double dx, double dy, double ms, double js, double fs) {
    	this.dx = dx;
    	this.dy = dy;
    	initSpeed = dx;
    	moveSpeed = ms;
    	//moveSpeed = ms * (dx / stdspeed);
    	maxSpeed = moveSpeed * 5;
    	jumpStart = js * (Math.exp(stdspeed / dx - 1));
    	fallSpeed = fs;
    	//fallSpeed = fs * (dx / stdspeed);
    	maxFallSpeed = fallSpeed * 13;
    	animationDelay = (long)(150 * (stdspeed / dx));
    	animation.setDelay(animationDelay);
    }
    
    public long getRelativeDelay(long delay, double speed) {
    	delay *= (long)(stdspeed / speed);
    	if(speed == 0) return stddelay;
    	return delay;
    }
    
    public double getRelativeMoveSpeed(double speed, double dx) {
    	return speed * (dx / stdspeed);
    }
    
    public double getx() { return x; }
    public double gety() { return y; }
    public double getdx() { return dx; }
    public double getdy() { return dy; }
    public int getCurrentRail() { return currentRail; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getCWidth() { return cwidth; }
    public int getCHeight() { return cheight; }
    public boolean isJumping() { return jumping; }
    public boolean isFalling() { return falling; }
    public void setx(double i) { x = i; }
    public void sety(double i) { y = i; }
    public void setLeft(boolean b) { left = b; }
    public void setRight(boolean b) { right = b; }
    public void setUp(boolean b) { up = b; }
    public void setDown(boolean b) { down = b; }
    public void setJumping(boolean b) { jumping = b; }
    public void setUp() { up = true; }
    public void setDown() { down = true; }
    public void setJumping() { jumping = true; }
    
}
