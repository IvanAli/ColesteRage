/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import java.io.InputStream;

import Game.GamePanel;
import HUD.*;
import Images.ImagesLoader;
import Images.ImagesLoader;
import Keyboard.Keys;
import Sprites.*;
import StateMisc.Camera;
import StateMisc.DishValue;
import Text.Text;
import Text.TextAnimation;
import Text.Title;
import Audio.AudioLoader;
import Audio.AudioPlayer;
import Background.Background;
import Background.BackgroundAnimation;
import Data.OptionsData;
import Data.PlayerData;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import static java.lang.Math.random;

import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * @author Ivan
 */
public class Level5State extends GameState {
    
    // txtanimation
    private TextAnimation txtAnimation;
    
    // Level events
    private boolean eventIntro;
    private boolean eventClear;
    private boolean eventDead;
    private boolean eventCaught;
    private boolean levelStart;
    private boolean eventPoints;
    private boolean nextLevel;
    
    // Level intro
    private long levelIntroTimer;
    private long levelIntroTimerDiff;
    private long levelIntroDelay;
    
    // results stuff
    private boolean showResults;
    private int squarewidth;
    private int squareheight;
    private long resultsStartTime;
    private long resultsDelay;
    private long resultsElapsed;
    private int ticksResults;
    
    // game objects
    private Player player;
    private NPC npc;
    private ArrayList<Dish> dishes;
    private HUD hud;
    
    // food probability and delay between dishes
    private double foodprobability;
    private int[] fooddelays;
    
    // difficulty
    private int difficulty;
    
    // animation stuff
    private ArrayList<DishValue> numbers;
    private ArrayList<SpriteAnimation> explosions;
    private ArrayList<SpriteAnimation> powerup;
    private BackgroundAnimation heartAttack;
    private BackgroundAnimation warning;
    
    // camera
    private Camera camera;
    private Camera cameraPoint;
    private final int playerxscreen = 250;
    
    // title
    private Title titleIntroA;
    private Title titleIntroB;
    private Title titleClear;
    private Title titleCaught;
    private Title titleDead;
    
    // rails
    private Rail[] rails;
    private final int UPPER_RAIL = 0;
    private final int MIDDLE_RAIL = 1;
    private final int LOWER_RAIL = 2;
    
    // dish creation stuff
    private long foodTimer;
    private long foodDelay;
    private long foodTimerDiff;
    private HashMap<String, Integer> foodValues;
    private ArrayList<String> foodList;
    private ArrayList<String> powerList;    

    // backgrounds
    private Background bg;
    //private Background bgParallax;
    //private Background bgParallax2;
    
    // level speed
    private int levelSpeed;
    
    // music flag
    private boolean playFlag;
    private final String musicFnm = "nivel5_bg";
    
    // next state
    private final int nextState = GameStateManager.STORY6;
    
    // level name
    private final String levelName = "NIVEL 5";
    
    public Level5State(GameStateManager gsm) {
        super(gsm);
        
        init();
    }
    
