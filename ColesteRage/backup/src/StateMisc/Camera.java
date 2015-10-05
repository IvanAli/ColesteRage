/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StateMisc;

/**
 *
 * @author Ivan
 */
public class Camera {
    
    private double x;
    private double y;
    private double dx;
    private double dy;
    
    public Camera() {}
    
    public Camera(double x, double y) {
        this.x = x;
        this.y = y;
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
    public void sety(double i) { y = i; }
    
}
