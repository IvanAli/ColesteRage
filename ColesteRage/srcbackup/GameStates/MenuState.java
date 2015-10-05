/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import Audio.AudioLoader;
import Audio.AudioPlayer;
import Background.Background;
import Game.GamePanel;
import Images.ImagesLoader;
import Keyboard.Keys;
import StateMisc.MovingAnimation;
import Text.Title;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author Ivan
 */
public class MenuState extends GameState {
    
    private MovingAnimation bodoque;
    private MovingAnimation logotype;
    private Title story;
    private Title instructions;
    private Title topScores;
    private Title option;
    private Title exit;
    
    // background
    private Background bgMenu;
    
    // time
    private int ticks;
    private long startTime;
    private long delay;
    private final int FADEOUT = 15;
    
    // alpha for fadeout
    private int alpha;
    
    private boolean up;
    private boolean down;
    private boolean enter;
    
    private int currentOption;
    
    private int menuBorderX;
    private int menuBorderY;
    
    private final String[] options = {
        "Modo Historia",
        "Opciones",
        "Salir"
    };
    
    private final int STORYMODE = 0;
    private final int INSTRUCTIONS = 1;
    private final int TOPSCORES = 2;
    private final int OPTIONS = 3;
    private final int EXIT = 4;
    private final int NUM_OPTIONS = 5;
    
    public MenuState(GameStateManager gsm) {
        super(gsm);
        currentOption = STORYMODE;
        init();
    }
    
    public void init() {
        
        // images loader init
        ImagesLoader.loadFromFile("menu_imagenes.txt");
        
        // start the audio
        AudioLoader.loadFromFile("menu_sonido.txt");
        
        // stop all audio beforehand
    	//audio.stopAll();
    	
    	// init the background
    	bgMenu = new Background(
				0, 
				0, 
				ImagesLoader.getImage("bgMenu"), 
				Background.MOVEUP
		);
    	bgMenu.setVector(0, 3);
    	
        // play the audio
    	if(!AudioPlayer.isPlaying("menumusic")) {
    		System.out.println("Not playing menu music");
    		AudioPlayer.playAndLoop("menumusic");
    	}

        // animation init
        bodoque = new MovingAnimation(ImagesLoader.getImage("bodoque_left"), GamePanel.WIDTH + 190, 400, MovingAnimation.TOLEFT, 150);
        logotype = new MovingAnimation(ImagesLoader.getImage("colesterage_logo"), GamePanel.WIDTH / 2, -120, MovingAnimation.TODOWN, 50);
        
        story = new Title("Modo historia", Title.CENTERED, 350, Title.MENUOPTIONANIMATION);
        instructions = new Title("Instrucciones", Title.CENTERED, 400, Title.MENUOPTIONANIMATION);
        topScores = new Title("Mejores puntajes", Title.CENTERED, 450, Title.MENUOPTIONANIMATION);
        option = new Title("Opciones", Title.CENTERED, 500, Title.MENUOPTIONANIMATION);
        exit = new Title("Salir", Title.CENTERED, 550, Title.MENUOPTIONANIMATION);
        
        startTime = System.nanoTime();
        
    }
    