    public void init() {
    	
    	// LEVEL SPEED
    	levelSpeed = 20; 
        
    	// AudioPlayer init
    	AudioLoader.loadFromFile("nivel5_sonido.txt");
        
        // images loading
        ImagesLoader.loadFromFile("nivel5_imagenes.txt");
        ImagesLoader.loadFromFile("nivel_efectosvisuales_imagenes.txt");
        ImagesLoader.loadFromFile("food_imagenes.txt");
        
        
        // food appearance probability
        if(difficulty == OptionsState.EASY) foodprobability = 0.8;
        if(difficulty == OptionsState.NORMAL) foodprobability = 0.88;
        if(difficulty == OptionsState.HARD) foodprobability = 0.94;
        
        fooddelays = new int[6];
        fooddelays[0] = 60;
        fooddelays[1] = 150;
        fooddelays[2] = 60;
        fooddelays[3] = 80;
        fooddelays[4] = 60;
        fooddelays[5] = 30;
        
        // Events init
        eventIntro = true;
        eventClear = eventDead = eventCaught = false;
        
        txtAnimation = new TextAnimation();
        
        
        camera = new Camera();
        cameraPoint = new Camera();
        
        levelStart = false;
        levelIntroTimer = System.nanoTime();
        levelIntroDelay = 10000;
        
        foodTimer = System.nanoTime();
        foodDelay = 3000;
        
        // load fonts
        Text.loadFont("yummyFont.png");
        Text.loadFont("leishoFont.png");
        
        player = new Player();
        npc = new NPC(player);
        dishes = new ArrayList<>();
        hud = new HUD(player);
        
        // difficulty
        difficulty = OptionsData.getDifficulty();
        
        // rails init
        rails = new Rail[3];
        
        // title init
        titleIntroA = new Title(levelName, Title.INTROANIMATIONA);
        titleIntroB = new Title("EMPIEZA", Title.INTROANIMATIONB);
        titleClear = new Title("GANASTE", Title.CLEARANIMATION);
        titleDead = new Title("MORISTE", Title.CAUGHTANIMATION);
        titleCaught = new Title("ATRAPADO", Title.CAUGHTANIMATION);
        
        titleIntroA.start();
        
        // background creation
        // individual creation
        
        bg = new Background(0, 0, ImagesLoader.getImage("5_background"), Background.MOVELEFT);
        //bgParallax = new Background(0, 0, ImagesLoader.getImage("5_parallax"), Background.MOVELEFT);
        //bgParallax2 = new Background(0, 0, ImagesLoader.getImage("5_parallax2"), Background.MOVELEFT);
        heartAttack = new BackgroundAnimation(ImagesLoader.getImage("heartsolo"), BackgroundAnimation.HEARTATTACK);
        warning = new BackgroundAnimation(ImagesLoader.getImage("warning"), BackgroundAnimation.WARNING);
        /*
        bgSky = new Background(0, 0, ImagesLoader.getImage("nivel1_clouds"));
        bgSoil = new Background(0, GamePanel.HEIGHT - (ImagesLoader.getHeight("nivel1_soil")), 
                ImagesLoader.getImage("nivel1_soil"));
        */
        numbers = new ArrayList<>();
        explosions = new ArrayList<>();
        powerup = new ArrayList<>();
        player.init(dishes, numbers, explosions, powerup);
        
        // setting the score for the player
        player.setScore(PlayerData.getScore());
        
        foodValues = new HashMap<>();
        foodList = new ArrayList<>();
        powerList = new ArrayList<>();
        
        loadFoodFile("archivoComida.txt");
        
        
        powerList.add("FREEZEPOWER");
        powerList.add("DOUBLESCOREPOWER");
        powerList.add("DOUBLESPEEDPOWER");
        powerList.add("HALFSPEEDPOWER");
        
        // set position
        // debugging
        player.setPositionInRail(playerxscreen, player.getCurrentRail());
        cameraPoint.setPosition(playerxscreen, 0);
        
        //player.setVector(levelSpeed, 0);
        // parameters to be given: dx, dy, moveSpeed, jumpStart, fallSpeed
        player.setSpeed(levelSpeed, 0, 3.6, -28.8, 1.8);
        cameraPoint.setVector(levelSpeed, 0);
        
        npc.setPositionInRail(-npc.getWidth() / 2, npc.getCurrentRail());
        //npc.setVector(levelSpeed, 0);
        // parameters to be given: dx, dy, moveSpeed
        npc.setSpeed(levelSpeed, 0, 1.6);
        
        player.setCamera(camera);
        npc.setCamera(camera);
        
        // results stuff init
        squarewidth = 5;
        squareheight = 1;
        resultsDelay = 600;
        ticksResults = 0;
        
        // AudioPlayer volume setting
        //AudioPlayer.setVolume("musicFnm", -5.0f);
        AudioPlayer.setVolume("increasescore", -25.0f);
        
        // AudioPlayer bg play
        AudioPlayer.playAndLoop(musicFnm);
        
    }
    
