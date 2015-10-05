/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StateMisc;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.RescaleOp;

import Game.GamePanel;
/**
 *
 * @author Ivan
 */
public class MovingAnimation {
    
    private BufferedImage image;
    private RescaleOp brightOp;
    
    private double x;
    private double y;
    private double dx;
    private double dy;
    
    private double moveSpeed;
    private double maxSpeed;
    private double minSpeed;
    
    private int width;
    private int height;
    
    private int animationType;
    private int edge;
    
    private float brightness;
    private boolean topBrightness;
    
    public static final int TOLEFT = 0;
    public static final int TORIGHT = 1;
    public static final int TOUP = 2;
    public static final int TODOWN = 3;
    public static final int BRIGHTINCREASE = 4;
    
    public static final int CENTER = -1;
    
    public MovingAnimation(BufferedImage image, int type) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
        animationType = type;
    }
    
    public MovingAnimation(BufferedImage image, int x, int y, int type, int edge) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
        
        this.x = x;
        this.y = y;
        
        if(x == CENTER) {
            x = GamePanel.WIDTH /2;
        }
        
        moveSpeed = 20.5;
        maxSpeed = 130;
        minSpeed = 10;
        
        brightness = 1;
        
        animationType = type;
        this.edge = edge;
    }
    
    public void update() {
        if(animationType == TOLEFT) {
            toLeft(edge);
        }
        if(animationType == TORIGHT) {
            //toRight(3);
        }
        if(animationType == TODOWN) {
            toDown(edge);
        }
        if(animationType == BRIGHTINCREASE) {
            brightIncrease();
        }
        x += dx;
        y += dy;
    }
    
    public void draw(Graphics2D g) {
        if(animationType == BRIGHTINCREASE)
            g.drawImage(
                    image,
                    brightOp,
                    (int)(x - width / 2),
                    (int)(y - height / 2)
            );
        else 
            g.drawImage(
                image,
                (int)(x - width / 2),
                (int)(y - height / 2),
                null
            );
        
    }
    
    
    public void toLeft(int edge) {
        dx -= moveSpeed;
        if(dx < -maxSpeed) dx = -maxSpeed;
        if(x < edge) dx = 0;
    }
    
    public void toRight(int edge) {
        dx += moveSpeed;
        if(dx > maxSpeed) dx = maxSpeed;
        if(x > edge) dx = 0;
    }
    
    public void toDown(int edge) {
        dy += moveSpeed;
        if(dy > moveSpeed) dy = maxSpeed;
        if(y > edge) dy = 0;
    }
    
    public void brightIncrease() {
        if(hasAlpha(image)) {
            float[] factors = {brightness, brightness, brightness, 1.0f};
            float[] offset = {0.0f, 0.0f, 0.0f, 0.0f};
            brightOp = new RescaleOp(factors, offset, null);
        }
        else {
            brightOp = new RescaleOp(brightness, 0.0f, null);
        }
        
        if(!topBrightness) {
            brightness += 0.1;
            if(brightness > 1.9) topBrightness = true;
        }
        else {
            brightness -= 0.1;
            if(brightness < 1) brightness = 1;
        }
        
    }
    
    public boolean hasAlpha(BufferedImage im) {
        if(im == null)
            return false;
        int transparency = im.getColorModel().getTransparency();
        if((transparency == Transparency.BITMASK) || (transparency == Transparency.TRANSLUCENT))
            return true;
        return false;
    }
    
    public void setAnimationType(int type) { animationType = type; }
    
}
