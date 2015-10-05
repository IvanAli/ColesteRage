package Text;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import Game.GamePanel;
import Images.ImagesLoader;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.RescaleOp;

/**
 *
 * @author Ivan
 */
public class Title {
    
    private Letter[] letters;
    
    private int x;
    private int y;
    private int dx;
    private int dy;
    private int width;
    private int height;
    
    private int xmin;
    private int xmax;
    private int ymin;
    private int ymax;
    
    public static int TITLENUMBER = 0;
    
    private BufferedImage image;
    private ImagesLoader imsLoader;
    
    private String message;
    
    private long startTime;
    private long elapsed;
    
    private long delay;
    private boolean remove;
    
    // Graphics stuff for animation
    private AffineTransform at;
    private int angle;
    private double scale;
    private int spacing;
    
    // flags for animation
    private boolean angleMax;
    private boolean hitUp;
    
    private int counter;
    
    private String fontName;
    private int type;
    
    private boolean startFlag;
    private boolean stopped;
    
    public static final int INTROANIMATIONA = 0;
    public static final int INTROANIMATIONB = 1;
    public static final int CLEARANIMATION = 2;
    public static final int CAUGHTANIMATION = 3;
    public static final int DEADANIMATION = 4;
    public static final int MENUOPTIONANIMATION = 5;
    
    public static final int CENTERED = -1;
   
        
    public Title(String msg, int type) { 
        
        TITLENUMBER++;
        
        message = msg;
        
        this.type = type;
        
        if(type == INTROANIMATIONA) {
            fontName = "titleFont";
            angle = 0;
            delay = 0;
            
        }
        if(type == INTROANIMATIONB) {
            fontName = "titleFont";
            angle = -180;
            delay = 200;
        }
        if(type == CLEARANIMATION) {
            fontName = "clearFont";
            angle = 0;
            delay = 250;
        }
        if(type == CAUGHTANIMATION) {
            fontName = "failFont";
            angle = 0;
            delay = 150;
        }
        if(type == MENUOPTIONANIMATION) {
            fontName = "leishoFont";
            angle = 0;
            delay = 150;
        }
        x = y = -1;
        Text.loadFont(fontName + ".png");
    }
   
    public Title(String msg, int x, int y, int type) { 
        
        TITLENUMBER++;
        
        message = msg;
        
        this.x = x;
        this.y = y;
        
        this.type = type;
        
        if(type == INTROANIMATIONA) {
            fontName = "titleFont";
            angle = 0;
            delay = 0;
            
        }
        if(type == INTROANIMATIONB) {
            fontName = "titleFont";
            angle = -180;
            delay = 200;
        }
        if(type == CLEARANIMATION) {
            fontName = "clearFont";
            angle = 0;
            delay = 250;
        }
        if(type == CAUGHTANIMATION) {
            fontName = "failFont";
            angle = 0;
            delay = 150;
        }
        if(type == MENUOPTIONANIMATION) {
            fontName = "leishoFont";
            angle = 0;
            delay = 30;
        }
        
        Text.loadFont(fontName + ".png");
    }
        
    public void start() {
        counter = 0;
        letters = new Letter[message.length()];
        //Text.loadFont(fontName + ".png");
        
        
        for(int i = 0; i < letters.length; i++) {
                letters[i] = new Letter(
                        (char)message.charAt(i), 
                        Text.getFont(fontName).getLetter(message.charAt(i)).getImage()
                );
                //letters[i] = Text.getFont(fontName).getLetter(message.charAt(i));
                letters[i].setAngle(angle);
                letters[i].setDelay(i * delay);
                letters[i].setStartTime(System.nanoTime());
                width += letters[i].getWidth();
                height = letters[i].getHeight();
        }
        height = letters[0].getHeight();
        
        if(type != MENUOPTIONANIMATION) {
            x = (GamePanel.WIDTH - width) / 2;
            y = (GamePanel.HEIGHT - height) / 2;
        }
        else {
            if(x == CENTERED) x = (GamePanel.WIDTH - width) / 2;
            //x = (x - width) / 2;
            //y = (y - height) / 2;
            
        }
        System.out.println("y pos: " + y);
        
        ymin = y - 5;
        ymax = y + 5;
        
        width = 0;
        
        for(int i = 0; i < letters.length; i++) {
            letters[i].setPosition(x + spacing, y);
            if(type == CLEARANIMATION) {
                letters[i].setPosition(x + spacing, GamePanel.HEIGHT);
                System.out.println("y position: " + letters[i].gety());
            }
            spacing += letters[i].getWidth();
        }
        
        startTime = System.nanoTime();
        startFlag = true;
        
        
    }
    
    public boolean hasStarted() {
        return startFlag;
    }
    
    public void reset() {
        for(int i = 0; i < letters.length; i++) {
            letters[i] = Text.getFont(fontName).getLetter(message.charAt(i));
            letters[i].setAngle(0);
            letters[i].setDelay(0);
            letters[i].setStartTime(0);
        }
    }
    
