/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Keyboard;

import java.awt.event.KeyEvent;

/**
 *
 * @author Ivan
 */
public class Keys {
    
    public static int NUM_KEYS = 7;
    
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int SPACE = 4;
    public static final int ENTER = 5;
    public static final int ESCAPE = 6;
    
    public static boolean[] keyState = new boolean[NUM_KEYS];
    public static boolean[] prevKeyState = new boolean[NUM_KEYS];
    
    public static void keySet(int key, boolean b) {
        if(key == KeyEvent.VK_UP) keyState[UP] = b;
        else if(key == KeyEvent.VK_DOWN) keyState[DOWN] = b;
        else if(key == KeyEvent.VK_LEFT) keyState[LEFT] = b;
        else if(key == KeyEvent.VK_RIGHT) keyState[RIGHT] = b;
        else if(key == KeyEvent.VK_SPACE) keyState[SPACE] = b;
        else if(key == KeyEvent.VK_ENTER) keyState[ENTER] = b;
        else if(key == KeyEvent.VK_ESCAPE) keyState[ESCAPE] = b;
    }
    
    public static boolean isPressed(int key) {
        return keyState[key] && !prevKeyState[key];
    }
    
    public static void update() {
        for(int i = 0; i < NUM_KEYS; i++) {
            prevKeyState[i] = keyState[i];
        }
    }
    
}
