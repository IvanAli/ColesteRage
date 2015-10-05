package GameStates;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Color;

import Audio.AudioLoader;
import Audio.AudioPlayer;
import Game.GamePanel;
import Keyboard.Keys;
import Text.Text;

public class PauseState extends GameState {
	
	private int widthbox;
	private int heightbox;
	private int currentOption;
	
	// ticks
	private int ticks;
	
	// list of options
	public static final int NUM_OPTIONS = 3;
	public static final int RESUMEGAME = 0;
	public static final int BACKTOMENU = 1;
	public static final int QUITGAME = 2;
	
	public PauseState(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	public void init() {
		
		widthbox = 1000;
		heightbox = 600;
		
		AudioLoader.loadFromFile("pause_sonido.txt");
		
	}
	
	public void update() {
		
		input();
		// to-do-list
		// reanudar partida
		// ir al menu
		// salir del juego
		ticks++;
		ticks %= 16;
		
	}
	
	public void draw(Graphics2D g) {
		
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(
				GamePanel.WIDTH / 2 - widthbox / 2, 
				GamePanel.HEIGHT / 2 - heightbox / 2, 
				widthbox, 
				heightbox);
		g.setColor(Color.white);
		g.setStroke(new BasicStroke(10));
		g.drawRect(
				GamePanel.WIDTH / 2 - widthbox / 2, 
				GamePanel.HEIGHT / 2 - heightbox / 2, 
				widthbox, 
				heightbox);
		Text.drawString(g, "PAUSA", 560, 150, "leishoFont", null);
		
		Text.drawString(g, "Reanudar partida", 440, 300, "leishoFont", null);
		Text.drawString(g, "Regresar al menú principal", 340, 350, "leishoFont", null);
		Text.drawString(g, "Salir del juego", 470, 400, "leishoFont", null);
		
		if(currentOption == RESUMEGAME) {
			if(ticks < 8)
				Text.drawString(g, Character.toString((char)16), 410, 300, "leishoFont", null);
		}
		if(currentOption == BACKTOMENU) {
			if(ticks < 8)
				Text.drawString(g, Character.toString((char)16), 310, 350, "leishoFont", null);
		}
		if(currentOption == QUITGAME) {
			if(ticks < 8)
				Text.drawString(g, Character.toString((char)16), 440, 400, "leishoFont", null);
		}
		
	}
	
	public void input() {
		
		if(Keys.isPressed(Keys.UP)) {
			ticks = 0;
			AudioPlayer.play("menu_mover2");
			currentOption--;
			if(currentOption < 0) currentOption = NUM_OPTIONS - 1;
		}
		if(Keys.isPressed(Keys.DOWN)) {
			ticks = 0;
			AudioPlayer.play("menu_mover2");
			currentOption++;
			if(currentOption > NUM_OPTIONS - 1) currentOption = 0;
		}
		if(Keys.isPressed(Keys.ENTER)) {
			AudioPlayer.play("menu_ok");
			gsm.setPaused(false);
			if(currentOption == RESUMEGAME) gsm.setPaused(false);
			if(currentOption == BACKTOMENU) { // audio not stopping
				gsm.setState(GameStateManager.MENU);
			}
			if(currentOption == QUITGAME) System.exit(0);
		}
		if(Keys.isPressed(Keys.ESCAPE)) gsm.setPaused(false);
		
	}
	
	public void clearAll() {
    	//imsLoader.clearImages();
    	AudioPlayer.clearAudio();
    }

}