    private void loadFoodFile(String fnm){
        String line;
        try {
        	InputStream in = getClass().getResourceAsStream("/" + fnm);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while((line = br.readLine()) != null) {
                if(line.startsWith("//"))
                    continue;
                if(line.startsWith("food")){
                    readFoodValues(line);
                }
                //if(line.startsWith("poder"))
                    //leerPoder(line);
            }
            br.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void readFoodValues(String line) {
        StringTokenizer token = new StringTokenizer(line);
        if(token.countTokens() != 2)
            System.out.println("Numero incorrecto de tokens");
        else{
            String name = token.nextToken();
            try {
                int value = Integer.parseInt(token.nextToken()); // this might change depending on difficulty
                if(difficulty == OptionsState.HARD) value *= 2;
                if(difficulty == OptionsState.EASY) value /= 2;
                if(foodValues.containsKey(name))
                    System.out.println("Nombre ya existente");
                else {
                    foodValues.put(name, value);
                    foodList.add(name);
                    System.out.println(name + ": " + value);
                }
                    
            }
            catch(Exception e) {
                System.out.println("Numero invalido para valor nutricional");
            }
        }
    }
    
    private void addDish() {
        foodTimerDiff = (System.nanoTime() - foodTimer) / 1000000;
        if(foodTimerDiff > foodDelay) { // there will be a 88% chance the next dish will be food
            double chance = Math.random();
            int rail = (int)Math.round(Math.random() * 2);
            if(chance < foodprobability) {
                String food = foodList.get((int)(Math.random() * foodList.size()) % foodList.size());
                Food f = new Food(food, rail, foodValues.get(food));
                f.setPositionInRail(player.getx() + GamePanel.WIDTH + f.getWidth() / 2, rail);
                f.setCamera(camera);
                dishes.add(f);
            }
            else {
                String power = powerList.get((int)(Math.random() * powerList.size()) % powerList.size());
                Power p = new Power(power, rail);
                p.setPositionInRail(player.getx() + GamePanel.WIDTH + p.getWidth() / 2, rail);
                p.setCamera(camera);
                dishes.add(p);
            }
            foodTimer = System.nanoTime();
            if(difficulty == OptionsState.EASY) foodDelay = (long)(Math.random() * fooddelays[0]) + fooddelays[1];
            if(difficulty == OptionsState.NORMAL) foodDelay = (long)(Math.random() * fooddelays[2]) + fooddelays[3];
            if(difficulty == OptionsState.HARD) foodDelay = (long)(Math.random() * fooddelays[4]) + fooddelays[5];
        }
    }
    
    private void collisionDetection() {
        for(int i = 0; i < dishes.size(); i++) {
            Dish d = dishes.get(i);
            if(player.eats(d)) {
                if(d instanceof Food) {
                    Food f = (Food)d;
                    player.changeCholesterol(f.getValue());
                }
                if(d instanceof Power) {
                    Power p = (Power)d;
                    player.setPower(p);
                }
                dishes.remove(i);
                i--;
            }
        }
    }
    
    /*
    public void keyPressed(int key) {
        if(key == KeyEvent.VK_UP) {
            player.setUp(true);
            npc.setUp(true);
        }
        if(key == KeyEvent.VK_DOWN) {
            player.setDown(true);
            npc.setDown(true);
        }
        if(key == KeyEvent.VK_SPACE) {
            player.setJumping(true);
        }
    }
    
    public void keyReleased(int key) {
        if(key == KeyEvent.VK_UP) {
            player.setUp(false);
        }
        if(key == KeyEvent.VK_DOWN) {
            player.setDown(false);
        }
        if(key == KeyEvent.VK_SPACE) {
            player.setJumping(false);
        }
    }
    */
    
    public void showResults() {
        resultsElapsed = (System.nanoTime() - resultsStartTime) / 1000000;
        ticksResults++;
        if(ticksResults < 35) {
        	squarewidth += 28;
        	squareheight += 14;
        }
        if(ticksResults > 95) ticksResults = 65;
    }
    
    public void eventPoints() {
    	if(player.getEatenDishes() > 0) {
    		player.addEatenDishes(-1);
    		player.increaseScore(5);
    		AudioPlayer.play("increasescore");
    	}
    	else nextLevel = true;
    }
    
    
    public void update() {
        
        // keyboard input
        input();
        
        // check the events flags
        eventClear = player.clearedLevel();
        eventDead = player.isDead();
        eventCaught = player.isCaught();
        
        
        if(eventIntro) {
            // title update
            player.setPlaying(false);
            if(titleIntroA != null) {
                titleIntroA.update();
                if(titleIntroA.shouldRemove()) {
                    titleIntroA = null;
                    titleIntroB.start();
                }
            }
            if(titleIntroB != null) {
                titleIntroB.update();
                if(titleIntroB.shouldRemove()) {
                    titleIntroB = null;
                    eventIntro = false;
                    player.setPlaying(true);
                }
            }
        }
        
        
        if(eventClear || eventDead || eventCaught) player.stop();
        
        if(eventClear) {
            if(!playFlag) {
                AudioPlayer.stop(musicFnm);
                AudioPlayer.playAndLoop("levelclear");
                playFlag = true;
            }
            PlayerData.setScore(player.getScore());
            PlayerData.addEatenDishes(player.getEatenDishes());
            if(showResults) showResults();
            if(!titleClear.hasStarted())
                titleClear.start();
        }
        
        if(eventCaught) {
        	if(!playFlag) {
        		AudioPlayer.stop(musicFnm);
        		AudioPlayer.play("levelfailcaught");
        		playFlag = true;
        	}
            if(!titleCaught.hasStarted())
                titleCaught.start();
            if(titleCaught.shouldRemove()) titleCaught = null;
        }
        
        if(eventDead) {
        	if(!playFlag) {
        		AudioPlayer.stop(musicFnm);
        		AudioPlayer.play("levelfaildead");
        		playFlag = true;
        	}
            if(!titleDead.hasStarted())
                titleDead.start();
            if(titleDead.shouldRemove()) titleDead = null;
        }
        
        camera.setPosition(playerxscreen - player.getx(), 0);
        
        player.update();
        
        /*
        bgSky.setPosition(camera.getx() * 0.6, camera.gety() + bgSky.gety());
        bgSoil.setPosition(camera.getx(), camera.gety() + bgSoil.gety());
        
        bgSky.update();
        bgSoil.update();
        */
        
        bg.setPosition(camera.getx(), camera.gety());
        bg.update();
        //bgParallax.setPosition(camera.getx() * 0.8, camera.gety());
        //bgParallax.update();
        //bgParallax2.setPosition(camera.getx() * 0.6, camera.gety());
        //bgParallax2.update();
        
        for(int i = 0; i < numbers.size(); i++) {
            numbers.get(i).update();
            if(numbers.get(i).isOver()) {
                numbers.remove(i);
                i--;
            }
        }
        
        for(int i = 0; i < explosions.size(); i++) {
        	SpriteAnimation explosion = explosions.get(i);
        	explosion.update();
        	if(explosion.hasPlayedOnce()) {
        		explosions.remove(i);
        		i--;
        	}
        }
        for(int i = 0; i < powerup.size(); i++) {
        	SpriteAnimation pup = powerup.get(i);
        	pup.update();
        	if(pup.hasPlayedOnce()) {
        		powerup.remove(i);
        		i--;
        	}
        }
                
        //collisionDetection();
        for(int i = 0; i < dishes.size(); i++) {
            //dishes.get(i).setPosition(400 - player.getx(), dishes.get(i).gety());
            dishes.get(i).update();
            if(dishes.get(i).shouldRemove()) {
                dishes.remove(i);
                i--;
            }
        }
        hud.update();
        npc.update();
        
        if(player.getCholesterol() >= 85) {
        	heartAttack.update();
        	if(heartAttack.shouldPlaySound()) 
        		if(player.isPlaying()) AudioPlayer.play("heartbeat");
        }
        if(player.getCholesterol() <= 25) {
        	warning.update();
        	if(warning.shouldPlaySound()) 
        		if(player.isPlaying()) AudioPlayer.play("redalert");
        }
        
        levelIntroTimerDiff = (System.nanoTime() - levelIntroTimer) / 1000000;
        if(levelIntroTimerDiff > levelIntroDelay) levelStart = true;
        
        if(eventIntro || eventClear || eventDead || eventCaught) return;
        addDish();
        
    }
    
    public void draw(Graphics2D g) {
        
        /*
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        g.setColor(Color.white);
        */
                //bgManager.draw(g);
        /*
                mAlimentos.drawAlimentos(g);
        mAlimentos.drawPoderes(g);
    */
        // draw the backgrounds
        /*
        bgSky.draw(g);
        bgSoil.draw(g);
        */
    	//bgParallax2.draw(g);
    	//bgParallax.draw(g);
        bg.draw(g);
       
        
        
        // draw the rails
        /*
        g.setColor(Color.blue);
        g.fillRect(0, 484, GamePanel.WIDTH, 3);
        g.fillRect(0, 592, GamePanel.WIDTH, 3);
        g.fillRect(0, 700, GamePanel.WIDTH, 3);
        */
        
        // draw the dishes
        for(int i = 0; i < dishes.size(); i++) {
            dishes.get(i).draw(g);
        }
        
        // draw the player
        player.draw(g);
        
        //draw the npc
        npc.draw(g);
        
        // draw the hud
        hud.draw(g);
        
        // drawing rails
        //{444, 552, 660}
        
        // draw the dish values
        for(int i = 0; i < numbers.size(); i++) {
            numbers.get(i).draw(g);
        }
        
        // draw the explosion
        for(int i = 0; i < explosions.size(); i++) {
        	explosions.get(i).draw(g);
        }
        
        
        // draw the heart bg animation
        if(player.getCholesterol() >= 85) {
        	g.drawImage(
        			ImagesLoader.getImage("heartattackbg"),
        			GamePanel.WIDTH / 2 - ImagesLoader.getImage("heartattackbg").getWidth() / 2,
        			GamePanel.HEIGHT / 2 - ImagesLoader.getImage("heartattackbg").getHeight() / 2,
        			null
			);
        	heartAttack.draw(g);
        	
        }
        if(player.getCholesterol() <= 25) {
        	warning.draw(g);
        }
        
        
        
        
        // end of heart bg animation
        //Text.drawString(g, "Testing Colesterage", 200, 400, "leishoFont", null);
        //Text.drawString(g, "Font testing", 600, 200, "leishoFont", txtAnimation);
        if(eventIntro) {
            if(titleIntroA != null) titleIntroA.draw(g);
            if(titleIntroB != null) titleIntroB.draw(g);
        }
        
        if(eventCaught) {
            if(titleCaught != null) titleCaught.draw(g);
        }
        
        if(eventClear) {
            if(titleClear != null) if(!showResults) titleClear.draw(g);
            if(showResults) {
            	g.setColor(new Color(0, 0, 0, 226));
            	g.fillRect(
            			GamePanel.WIDTH / 2 - squarewidth / 2, 
            			GamePanel.HEIGHT / 2 - squareheight / 2, 
            			squarewidth, 
            			squareheight
    			);
            	if(ticksResults > 35) {
            		g.setStroke(new BasicStroke(6));
            		g.setColor(Color.white);
            		g.drawRect(
            				GamePanel.WIDTH / 2 - squarewidth / 2, 
            				GamePanel.HEIGHT / 2 - squareheight / 2, 
            				squarewidth, 
            				squareheight
    				);
            	}
            	if(ticksResults > 40) 
            		Text.drawString(g, "Resultados", Text.CENTERED, 150, "leishoFont", null);
            	if(ticksResults > 50) 
            		Text.drawString(g, "Puntos adquiridos: " + player.getScoreLevel(), 250, 250, "leishoFont", null);
            	if(ticksResults > 55)
            		Text.drawString(g, "Alimentos consumidos: " + player.getEatenDishes(), 250, 300, "leishoFont", null);
            	if(ticksResults > 60)
            		Text.drawString(g, "Ocasiones en Paro Cardiaco: " + hud.getTimesHeartAttack(), 250, 350, "leishoFont", null);
            	if(ticksResults > 60)
            		Text.drawString(g, "Ocasiones en Débil: " + hud.getTimesWeak(), 250, 400, "leishoFont", null);
            	if(ticksResults > 65 && ticksResults < 80)
            		Text.drawString(g, "Presiona Enter para continuar", Text.CENTERED, 500, "leishoFont", null);
            	
            }
        }
        
        if(eventDead) {
            if(titleDead != null) titleDead.draw(g);
        }
        
        if(eventPoints) eventPoints();
    }
    
    public void input() {
    	if(player.isPlaying()) if(Keys.isPressed(Keys.ESCAPE) || Keys.isPressed(Keys.ENTER)) gsm.setPaused(true);
        if(!eventCaught && !eventDead && !eventClear) {
            if(Keys.isPressed(Keys.UP)) {
                player.setUp();
                npc.setUp();
            }
            if(Keys.isPressed(Keys.DOWN)) {
                player.setDown();
                npc.setDown();
            }
            if(Keys.isPressed(Keys.SPACE)) player.setJumping();
            if(Keys.isPressed(Keys.RIGHT)) player.setAccelerating();
        }
        
        if(eventCaught) {
            if(Keys.isPressed(Keys.ENTER)) {
            	AudioPlayer.stop("levelfailcaught");
            	gsm.setState(GameStateManager.MENU);
            	ImagesLoader.removeImages("nivel5_imagenes.txt");
            	ImagesLoader.removeImages("player_imagenes.txt");
            	ImagesLoader.removeImages("npc_imagenes.txt");
            	ImagesLoader.removeImages("hud_imagenes.txt");
            	ImagesLoader.removeImages("nivel_efectosvisuales_imagenes.txt");
            	AudioLoader.removeAudio("nivel5_sonido.txt");
            }
        }
        if(eventDead) {
        	if(Keys.isPressed(Keys.ENTER)) {
        		AudioPlayer.stop("levelfaildead");
        		gsm.setState(GameStateManager.MENU);
        		ImagesLoader.removeImages("nivel5_imagenes.txt");
        		ImagesLoader.removeImages("player_imagenes.txt");
            	ImagesLoader.removeImages("npc_imagenes.txt");
            	ImagesLoader.removeImages("hud_imagenes.txt");
            	ImagesLoader.removeImages("nivel_efectosvisuales_imagenes.txt");
        		AudioLoader.removeAudio("nivel5_sonido.txt");
        	}
        }
        if(eventClear) {
        	if(Keys.isPressed(Keys.ENTER)) {
        		if(!showResults) {
        			showResults = true;
        			resultsStartTime = System.nanoTime();
        			return;
        		}
        		if(ticksResults > 65) {
        			eventPoints = true;
        			if(nextLevel) {
        				AudioPlayer.play("menu_skip");
            			AudioPlayer.stop("levelclear");
            			ImagesLoader.removeImages("nivel5_imagenes.txt");
            			AudioLoader.removeAudio("nivel5_sonido.txt");
                		gsm.setState(nextState);
        			}
        		}
        		
        	}
        	
        	
            /*
        	if(Keys.isPressed(Keys.ENTER)) {
                AudioPlayer.stop("levelclear");
                gsm.setState(GameStateManager.Level3);
            }
            */
        }
        
    }
    
    public void stopAudioPlayer() {
    	AudioPlayer.stop(musicFnm);
    	//AudioPlayer.stopAll();
    }
    
    public void clearAll() {
    	ImagesLoader.clearImages();
    	AudioPlayer.clearAudio();
    }
    
}
