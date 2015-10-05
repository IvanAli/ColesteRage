/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import GameStates.GameStateManager;
import Keyboard.Keys;

import java.awt.event.KeyListener;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
/**
 *
 * @author Ivan
 */
public class GamePanel extends JPanel implements Runnable, KeyListener {
	
    // default game width and height
    public static int WIDTH = 1280;
    public static int HEIGHT = 720;
    
    // scales for resolution
    public static double FULLSCREEN = 0;
    public static final double SCALE1280X720 = 1;
	public static final double SCALE960X540 = 0.75;
	public static final double SCALE640X360 = 0.5;
	public static double SCALE = FULLSCREEN;
    
    // frame dimensions
    public static int WINDOW_WIDTH;
    public static int WINDOW_HEIGHT;
    
    // graphics variables
    private Graphics2D g;
    private GraphicsDevice gd;
    
    private BufferedImage image;
    
    // game thread
    private Thread thread;
    
    // check if game is running
    private boolean running = false;
    
    // fps and period
    public static int FPS = 30;
    private long period = 1000/FPS;
    
    // game state manager
    private GameStateManager gsm;
    
    public GamePanel() {
        super();
        WINDOW_WIDTH = (int)(WIDTH * 1.0 * SCALE);
        WINDOW_HEIGHT = (int)(HEIGHT * 1.0 * SCALE);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setFocusable(true);
        requestFocus();
    }
    
    
    public void setWindowSize(double scale) {
    	if(scale == FULLSCREEN) {
    		return;
    	}
    	SCALE = scale;
    	WINDOW_WIDTH = (int)(WIDTH * 1.0 * scale);
    	WINDOW_HEIGHT = (int)(HEIGHT * 1.0 * scale);
    }
    
    public void addNotify() {
        super.addNotify();
        if(thread == null) {
            thread = new Thread(this);
            thread.start();
        }
        addKeyListener(this);
    }
    
    public void run() {
        
        long startTime;
        long delayTime;
        long waitTime;
        
        running = true;
        
        init();
        
        while(running) {
            
            startTime = System.nanoTime();
            
            update();
            render();
            draw();
            
            delayTime = (System.nanoTime() - startTime) / 1000000;
            waitTime = period - delayTime;
            
            if(waitTime < 0) waitTime = 5;
            
            try {
                Thread.sleep(waitTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
    }
    
    private void init() {
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        
        //g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gsm = new GameStateManager();
    }
    
    private void update() {
        gsm.update();
        Keys.update();
    }
    
    private void render() {
        g = (Graphics2D) image.getGraphics();
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      
        g.setColor(Color.white);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        gsm.draw(g);
    }
    
    private void draw() {
    
        Graphics g2 = this.getGraphics();
        //g2.fillRect(0, 0, WIDTH, HEIGHT);
        g2.drawImage(image, 0, 0, WINDOW_WIDTH + 10, WINDOW_HEIGHT + 10, null);
        g2.dispose();
        
    }
    
    public void keyTyped(KeyEvent k) {}
    public void keyPressed(KeyEvent k) {
        Keys.keySet(k.getKeyCode(), true);
    }
    public void keyReleased(KeyEvent k) {
        Keys.keySet(k.getKeyCode(), false);
    }
}
