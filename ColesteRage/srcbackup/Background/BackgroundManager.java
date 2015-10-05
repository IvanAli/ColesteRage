/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Background;

import Images.ImagesLoader;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author Ivan
 */
public class BackgroundManager {
    private double x;
    private double y;
    private double dx;
    private double dy;
    
    private Background[] backgrounds;
    private double[] factors;
    private int velocidad;
    
    public BackgroundManager(String[] nombreBackgrounds, int[] yBackgrounds, int vel, ImagesLoader imsLoader) {
        backgrounds = new Background[nombreBackgrounds.length];
        factors = new double[nombreBackgrounds.length];
        velocidad = vel;
        setFactores(nombreBackgrounds.length);
        for(int i=0; i<nombreBackgrounds.length; i++) {
            backgrounds[i] = new Background(0, yBackgrounds[i], imsLoader.getImage(nombreBackgrounds[i]), Background.MOVELEFT);
            backgrounds[i].setdx(dx*factors[i]);
        }
    }
    
    public BackgroundManager(Background[] bgs, double[] factors, double dx) {
        
        backgrounds = bgs;
        this.factors = factors;
        this.dx = dx;
        for(int i = 0; i < bgs.length; i++) {
            backgrounds[i].setdx(dx*factors[i]);
        }
    }
    
    public void setFactores(int numBackgrounds) {
        double factor = 1.0/numBackgrounds*1.0;
        for(int i=0; i<numBackgrounds; i++){
            factors[i] = factor;
            //System.out.println("Factor: " + factor);
            factor += 1.0/numBackgrounds*1.0;
        }
    }
    
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
    public double getx() { return x; }
    public double gety() { return y; }
    public void setx(double i) { x = i; }
    public void setdx(double i) { dx = i; }
    
    public void update() {
        
        for(int i=0; i<backgrounds.length; i++) {
            backgrounds[i].update();
        }
        
        x += dx;
        y += dy;
    
    }
    /*
    public void draw(Graphics2D g){
        for(int i=0; i<backgrounds.length; i++){
            backgrounds[i].setx(backgrounds[i].getx()%backgrounds[i].getWidth());
            if(backgrounds[i].getWidth() - backgrounds[i].getx() > GamePanel.WIDTH)//Revisar este g.drawImage
                g.drawImage(backgrounds[i].getImage(), 0, backgrounds[i].gety(), GamePanel.WIDTH, (backgrounds[i].gety() + backgrounds[i].getHeight()), backgrounds[i].getx(), 0, GamePanel.WIDTH + backgrounds[i].getx(), backgrounds[i].getHeight(), null);
            if(backgrounds[i].getWidth() - backgrounds[i].getx() <= GamePanel.WIDTH){
                g.drawImage(backgrounds[i].getImage(), 0, backgrounds[i].gety(), (backgrounds[i].getWidth() - backgrounds[i].getx()), backgrounds[i].gety() + backgrounds[i].getHeight(), (backgrounds[i].getx()), 0, backgrounds[i].getWidth(), backgrounds[i].getHeight(), null);
                g.drawImage(backgrounds[i].getImage(), (backgrounds[i].getWidth() - backgrounds[i].getx()), backgrounds[i].gety(), GamePanel.WIDTH, backgrounds[i].gety() + backgrounds[i].getHeight(), 0, 0, GamePanel.WIDTH - (backgrounds[i].getWidth() - backgrounds[i].getx()), backgrounds[i].getHeight(), null);
            }
        }
    }*/
}