/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sprites;

/**
 *
 * @author Ivan
 */
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class Animation {
    
    public ArrayList<BufferedImage> frames;
    public int currentFrame;
    public long startTime;
    public long delay;
    public boolean playedOnce;
    public boolean playingOnce;
    
    private BufferedImage noImage;
    
    public Animation() {
        
        currentFrame = 0;
        playedOnce = false;
        
        noImageCreation();
        
    }
    
    public void noImageCreation() {
        noImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics g = noImage.getGraphics();
        g.fillRect(0, 0, noImage.getWidth(), noImage.getHeight());
        g.dispose();
    }
    
    
    public void playOnce() { playingOnce = true; }
    
    public void setFrames(ArrayList<BufferedImage> frames) {
        
        this.frames = frames;
        startTime = System.nanoTime();
        currentFrame = 0;
        playedOnce = false;
        
    }
    
    public void setDelay(long d) { delay = d; }

    public void update() {
        
        if(delay == -1) return;
        
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if(elapsed > delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }
        
        if(frames == null) return;
        if(currentFrame == frames.size()) {
            currentFrame = 0;
            playedOnce = true;
        }
        if(playedOnce && playingOnce) currentFrame = frames.size() - 1;
        
    }
    
    public boolean hasPlayedOnce() { return playedOnce; }
    
    public int getFrame() { return currentFrame; }
    public BufferedImage getImage() { 
        if(frames.get(currentFrame) == null) return noImage;
        return frames.get(currentFrame); 
    }

	public void setPlayedOnce(boolean b) { playedOnce = b; }
    
}
