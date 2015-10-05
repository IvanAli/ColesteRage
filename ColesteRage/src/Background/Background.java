/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Background;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import Game.GamePanel;

/**
 *
 * @author Ivan
 */
public class Background {
    
    private double x;
    private double y;
    private double xtemp;
    private double ytemp;
    private double dx;
    private double dy;
    
    private int width;
    private int height;
    
    private BufferedImage image;
    
    private int type;
    
    public static final int MOVELEFT = 0;
    public static final int MOVERIGHT = 1;
    public static final int MOVEUP = 2;
    public static final int MOVEDOWN = 3;
    public static final int MOVEUPLEFT = 4;
    
    public Background(double x, double y, BufferedImage image, int type){
        
        this.x = x;
        this.y = y;
        this.image = image;
        this.type = type;
        width = image.getWidth();
        height = image.getHeight();
    
    }
    
    public Background(BufferedImage image, int type) {
    
        this.image = image;
        this.type = type;
        width = image.getWidth();
        height = image.getHeight();
   
    }
    
    public void update() {
    	if(type == MOVELEFT) x %= width;
    	if(type == MOVEUPLEFT) {
    		x %= width;
    		y %= height;
    	}
    	x %= width;
		y %= height;
		xtemp %= width;
		ytemp %= height;
    	x += dx;
    	y += dy;
    	xtemp -= dy;
    	ytemp -= dy;
    }
    
    
    public void drawImage(Graphics2D g, double dx1, double dx2, double sx1, double sx2) {
        g.drawImage(image, (int)dx1, (int)y, (int)dx2, GamePanel.HEIGHT, (int)sx1, 0, (int)sx2, height, null);
    }
    
    public void draw(Graphics2D g) {
    	if(type == MOVELEFT) {
    		if(x >= 0) {
                drawImage(g, x, GamePanel.WIDTH, 0, GamePanel.WIDTH - x);
                drawImage(g, 0, x, width - x, width);
            }
            else {
                drawImage(g, 0, GamePanel.WIDTH, -x, GamePanel.WIDTH - x);
                if(x + width < GamePanel.WIDTH) {
                    drawImage(g, x + width, GamePanel.WIDTH, 0, GamePanel.WIDTH - (x + width));
                }
            }
    	}
    	/*
    	if(type == MOVEUPLEFT) {
            if(width - x > GamePanel.WIDTH)//Revisar este g.drawImage
                g.drawImage(image, 0, (int)y, GamePanel.WIDTH, (int)(y + height), (int)x, 0, (int)(GamePanel.WIDTH + x), height, null);
            if(width - x <= GamePanel.WIDTH) {
                g.drawImage(image, 0, (int)y, (int)(width - x), (int)(y + height), (int)x, 0, width, height, null);
                g.drawImage(image, (int)(width - x), (int)y, GamePanel.WIDTH, (int)(y + height), 0, 0, GamePanel.WIDTH - (int)(width - x), height, null);
            }
    	}
    	*/
    	if(type == MOVEUP) {
            if(height - y > GamePanel.HEIGHT)
                g.drawImage(image, (int)x, 0, (int)(x + width), GamePanel.HEIGHT, 0, (int)y, width, (int)(GamePanel.HEIGHT + y), null);
            if(height - y <= GamePanel.HEIGHT) {
                g.drawImage(image, (int)x, 0, (int)(x + width), (int)(height - y), 0, (int)y, width, height, null);
                g.drawImage(image, (int)x, (int)(height - y), (int)(x + width), GamePanel.HEIGHT, 0, 0, width, GamePanel.HEIGHT - (int)(height - y), null);
            }
    	}
    	/*if(type == MOVEUPLEFT) { // not working
    		if(width - x > GamePanel.WIDTH || height - y > GamePanel.HEIGHT)
                g.drawImage(image, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, (int)x, (int)y, (int)(GamePanel.WIDTH + x), (int)(GamePanel.HEIGHT + y), null);
            if(width - x <= GamePanel.WIDTH || height - y <= GamePanel.HEIGHT) {
                g.drawImage(image, 0, 0, (int)(width - x), (int)(height - y), (int)x, (int)y, width, height, null);
                g.drawImage(image, (int)(width - x), (int)(height - y), GamePanel.WIDTH, GamePanel.HEIGHT, 0, 0, GamePanel.WIDTH - (int)(width - x), GamePanel.HEIGHT - (int)(height - y), null);
            }
    	}*/
    	/*
    	if(type == MOVEUPLEFT) { // not working
    		g.drawImage(image, 0, 0, (int)(width - x), (int)(height - y), (int)x, (int)y, width, height, null);
            if(width - x <= GamePanel.WIDTH) {
            	g.drawImage(image, 0, -(int)y, -(int)(width - x), -(int)(y + height), (int)x, 0, width, height, null);
                g.drawImage(image, (int)(width - x), (int)(height - y), GamePanel.WIDTH, GamePanel.HEIGHT, 0, 0, GamePanel.WIDTH - (int)(width - x), GamePanel.HEIGHT - (int)(height - y), null);
            }
            if(height - y <= GamePanel.HEIGHT) {
            	g.drawImage(image, -(int)x, 0, -(int)(x + width), -(int)(height - y), 0, (int)y, width, height, null);
                g.drawImage(image, (int)(width - x), (int)(height - y), GamePanel.WIDTH, GamePanel.HEIGHT, 0, 0, GamePanel.WIDTH - (int)(width - x), GamePanel.HEIGHT - (int)(height - y), null);
            }
            */
    	if(type == MOVEUPLEFT) { // not working either
            if(width - x > GamePanel.WIDTH)
                g.drawImage(image, 0, (int)y, GamePanel.WIDTH, (int)(y + height), (int)x, 0, (int)(GamePanel.WIDTH + x), height, null);
            if(width - x <= GamePanel.WIDTH) {
                g.drawImage(image, 0, (int)y, (int)(width - x), (int)(y + height), (int)x, 0, width, height, null);
                g.drawImage(image, (int)(width - x), (int)y, GamePanel.WIDTH, (int)(y + height), 0, 0, GamePanel.WIDTH - (int)(width - x), height, null);
            }
            if(height + y <= GamePanel.HEIGHT) {
            	System.out.println("Drawing the height");
            	g.drawImage(image, (int)xtemp, 0, (int)(xtemp + width), (int)(height - ytemp), 0, (int)ytemp, width, height, null);
                g.drawImage(image, (int)xtemp, (int)(height - ytemp), (int)(xtemp + width), GamePanel.HEIGHT, 0, 0, width, GamePanel.HEIGHT - (int)(height - ytemp), null);
            }
    	}
    }
    
    
    public BufferedImage getImage() { return (BufferedImage) image; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public double getx() { return x; }
    public double gety() { return y; }
    public double getdx() { return dx; }
    public double getdy() { return dy; }
    public void setx(double i) { x = i; }
    public void sety(double i) { y = i; }
    public void setdx(double i) { dx = i; }
    public void setdy(double i) { dy = i; }
    
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
    
}
