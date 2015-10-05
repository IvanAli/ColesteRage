/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

import Game.GamePanel;
import Keyboard.Keys;
/**
 *
 * @author Ivan
 */
public class IntroState extends GameState {
    
    private int ticks;
    private BufferedImage image;
    private int alpha;
    private int FADE_IN = 60;
    private int LENGTH = 60;
    private int FADE_OUT = 60;
    
    public IntroState(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {}
    
    public void update() {
        
        input();
        
        ticks++;
        
        if(ticks < FADE_IN) {
            alpha = (int)(255 - 255 * (1.0 * ticks / FADE_IN));
            if(alpha < 0) alpha = 0;
        }
        if(ticks > FADE_IN + LENGTH) {
            alpha = (int)(255 * ((1.0 * ticks - FADE_IN - LENGTH) / FADE_OUT));
        }
        if(ticks > FADE_IN + LENGTH + FADE_OUT) {
            gsm.setState(GameStateManager.MENU);
        }
        
    }
    
    public void draw(Graphics2D g) {
        
        g.setColor(Color.white);
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        g.setColor(Color.blue);
        g.setFont(new Font("Century Gothic", Font.PLAIN, 45));
        g.drawString("Intro", GamePanel.WIDTH / 2 - 20, GamePanel.HEIGHT / 2 - 10);
        g.setColor(new Color(0, 0, 0, alpha));
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        
    }
    
    public void input() {
        if(Keys.isPressed(Keys.ENTER)) gsm.setState(GameStateManager.MENU);
    }
}
