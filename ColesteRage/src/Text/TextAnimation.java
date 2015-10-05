package Text;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Ivan
 */
public class TextAnimation {
    
    private int counter;
    private int length;
    private long startTime;
    private long elapsed;
    private long delay;
    private int currentToken;
    
    public TextAnimation() {
        counter = 1;
        startTime = System.nanoTime();
        delay = 150;
    }
    
    public TextAnimation(long d) {
    	counter = 1;
    	startTime = System.nanoTime();
    	delay = d;
    }
    
    public void setLength(int i) { length = i; }
    
    public void reset() {
    	counter = 1;
    	startTime = System.nanoTime();
    }
    
    public void update() {
        elapsed = (System.nanoTime() - startTime) / 1000000;
        if(elapsed > delay) {
            counter++;
            startTime = System.nanoTime();
        }
        if(counter > length) counter = length;
    }
    
    public int getCounter() { return counter; }
    public void increaseToken() { currentToken++; }
    public int getCurrentToken() { return currentToken; }
    public void setCounter(int i) { counter = i; }
    
    
}