    public void update() {
        ticks++;
        
        // keyboard input
        input();
        bgMenu.update();
        // alpha update
        alpha = (int)(255 - 255 * (1.0 * ticks / FADEOUT));
        if(alpha < 0) alpha = 0;
                
        // animation update
        if(ticks > 15)
            bodoque.update();
        if(ticks > 30)
            logotype.update();
        if(ticks > 40) logotype.setAnimationType(MovingAnimation.BRIGHTINCREASE);
        
        if(ticks > 50) {
            if(!story.hasStarted()) story.start();
            if(!instructions.hasStarted()) instructions.start();
            if(!topScores.hasStarted()) topScores.start();
            if(!option.hasStarted()) option.start();
            if(!exit.hasStarted()) exit.start();
            
        

            // animating the menu options
            /*
            if(currentOption == STORYMODE) {
                story.resume();
                option.stop();
                exit.stop();
            }
            if(currentOption == OPTIONS) {
                story.stop();
                option.resume();
                exit.stop();
            }
            if(currentOption == EXIT) {
                story.stop();
                option.stop();
                exit.resume();
            }
            */
        }
        // moving through the menu
        if(up) {
            AudioPlayer.play("menu_mover");
            currentOption--;
            if(currentOption < 0) currentOption = NUM_OPTIONS - 1;
        }
        if(down) {
            AudioPlayer.play("menu_mover");
            currentOption++;
            if(currentOption > NUM_OPTIONS - 1) currentOption = 0;
        }
        if(enter) {
            AudioPlayer.play("menu_enter");
            AudioPlayer.stop("menumusic");
            
            if(currentOption == STORYMODE) {
            	//audio.stop("menumusic");
            	ImagesLoader.removeImages("menu_imagenes.txt");
            	AudioLoader.removeAudio("menu_sonido.txt");
            	gsm.setState(GameStateManager.STORY1);
            }
            //if(currentOption == INSTRUCTIONS) gsm.setState(GameStateManager.INSTRUCTIONS);
            if(currentOption == TOPSCORES) gsm.setState(GameStateManager.TOPSCORES);
            if(currentOption == OPTIONS) {
            	//audio.stop("menumusic");
            	ImagesLoader.removeImages("menu_imagenes.txt");
            	AudioLoader.removeAudio("menu_sonido.txt");
            	gsm.setState(GameStateManager.OPTIONS);
            }
            if(currentOption == EXIT) System.exit(0);
        }
        up = down = enter = false;
    }
    
    public void draw(Graphics2D g) {
        
        g.drawImage(ImagesLoader.getImage("menu_bg"), 0, 0, null);
        bgMenu.draw(g);
        if(ticks > 50) {
        	if(currentOption == STORYMODE) menuBorderY = 353;
        	if(currentOption == INSTRUCTIONS) menuBorderY = 403;
        	if(currentOption == TOPSCORES) menuBorderY = 453;
            if(currentOption == OPTIONS) menuBorderY = 503;
            if(currentOption == EXIT) menuBorderY = 553;
            
            g.drawImage(ImagesLoader.getImage("menuhighlight"), 0, menuBorderY, null);
            
            if(story != null) story.draw(g);
            if(instructions != null) instructions.draw(g);
            if(topScores != null) topScores.draw(g);
            if(option != null) option.draw(g);
            if(exit != null) exit.draw(g);
            
            /*
            g.setColor(Color.red);
            g.fillOval(420, menuBorderY, 20, 20);
            */
            //g.drawImage(ImagesLoader.getImage("menu_optionborder"), 500, menuBorderY, null);
        }
        //g.drawImage(ImagesLoader.getImage("bodoque_left"), 20, 60, null);
        bodoque.draw(g);
        logotype.draw(g);
        
        //logo.drawImage(g, 400, 120, -1f);
        //g.drawImage(ImagesLoader.getImage("colesterage_logo"), 400, 60, null);
        
        g.setColor(new Color(0, 0, 0, alpha));
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        
        /*
        g.setFont(new Font("Century Gothic", Font.PLAIN, 90));
        g.drawString("Colesterage", 300, 200);
        */
        /*
        for(int i = 0; i < options.length; i++) {
            if(i == currentOption) g.setColor(Color.red);
            else g.setColor(Color.cyan);
            g.setFont(new Font("Century Gothic", Font.PLAIN, 30));
            g.drawString(options[i], GamePanel.WIDTH / 2 - 160, GamePanel.HEIGHT / 2 + (i * 60));
        }*/
        
        
        
        
    }
    
    public void input() {
    	
        if(ticks < 50) return;
        up = Keys.isPressed(Keys.UP);
        down = Keys.isPressed(Keys.DOWN);
        enter = Keys.isPressed(Keys.ENTER);
        
    }
    
    public void clearAll() {
    	ImagesLoader.clearImages();
    	AudioPlayer.clearAudio();
    }
}
