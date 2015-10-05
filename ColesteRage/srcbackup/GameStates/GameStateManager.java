/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Images.ImagesLoader;

/**
 *
 * @author Ivan
 */
public class GameStateManager {
	
	public boolean paused;
	
    // Mapeo de estados del juego
    public static final int INTRO = 0;
    public static final int MENU = 1;
    public static final int OPTIONS = 2;
    public static final int LEVEL1 = 3;
    public static final int LEVEL2 = 4;
    public static final int LEVEL3 = 5;
    public static final int LEVEL4 = 6;
    public static final int LEVEL5 = 7;
    public static final int STORY1 = 8;
    public static final int STORY2 = 9;
    public static final int STORY3 = 10;
    public static final int STORY4 = 11;
    public static final int STORY5 = 12;
    public static final int STORY6 = 13;
    public static final int CREDITS = 14;
    public static final int NAMEENTRY = 15;
    public static final int INSTRUCTIONS = 16;
    public static final int TOPSCORES = 17;
    public static final int NUM_STATES = 18;
    private GameState[] gameStates;
    private GameState pauseState;
    private int currentState;
    
    public GameStateManager() {
    	
        gameStates = new GameState[NUM_STATES];
        currentState = INTRO;
        paused = false;
        
        loadState(currentState);
        pauseState = new PauseState(this);
        
    }
    
    public void loadState(int state) {
        // debugging
        System.out.println("State loading; " + state);
        if(state == INTRO) gameStates[state] = new IntroState(this);
        if(state == MENU) gameStates[state] = new MenuState(this);
        
        //if(state == INSTRUCTIONS) gameStates[state] = new InstructionsState(this);
        if(state == TOPSCORES) gameStates[state] = new TopScoresState(this);
        if(state == OPTIONS) gameStates[state] = new OptionsState(this);
        if(state == STORY1) gameStates[state] = new Story1State(this);
        if(state == STORY2) gameStates[state] = new Story2State(this);
        if(state == STORY3) gameStates[state] = new Story3State(this);
        if(state == STORY4) gameStates[state] = new Story4State(this);
        if(state == STORY5) gameStates[state] = new Story5State(this);
        if(state == STORY6) gameStates[state] = new Story6State(this);
        if(state == LEVEL1) gameStates[state] = new Level1State(this);
        if(state == LEVEL2) gameStates[state] = new Level2State(this);
        if(state == LEVEL3) gameStates[state] = new Level3State(this);
        if(state == LEVEL4) gameStates[state] = new Level4State(this);
        if(state == LEVEL5) gameStates[state] = new Level5State(this);
        if(state == CREDITS) gameStates[state] = new CreditsState(this);
        if(state == NAMEENTRY) gameStates[state] = new NameEntryState(this);
        
    }
    
    public void setState(int state) { 
        unloadState(currentState);
        System.gc();
        currentState = state;
        loadState(currentState);
    }
    
    private void unloadState(int state) { 
    	// let's stop the audio first
    	gameStates[state].stopAudio();
    	gameStates[state].clearAll();
    	gameStates[state] = null; 
    	
    	// debugging
    	System.out.println("Images hashmap size: " + ImagesLoader.size());
	}
    
    public void update() {
    	if(paused) {
    		pauseState.update();
    		return;
    	}
        if(gameStates[currentState] != null)
            gameStates[currentState].update();
    }
    
    public void draw(Graphics2D g) {
        if(gameStates[currentState] != null)
            gameStates[currentState].draw(g);
        else {
            g.setColor(Color.black);
            //g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        } 
        if(paused) pauseState.draw(g);
    }
    
    public boolean isPaused() { return paused; }
    public void setPaused(boolean b) { paused = b; }
    
}
