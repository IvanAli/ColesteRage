/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import java.awt.Graphics2D;

/**
 *
 * @author Ivan
 */
public abstract class GameState {
    protected GameStateManager gsm;
    
    public GameState(GameStateManager gsm) {
        
        this.gsm = gsm;
        
    }
    
    public void init() {};
    public void update() {};
    public void draw(Graphics2D g) {};
    public void input() {};
    public void stopAudio() {};
}
