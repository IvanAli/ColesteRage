/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sprites;

import Audio.AudioLoader;
import Audio.AudioPlayer;
import Background.BackgroundManager;
import GameStates.GameState;
import Images.ImagesLoader;
import StateMisc.DishValue;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Ivan
 */
public class Player extends Entity {
    
    private int cholesterol;
    private int score;
    private int scoreLevel;
    private long time;
    private int currentPower;
    private int eatenDishes;
    
    // reference to the background manager
    //private BackgroundManager bgManager;
    
    // reference to dishes
    private ArrayList<Dish> dishes;
    
    // numbers
    private ArrayList<DishValue> numbers;
    
    // explosion
    private ArrayList<SpriteAnimation> explosions;
    
    // power up
    private ArrayList<SpriteAnimation> powerup;
    
    // player states
    private boolean eatinghealthy;
    private boolean eatingunhealthy;
    private boolean eatingpower;
    private boolean accelerating;
    private boolean caught;
    private boolean dead;
    private boolean cleared;
    private boolean playing;
    
    private boolean wait;
    
    // timers
    private long startTime;
    private long cholesterolTimer;
    private long cholesterolElapsed;
    private long timeElapsed;
    
    private long eatingTimer;
    private long eatingTimerDiff;
    private long powerTimer;
    private long powerTimerDiff;
    
    
    private final int NOPOWER = 0;
    private final int FREEZEPOWER = 1;
    private final int DOUBLESCOREPOWER = 2;
    private final int DOUBLESPEEDPOWER = 3;
    private final int HALFSPEEDPOWER = 4;
    
    
    private final int WALKING = 0;
    private final int EATINGHEALTHY = 1;
    private final int EATINGUNHEALTHY = 2;
    private final int EATINGPOWER = 3;
    private final int JUMPING = 4;
    private final int FALLING = 5;
    private final int CHANGINGUPPERRAIL = 6;
    private final int CHANGINGLOWERRAIL = 7;
    private final int CAUGHT = 7;
    private final int DEAD = 8;
    private final int CLEARED = 9;
    private final int ACCELERATING = 10;
    private final int NUM_STATES = 11;
    
    
    public Player() {
    	
        ImagesLoader.loadFromFile("player_imagenes.txt");
        AudioLoader.loadFromFile("playersfx.txt");
        // setting the physics
        moveSpeed = 3.6;
        maxSpeed = 15.2;
        //stopSpeed = 0.4;
        fallSpeed = 1.8;
        maxFallSpeed = 24.2;
        jumpStart = -28.8;
        //stopJumpSpeed = 0.3;
        
        
        // size
        setDimensions("player_walk");

        // setting the rail
        currentRail = MIDDLE_RAIL;
        nextRail = currentRail;
        
        // current state
        currentAction = WALKING;
        
        // current power
        currentPower = NOPOWER;
        // set animation
        setAnimation("player_walk", animationDelay);
        
        // cholesterol
        cholesterol = 50;
        
        // set time
        time = 60;
        startTime = System.nanoTime();
        cholesterolTimer = System.nanoTime();
        
        // score
        score = 0;
        falling = jumping = eatinghealthy = eatingunhealthy = eatingpower = caught = dead = cleared = false;
    }
    
    public void init(ArrayList<Dish> dishes, ArrayList<DishValue> numbers, ArrayList<SpriteAnimation> explosions, ArrayList<SpriteAnimation> powerup) {
        
        this.dishes = dishes;
        this.bgManager = bgManager;
        this.numbers = numbers;
        this.explosions = explosions;
        this.powerup = powerup;
        
    }
    
    public boolean eats(Dish d) {
        if((x + cwidth / 2 > d.getx() - d.getCWidth() / 2) && (x - cwidth / 2  < d.getx() + d.getWidth() / 2) && 
                currentRail == d.getCurrentRail() && !(jumping || falling)) {
            eatenDishes++;
            return true;
        }
        return false;
    }
    
    public void setPower(Power p) {
        if(p.getName().equals("FREEZEPOWER")) currentPower = FREEZEPOWER;
        if(p.getName().equals("DOUBLESCOREPOWER")) currentPower = DOUBLESCOREPOWER;
        if(p.getName().equals("DOUBLESPEEDPOWER")) currentPower = DOUBLESPEEDPOWER;
        if(p.getName().equals("HALFSPEEDPOWER")) currentPower = HALFSPEEDPOWER;
        System.out.println("Current power: " + currentPower);
    }
    
