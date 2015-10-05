package Text;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.image.BufferedImage;

import Game.GamePanel;
/**
 *
 * @author Ivan
 */
public class Letter {
    
    private double x;
    private double y;
    private double dx;
    private double dy;
    
    private int angle;
    private int width;
    private int height;
    
    private long startTime;
    private long elapsed;
    private long delay;
    
    private char ch;
    private BufferedImage image;
    
    // flags for animation hola
    private boolean hitTop;
    
    public Letter() {}
    public Letter(char ch, BufferedImage image) {
        this.ch = ch;
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
        
        startTime = System.nanoTime();
    }
    
    public void update() {
        x += dx;
        y += dy;
        System.out.println("dy: " + dy);
    }
    
    public void playUpDownAnimation(int ymin, int ymax) {
        if(hitTop) dy = -dy;
        if(y < ymin) hitTop = true;
        if(y > ymax) hitTop = false;
        y += dy;
    }
    
    public void playFromBottomToTop() {
        if(y + dy < (GamePanel.HEIGHT - height) / 2) {
            y = (GamePanel.HEIGHT - height) / 2;
            dy = 0;
        }
        y += dy;
    }
    
    public BufferedImage getImage() { return image; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getAngle() { return angle; }
    public long getStartTime() { return startTime; }
    public long getDelay() { return delay; }
    public double getx() { return x; }
    public double gety() { return y; }
    public char getCh() { return ch; }
    
    public void setx(double d) { x = d; }
    public void sety(double d) { y = d; }
    public void setdx(double d) { dx = d; }
    public void setdy(double d) { dy = d; }
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
    public void setAngle(int i) { angle = i; }
    public void setDelay(long l) { delay = l; }
    public void setStartTime(long l) { startTime = l; }
    public void addAngle(int i) { angle += i; }
    
}