    public void update() {
        if(!startFlag) return;
        counter++;
        
        if(type == INTROANIMATIONA) {
            if(!angleMax) {
                angle = (angle + 10) % 360;
                if(angle > 40) angleMax = true;
            }
            else if(angleMax) {
                angle = (angle - 10) % 360;
                if(angle < -40) angleMax = false;
            }
            
            scale += 0.03;
            if(scale > 1) scale = 1;
        
            if(counter > message.length() * 8) {
                dy += 4;
            }
        }
        
        if(type == INTROANIMATIONB) {
            spacing = 1;
            if(counter > message.length() * 6) {
                dy -= 3;
            }   
        }
        
        if(type == CAUGHTANIMATION) {
            spacing = 1;
            dy = -3;
            if(y < GamePanel.HEIGHT / 4) hitUp = true;
            if(y > GamePanel.HEIGHT / 4 + GamePanel.HEIGHT / 2) hitUp = false;
            if(hitUp) dy = -dy;
        }
        
        if(type == MENUOPTIONANIMATION) {
            spacing = 1;
            dy = -3;
            if(y < ymin) hitUp = true;
            if(y > ymax) hitUp = false;
            if(hitUp) dy = -dy;
        }
        
        elapsed = (System.nanoTime() - startTime) / 1000000;
        
        if(elapsed > 4000 || counter > 80) {
            //Text.clearFont(fontName);
            remove = true;
            reset();
        }
        
        x += dx;
        y += dy;
    }
    
    public void drawFirstMessage(Graphics2D g) {
        
        for(int i = 0; i < message.length(); i++) {
            at = new AffineTransform();
            letters[i].setAngle(angle);
            at.rotate(
                    Math.toRadians(letters[i].getAngle()), 
                    x + spacing + letters[i].getWidth() / 2, 
                    y + letters[i].getHeight() / 2
            );
            g.setTransform(at);
            g.drawImage(
                    letters[i].getImage(), 
                    x + spacing, 
                    y, 
                    (int)(letters[i].getWidth() * scale),
                    (int)(letters[i].getHeight() * scale),
                    null
            );
            
            spacing += letters[i].getWidth();
        }
        g.setTransform(new AffineTransform());
        spacing = 0;
    }
    
    public void drawSecondMessage(Graphics2D g) {
        
            for(int i = 0; i < message.length(); i++) {
                
                Letter letter = letters[i];
                
                at = new AffineTransform();
                at.rotate(
                        Math.toRadians(letters[i].getAngle()), 
                        x + spacing + letters[i].getWidth() / 2, 
                        y + letters[i].getHeight() / 2
                );
                g.setTransform(at);
                
                if((System.nanoTime() - letter.getStartTime()) / 1000000 > letter.getDelay()) {
                    letter.addAngle(20);
                    if(letter.getAngle() > 0) letter.setAngle(0);
                }
                
                g.drawImage(
                        letters[i].getImage(), 
                        x + spacing, 
                        y, 
                        null
                );

                spacing += letters[i].getWidth();
            }
            g.setTransform(new AffineTransform());
    }
    
    public void stop() {
            for(int i = 0; i < message.length(); i++) {
                Letter letter = letters[i];
                
                //if((System.nanoTime() - letter.getStartTime()) / 1000000 > letter.getDelay()) {
                    letter.setdy(0);
                //} 
            stopped = true;
            }
    }
    
    public void resume() {
        for(int i = 0; i < message.length(); i++) {
            Letter letter = letters[i];
            letter.setStartTime(System.nanoTime());
        }
        stopped = false;
    }
    
    public void draw(Graphics2D g) {
        
        if(!startFlag) return;
        if(type == INTROANIMATIONA) {
                drawFirstMessage(g);
        }
        
        if(type == INTROANIMATIONB) {
            drawSecondMessage(g);
        }
        
        if(type == CAUGHTANIMATION) {
            for(int i = 0; i < message.length(); i++) {
                Letter letter = letters[i];
                
                if((System.nanoTime() - letter.getStartTime()) / 1000000 > letter.getDelay()) {
                    if(!stopped)
                        letter.setdy(-1.5);
                } 
                
                g.drawImage(
                        letter.getImage(), 
                        (int)letter.getx(), 
                        (int)letter.gety(), 
                        null
                );
                
                letter.playUpDownAnimation(200, 400);

            }
        }

        if(type == CLEARANIMATION) {
            for(int i = 0; i < message.length(); i++) {
                Letter letter = letters[i];
                
                if((System.nanoTime() - letter.getStartTime()) / 1000000 > letter.getDelay()) {
                    letter.setdy(-40);
                } 
                
                g.drawImage(
                        letter.getImage(), 
                        (int)letter.getx(), 
                        (int)letter.gety(), 
                        null
                );
                
                letter.playFromBottomToTop();

            }
        }
        
        if(type == MENUOPTIONANIMATION) {
            for(int i = 0; i < message.length(); i++) {
                Letter letter = letters[i];
                
                if((System.nanoTime() - letter.getStartTime()) / 1000000 > letter.getDelay()) {
                    letter.setdy(-0.3);
                } 
                
                g.drawImage(
                        letter.getImage(), 
                        (int)letter.getx(), 
                        (int)letter.gety(), 
                        null
                );
                
                letter.playUpDownAnimation(ymin, ymax);

            }
        }
        
    }
    
    public boolean shouldRemove() { return remove; }
    public void setMessage(String message) { this.message = message; }
}
