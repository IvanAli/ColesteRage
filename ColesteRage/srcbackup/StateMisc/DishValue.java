/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StateMisc;
import Images.ImagesLoader;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
/**
 *
 * @author Ivan
 */
public class DishValue {
    
    private double x;
    private double y;
    private int value;
    private String valueStr;
    private ArrayList<BufferedImage> blueNumbers;
    private ArrayList<BufferedImage> redNumbers;
    private BufferedImage powerup;
    private BufferedImage image;
    
    private int animation;
    private boolean animationPhase;
    
    private Camera camera;
    // timers
    private long startTime;
    private long startTimeDiff;
    
    // animation is over?
    private boolean over;
    
    private final int PLUS = 10;
    private final int MINUS = 11;
    
    public DishValue() {}
    public DishValue(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public DishValue(double x, double y, int value, Camera camera) {
        this.x = x + camera.getx();
        this.y = y + camera.gety();
        this.value = value;
        this.camera = camera;
        
        valueStr = String.valueOf(Math.abs(value));
        blueNumbers = ImagesLoader.getFrames("numerosAzul");
        redNumbers = ImagesLoader.getFrames("numerosRojo");
        powerup = ImagesLoader.getImage("powerup");
        
        startTime = System.nanoTime();
        animation = 50;
    }
    
    public void setCamera(Camera camera) {
        this.camera = camera;
    }
    
    public void update() {
        startTimeDiff = (System.nanoTime() - startTime) / 1000000;
        if(startTimeDiff > 1000) {
            over = true;
        }
        if(animation < -70) {
            animationPhase = true;
        }
        if(!animationPhase) animation -= 65;
        else { animation += 25; 
            if(animation > 0) animation = 0;
        }
        
    }
    
    public void draw(Graphics2D g) {
        
        startTimeDiff = (System.nanoTime() - startTime) / 1000000;
        if(over) return;
        
        if(value > 0) g.drawImage(blueNumbers.get(PLUS), (int)x, (int)y - 140, null);
        if(value < 0) g.drawImage(redNumbers.get(MINUS), (int)x, (int)y - 140, null);

        for(int i = 0; i < valueStr.length(); i++) {
            int number = valueStr.charAt(i) - '0';/*
            if(value > 0)
                g.drawImage((BufferedImage)blueNumbers.get(number), 
                    (int)x + ((i + 1) * 40), 
                    (int)y - 140, 
                    null);
            if(value < 0)
                g.drawImage((BufferedImage)redNumbers.get(number), 
                    (int)x + ((i + 1) * 40), 
                    (int)y - 140, 
                    null);
            */
            if(value > 0)
                g.drawImage((BufferedImage)blueNumbers.get(number), 
                    (int)x + ((i + 1) * 40), 
                    (int)y - 140 + animation, 
                    (int)(x + ((i + 1) * 40 + blueNumbers.get(number).getWidth())),
                    (int)(y - 140 + blueNumbers.get(number).getHeight() - animation),
                    0,
                    0,
                    blueNumbers.get(number).getWidth(),
                    blueNumbers.get(number).getHeight(),
                    null);
            if(value < 0)
                g.drawImage((BufferedImage)redNumbers.get(number), 
                    (int)x + ((i + 1) * 40), 
                    (int)y - 140 + animation, 
                    (int)(x + ((i + 1) * 40 + redNumbers.get(number).getWidth())),
                    (int)(y - 140 + redNumbers.get(number).getHeight() - animation),
                    0,
                    0,
                    redNumbers.get(number).getWidth(),
                    redNumbers.get(number).getHeight(),
                    null);
            if(value == 0)
                g.drawImage(powerup, 
                    (int)x + ((i + 1) * 40), 
                    (int)y - 140 + animation, 
                    (int)(x + ((i + 1) * 40 + powerup.getWidth())),
                    (int)(y - 140 + powerup.getHeight() - animation),
                    0,
                    0,
                    powerup.getWidth(),
                    powerup.getHeight(),
                    null);
        }
        
    }
    
    public boolean isOver() {
        return over;
    }
}
