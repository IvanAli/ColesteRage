/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sprites;

import Images.ImagesLoader;

/**
 *
 * @author Ivan
 */
public class NPC extends Entity {
    
    private Player player;
    
    // x positioning variables
    private double distance;
    private double startx;
    private double newx;
    private double levelSpeed = 8;
    
    private long delay;
    
    // temporal
    private double diff;
    
    private boolean catching;
    private boolean mourning;
    
    private final int WALKING = 0;
    private final int CATCHING = 1;
    private final int MOURNING = 2;
    private final int CHANGINGUPPERRAIL = 3;
    private final int CHANGINGLOWERRAIL = 4;
    
    public NPC(Player p) {
    
    	ImagesLoader.loadFromFile("npc_imagenes.txt");
        
        // setting the physics
        moveSpeed = 3.6;
        maxSpeed = 15.2;
        //stopSpeed = 0.4;
        fallSpeed = 1.8;
        maxFallSpeed = 24.2;
        jumpStart = -28.8;
        //stopJumpSpeed = 0.3;
        
        player = p;
        
        startx = x;
        
        currentRail = p.getCurrentRail();
        
        currentAction = WALKING;
        catching = false;
        
        setAnimation("npc_walk", getRelativeDelay(150, dx));
        setDimensions("npc_walk");
        
    }
    
    public void update() {
        
    	if(changingUpperRail) {
            if(currentAction != CHANGINGUPPERRAIL) {
                currentAction = CHANGINGUPPERRAIL;
                setAnimation("npc_upper", animationDelay);
                setDimensions("npc_upper");
            }
        }
    	else if(changingLowerRail) {
            if(currentAction != CHANGINGLOWERRAIL) {
                currentAction = CHANGINGLOWERRAIL;
                setAnimation("npc_lower", animationDelay);
                setDimensions("npc_lower");
            }
        }
    	else if(catching) {
            if(currentAction != CATCHING) {
                currentAction = CATCHING;
                animation.playOnce();
                setAnimation("npc_catch", 150);
                setDimensions("npc_catch");
            }
        }
    	else if(mourning) {
         	if(currentAction != MOURNING) {
        		currentAction = MOURNING;
         		setAnimation("npc_mourn", 150);
        		setDimensions("npc_mourn");
        	}
        }
    	else {
    		if(currentAction != WALKING) {
    			currentAction = WALKING;
    			setAnimation("npc_walk", animationDelay);
    			setDimensions("npc_walk");
    		}
    	}
        xdest = player.getx() - 6.2 * player.getCholesterol() - player.getWidth() / 2;
        /*if(player.isEatinghealthy()) dx = player.getdx() + 2;
        if(player.isEatingunhealthy()) dx = player.getdx() - 2;*/
        dx = (xdest - x) / 30 + player.getdx();
        if((x > xdest && player.isEatinghealthy()) || (x < xdest && player.isEatingunhealthy()) ) dx = player.getdx();
        //System.out.println("dx: " + dx);
        //if(x + dx > xdest)
        // my new position in x would be ... player.getx() - 6.2 * cholesterol;
        //diff = player.getx() - x;
        // linear y equation: 6.2x ... diff / cholesterol at 50
        /*
        if(player.isEatinghealthy()) {
            dx = 15;
        }
        else if(player.isEatingunhealthy()) {
            dx = 7;
        }
        else {
            dx = 8;
        }
        */
        /*
        if(player.getCholesterol() >= 50) 
            if(x > -width / 2) dx = (startx - x) / 30 + levelSpeed;
            else dx = levelSpeed;
        */
        if(player.getCholesterol() == 0) catching = true;
        if(player.clearedLevel()) mourning = true;
        // update the rail to player's rail
        //currentRail = player.getCurrentRail();
        
        // debugging
        //System.out.println("Player x: " + player.getx() + ". NPC x: " + x);
        animation.update();
        //if(!player.isJumping() && !player.isFalling())
            super.update();
        moving = player.isMoving(); // verify
        //currentRail = player.getCurrentRail();
        
    }
    
}