    public void update() {
        
        playing = (caught || dead || cleared || wait) ? false : true;
        
        if(caught) {
            if(currentAction != CAUGHT) {
                currentAction = CAUGHT;
                animation.playOnce();
                setAnimation("player_caught", 150);
                setDimensions("player_caught");
            }
        }
        else if(dead) {
            if(currentAction != DEAD) {
                currentAction = DEAD;
                animation.playOnce();
                setAnimation("player_dead", 150);
                setDimensions("player_dead");
            }
        }
        else if(cleared) {
            if(currentAction != CLEARED) {
                currentAction = CLEARED;
                setAnimation("player_cleared", 150);
                setDimensions("player_cleared");
            }
        }
        else if(changingUpperRail) {
            if(currentAction != CHANGINGUPPERRAIL) {
                currentAction = CHANGINGUPPERRAIL;
                setAnimation("player_upper", animationDelay);
                setDimensions("player_upper");
            }
        }
        else if(changingLowerRail) {
            if(currentAction != CHANGINGLOWERRAIL) {
                currentAction = CHANGINGLOWERRAIL;
                setAnimation("player_lower", animationDelay);
                setDimensions("player_lower");
            }
        }
        else if(jumping) {
            if(currentAction != JUMPING) {
                currentAction = JUMPING;
                setAnimation("player_jump", -1);
                setDimensions("player_jump");
                if(!falling) AudioPlayer.play("jump");
            }
        }
        else if(falling) {
        	if(dy > 0)
            if(currentAction != FALLING) {
                currentAction = FALLING;
                setAnimation("player_fall", -1);
                setDimensions("player_fall");
            }
        }
        else if(eatingpower) {
            if(currentAction != EATINGPOWER) {
                currentAction = EATINGPOWER;
                setAnimation("player_eatingpower", animationDelay);
                setDimensions("player_eatingpower");
            }
        }
        else if(eatinghealthy) {
            if(currentAction != EATINGHEALTHY) {
                currentAction = EATINGHEALTHY;
                setAnimation("player_eatinghealthy", animationDelay);
                setDimensions("player_eatinghealthy");
            }
        }
        else if(eatingunhealthy) {
            if(currentAction != EATINGUNHEALTHY) {
                currentAction = EATINGUNHEALTHY;
                setAnimation("player_eatingunhealthy", animationDelay);
                setDimensions("player_eatingunhealthy");
            }
        }
        else {
            if(currentAction != WALKING) {
                currentAction = WALKING;
                setAnimation("player_walk", animationDelay);
                setDimensions("player_walk");
            }
        }
        
        //if(cleared || dead || caught) dx = 0;
        //jumping = falling = eatinghealthy = eatingunhealthy = false; // let's fix it later
        /*
        if(jumping) 
            if(currentAction != JUMPING) setAnimation(JUMPING);
        else if(falling) 
            if(currentAction != FALLING) setAnimation(FALLING);
        else if(eatinghealthy)
            if(currentAction != EATINGHEALTHY) setAnimation(EATINGHEALTHY);
        else if(eatingunhealthy) 
            if(currentAction != EATINGUNHEALTHY) setAnimation(EATINGUNHEALTHY);
        else 
            if(currentAction != WALKING) setAnimation(WALKING);
        */
        
        
        if(playing) {
        
            timeElapsed = (System.nanoTime() - startTime) / 1000000;
            if(timeElapsed >= 1000) {
                time--;
                if(time < 6 && time > 0) AudioPlayer.play("timecounter");
                if(time == 0) AudioPlayer.play("timeend");
                if(time < 0) {
                    time = 0;
                    if(!caught && !dead) cleared = true;
                }
                startTime = System.nanoTime();
            }
        
            // checking our current power
            cholesterolElapsed = (System.nanoTime() - cholesterolTimer) / 1000000;
            if(currentPower == NOPOWER) {
                cholesterolElapsed = (System.nanoTime() - cholesterolTimer) / 1000000;
                /*if(cholesterol > 65 && cholesterol < 90) {
                    increaseScore(3);
                    AudioPlayer.play("increasescore");
                }*/
            }/*
            if(currentPower == FREEZEPOWER) {
                if(cholesterol > 65 && cholesterol < 90) increaseScore(3);
            }
            if(currentPower == DOUBLESCOREPOWER) {
                cholesterolElapsed = (System.nanoTime() - cholesterolTimer) / 1000000;
                if(cholesterol > 65 && cholesterol < 90) increaseScore(6);
            }
            if(currentPower == DOUBLESPEEDPOWER) {
                cholesterolElapsed = (System.nanoTime() - cholesterolTimer) / 1000000;
                dx *= 2;
            }
            if(currentPower == HALFSPEEDPOWER) {
                cholesterolElapsed = (System.nanoTime() - cholesterolTimer) / 1000000;
                dx /= 2;
            }*/

            //if(currentPower != FREEZEPOWER) cholesterolElapsed = (System.nanoTime() - cholesterolTimer) / 1000000;
            if(cholesterolElapsed > 500 && !cleared) {
                if(currentPower != FREEZEPOWER)
                    cholesterol--;
                cholesterolTimer = System.nanoTime();
            }


            if(cholesterol < 0) {
                cholesterol = 0;
                caught = true;
                // debug
                System.out.println("Te cacharon");
            }
            if(cholesterol > 100) {
                cholesterol = 100;
                dead = true;
                // debug
                System.out.println("Moriste");
            }
            if(cholesterol > 50 && cholesterol < 75) {
                if(currentPower == DOUBLESCOREPOWER) 
                    increaseScore(6);
                else 
                    increaseScore(3);
                AudioPlayer.play("increasescore");
            }
                


            // check for collisions with dishes
            for(int i = 0; i < dishes.size(); i++) {
                Dish d = dishes.get(i);
                if(eats(d)) {
                    if(d instanceof Food) {
                        if(currentPower == FREEZEPOWER) break;
                        Food f = (Food)d;
                        changeCholesterol(f.getValue());
                        if(f.getValue() > 0) {
                            eatingunhealthy = true;
                            explosions.add(new SpriteAnimation(f.getx(), f.gety(), "explosionUnhealthy", 50, camera));
                            AudioPlayer.play("eatingunhealthy");
                        }
                        if(f.getValue() < 0) {
                            eatinghealthy = true;
                            explosions.add(new SpriteAnimation(f.getx(), f.gety(), "explosionHealthy", 50, camera));
                            AudioPlayer.play("eatinghealthy");
                        }
                        eatingTimer = System.nanoTime();
                        numbers.add(new DishValue(f.getx(), f.gety(), f.getValue(), camera));
                        
                    }
                    if(d instanceof Power) {
                        Power p = (Power)d;
                        setPower(p);
                        eatingpower = true;
                        AudioPlayer.play("powerup");
                        powerTimer = System.nanoTime();
                        numbers.add(new DishValue(d.getx(), d.gety(), 0, camera));
                        explosions.add(new SpriteAnimation(d.getx(), d.gety(), "explosionPower2", 50, camera));
                        powerup.clear();
                        powerup.add(new SpriteAnimation((int)x, (int)y, "powerupeffect", 50, camera));
                    }
                    dishes.remove(i);
                    i--;
                }
            }

            if(eatinghealthy || eatingunhealthy)
                eatingTimerDiff = (System.nanoTime() - eatingTimer) / 1000000;
            
            if(eatingpower) {
                if(currentPower == DOUBLESPEEDPOWER) {
                    dx = initSpeed * 2; // temporary solution
                }
                else if(currentPower == HALFSPEEDPOWER) {
                    dx = initSpeed / 2; // temporary solution
                }
                else {
                	dx = initSpeed;
                }
                powerTimerDiff = (System.nanoTime() - powerTimer) / 1000000;
                /*
                if(currentPower == FREEZEPOWER) {
                    if(cholesterol > 65 && cholesterol < 90) increaseScore(3);
                }
                if(currentPower == DOUBLESCOREPOWER) {
                    cholesterolElapsed = (System.nanoTime() - cholesterolTimer) / 1000000;
                    if(cholesterol > 65 && cholesterol < 90) increaseScore(6);
                }
                if(currentPower == DOUBLESPEEDPOWER) {
                    cholesterolElapsed = (System.nanoTime() - cholesterolTimer) / 1000000;
                    dx *= 2;
                }
                if(currentPower == HALFSPEEDPOWER) {
                    cholesterolElapsed = (System.nanoTime() - cholesterolTimer) / 1000000;
                    dx /= 2;
                }           
                */
            }
            else {
                dx = initSpeed;
            }
            /*
            if(eatinghealthy || eatingunhealthy) {
            	System.out.println("Eating healthy or unhealthy");
            	if(animation.hasPlayedOnce()) {
            		System.out.println("Done playing");
            		eatinghealthy = eatingunhealthy = false;
            	}
            }
            */
            if(eatingTimerDiff > 800) { // maybe two timers
                eatinghealthy = eatingunhealthy = false;
                eatingTimer = 0;
            }
            
            // accelerating
            if(accelerating) {

            }

            if(powerTimerDiff > 5000) {
                eatingpower = false;
                // revert changes
                if(currentPower == DOUBLESPEEDPOWER) dx /= 2;
                if(currentPower == HALFSPEEDPOWER) dx *= 2;
                currentPower = NOPOWER;
                powerTimer = powerTimerDiff = 0;
            }
        
        }
        if(cleared || caught || dead) y = positionsy[currentRail];
        
        
        // Debugging with current action
        //System.out.println("Current Action: " + currentAction);
        
        animation.update();
        super.update();
    }
    
    /*
    private void setAnimation(ArrayList<BufferedImage> frames, long delay) {
        animation.setFrames(frames);
        animation.setDelay(delay);
    }
    */
    
    public int getCholesterol() { return cholesterol; }
    public int getScore() { return score; }
    public int getScoreLevel() { return scoreLevel; }
    public long getTime() { return time; }
    public int getEatenDishes() { return eatenDishes; }
    public boolean isEatinghealthy() { return eatinghealthy; }
    public boolean isEatingunhealthy() { return eatingunhealthy; }
    public boolean isEatingpower() { return eatingpower; }
    public boolean isPlaying() { return playing; }
    public boolean isCaught() { return caught; }
    public boolean isDead() { return dead; }
    public boolean clearedLevel() { return cleared; }
    
    public void setCholesterol(int i) { cholesterol = i; }
    public void setScore(int i) { score = i ; }
    public void setTime(long t) { time = t; }
    public void setAccelerating() { accelerating = true; }
    
    public void addEatenDishes(int i) { eatenDishes += i; }
    public void changeCholesterol(int value) { cholesterol += value; }
    public void increaseScore(int i) { score += i; scoreLevel += i; }
    public void setPlaying(boolean b) { wait = !b; }
    
}
