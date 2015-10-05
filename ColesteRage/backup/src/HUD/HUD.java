/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HUD;

import Data.OptionsData;
import Game.GamePanel;
import GameStates.OptionsState;
import Images.ImagesLoader;
import Sprites.Entity;
import Sprites.Player;
import Sprites.SpriteAnimation;
import Text.Text;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Color;
/**
 *
 * @author Ivan
 */
public class HUD {
    
    private Player player;
    private int cholesterol;
    private int score;
    private long time;
    private int difficulty;
    
    private boolean isInHeartAttack;
    private boolean isInWeak;
    private int timesHeartAttack;
    private int timesWeak;
    
    private long startTime;
    private long elapsed;
    private SpriteAnimation heart;
    private double rectWidth;
    private double rectHeight;
    
    private int indicatorX;
    private int indicatorY;
    
    // text
    private Text text;
    // images loader
    private ImagesLoader imsLoader;
    // setting the font
    private Font font;
    
    public HUD(ImagesLoader imsLoade, Player p) {
        player = p;
        //this.imsLoader = imsLoader;
        imsLoader = new ImagesLoader("hud_imagenes.txt");
        time = 60;
        difficulty = OptionsData.getDifficulty();
        startTime = System.nanoTime();
        font = new Font("Century Gothic", Font.PLAIN, 30);
        heart = new SpriteAnimation(55, 55, imsLoader, "corazon", 120);
        
        indicatorX = 94;
        indicatorY = 65;
    }
    
    public static String getScore(int score) {
        String zeros = "0000000";
        String scoreStr = zeros.substring(0, zeros.length()-String.valueOf(score).length());
        scoreStr = scoreStr + score;
        return scoreStr;
    }
    
    public static String getTime(long time) {
        if(time < 10) return "0" + time;
        else return String.valueOf(time);
    }
    
    public void update() {
        cholesterol = player.getCholesterol();
        score = player.getScore();
        time = player.getTime();
        
        cholesterol = player.getCholesterol();
        
        if(cholesterol > 89 && cholesterol <100) heart.setDelay(35);
        else if(cholesterol == 100) heart.setDelay(-1);
        else heart.setDelay(120);
        
        
        heart.update();
        
        if(cholesterol > 75) {
        	if(!isInHeartAttack) timesHeartAttack++;
        	isInHeartAttack = true;
        }
        else if(cholesterol < 50) {
        	if(!isInWeak) timesWeak++;
        	isInWeak = true;
        }
        else isInHeartAttack = isInWeak = false;
        
        
        //rectWidth = (int)(150 * (cholesterol / 100));
        rectWidth = 414 * (cholesterol * 1.0 / 100);
        rectHeight = 30;
    }
    
    public void draw(Graphics2D g) {
        
    	//g.setColor(Color.blue);
    	g.setColor(new Color(252, 168, 69));
        g.fillRect(88, 28, (int)rectWidth, (int)rectHeight);
        
        g.fillRect(88, 0, 414, (int)rectHeight);
        
        g.drawImage(
        		imsLoader.getImage("timebox"), 
        		GamePanel.WIDTH / 2 - imsLoader.getImage("timebox").getWidth() / 2, 
        		15, 
        		null
		);
        
        g.drawImage(
        		imsLoader.getImage("scorebox"), 
        		1000 - imsLoader.getImage("timebox").getWidth() / 2, 
        		15, 
        		null
		);
		
        /*
        g.setFont(font);
        g.setColor(Color.white);
        g.drawString("Score: " + score, 50, 50);
        g.drawString("Cholesterol: " + cholesterol, 250, 50);
        g.drawString("Time: " + time, 500, 50);
        */
        /*
        text.drawString(g, "SCORE", "yummyFontB", 30, 30);
        text.drawString(g, String.valueOf(score), "yummyFontB", 30, 70);
        text.drawString(g, "COLESTEROL: " + cholesterol, "yummyFontB", 400, 30);
        text.drawString(g, "TIEMPO", "yummyFontB", 1050, 30);
        text.drawString(g, String.valueOf(time), "yummyFontB", 1180, 70);
        */
        // draw difficulty
        //Text.drawString(g, "DIFICULTAD", 770, 10, "yummyFont", null);
        /*
        if(difficulty == OptionsState.EASY) Text.drawString(g, "FÁCIL", 770, 50, "yummyFont", null);
        if(difficulty == OptionsState.NORMAL) Text.drawString(g, "NORMAL", 770, 50, "yummyFont", null);
        if(difficulty == OptionsState.HARD) Text.drawString(g, "DIFÍCIL", 770, 50, "yummyFont", null);
        */
        //Text.drawString(g, "SCORE", 1080, 10, "yummyFont", null);
        Text.drawString(g, getScore(score), 900, 40, "yummyFont", null);
        //Text.drawString(g, "COLESTEROL: " + cholesterol, 400, 30, "yummyFont", null);
        //Text.drawString(g, "TIEMPO", 550, 10, "yummyFont", null);
        Text.drawString(g, getTime(time), 610, 40, "yummyFont", null);
        g.drawImage(imsLoader.getImage("hud"), 15, 15, null);
        heart.draw(g);
        
        if(cholesterol >= 50 && cholesterol <= 75) g.drawImage(imsLoader.getImage("rageTxt"), indicatorX, indicatorY, null);
        if(cholesterol < 50) g.drawImage(imsLoader.getImage("debilTxt"), indicatorX, indicatorY, null);
        if(cholesterol > 75) g.drawImage(imsLoader.getImage("paroTxt"), indicatorX, indicatorY, null);
        
        
        
        // g.fillRect(0, 0, 300, 200);
        
    }
    
    public int getTimesHeartAttack() { return timesHeartAttack; }
    public int getTimesWeak() { return timesWeak; }
    
}
