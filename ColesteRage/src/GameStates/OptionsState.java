package GameStates;
import java.awt.Graphics2D;

import Audio.AudioLoader;
import Audio.AudioPlayer;
import Background.Background;
import Data.OptionsData;
import Game.ColesteRage;
import Game.GamePanel;
import Images.ImagesLoader;
import Keyboard.Keys;
import Text.Text;

import java.awt.Color;

public class OptionsState extends GameState {
	
	private Background bg;
	
	private int xOptions;
	private int xSubOptions;
	private int xIndicator;
	
	private int currentOption;
	private int difficultyOption;
	private int soundOption;
	private int resolutionOption;
	
	// ticks
	private int ticks;
	private int ticksSaved;
	
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	private boolean enter;
	
	public static final int NUM_OPTIONS = 5;
	public static final int DIFFICULTY = 0;
	public static final int SOUND = 1;
	public static final int RESOLUTION = 2;
	public static final int SAVE = 3;
	public static final int BACK = 4;
	
	// difficulty options
	public static final int NUM_DIFFICULTIES = 3;
	public static final int EASY = 0;
	public static final int NORMAL = 1;
	public static final int HARD = 2;
	
	// sound options
	public static final int NUM_SOUNDOPTIONS = 2;
	public static final int ON = 0;
	public static final int OFF = 1;
	
	// resolution options
	public static final int NUM_RESOLUTIONS = 4;
	public static final int FULLSCREEN = 0;
	public static final int RES1280X720 = 1;
	public static final int RES960X540 = 2;
	public static final int RES640X360 = 3;
	
	
	public OptionsState(GameStateManager gsm) {
		
		super(gsm);
		init();
		
	}
	
	public void init() {
		
		xOptions = 220;
		xSubOptions = 760;
		xIndicator = xOptions - 30;
		ticks = 0;
		ticksSaved = 20;
		
		difficultyOption = OptionsData.getDifficulty();
		soundOption = OptionsData.getSound();
		resolutionOption = OptionsData.getResolution();
		
		// init the images loader
		ImagesLoader.loadFromFile("options_imagenes.txt");
		bg = new Background(
				0,
				0,
				ImagesLoader.getImage("bgoptions"),
				Background.MOVEUP
		);
		bg.setVector(0, 2);
		
		// init the audio
		AudioLoader.loadFromFile("options_sonido.txt");
		AudioPlayer.playAndLoop("optionsmusic");
		
	}
	
	public void update() {

		input();
		bg.update();
		if(up || down || left || right) ticks = 0;
		if(down) {
			AudioPlayer.play("menu_mover");
			currentOption++;
			if(currentOption > NUM_OPTIONS - 1) currentOption = 0;
		}
		if(up) {
			AudioPlayer.play("menu_mover");
			currentOption--;
			if(currentOption < 0) currentOption = NUM_OPTIONS - 1;
		}
		if(left) {
			if(currentOption == DIFFICULTY) {
				AudioPlayer.play("menu_change");
				difficultyOption--;
				if(difficultyOption < 0) difficultyOption = NUM_DIFFICULTIES - 1;
			}
			if(currentOption == SOUND) {
				AudioPlayer.play("menu_change");
				soundOption--;
				if(soundOption < 0) soundOption = NUM_SOUNDOPTIONS - 1;
			}
			if(currentOption == RESOLUTION) {
				AudioPlayer.play("menu_change");
				resolutionOption--;
				if(resolutionOption < 0) resolutionOption = NUM_RESOLUTIONS - 1;
			}
		}
		if(right) {
			if(currentOption == DIFFICULTY) {
				AudioPlayer.play("menu_change");
				difficultyOption++;
				if(difficultyOption > NUM_DIFFICULTIES - 1) difficultyOption = 0;
			}
			if(currentOption == SOUND) {
				AudioPlayer.play("menu_change");
				soundOption++;
				if(soundOption > NUM_SOUNDOPTIONS - 1) soundOption = 0;
			}
			if(currentOption == RESOLUTION) {
				AudioPlayer.play("menu_change");
				resolutionOption++;
				if(resolutionOption > NUM_RESOLUTIONS - 1) resolutionOption = 0;
			}
		}
		if(enter) {
			if(currentOption == SAVE) {
				AudioPlayer.play("menu_ok");
				// check new difficulty
				if(difficultyOption == EASY) {
					
				}
				if(difficultyOption == NORMAL) {
					
				}
				if(difficultyOption == HARD) {
					
				}
				// check sound option
				if(soundOption == ON) {
					AudioPlayer.setMuted(false);
					if(!AudioPlayer.isPlaying("optionsmusic")) {
						System.out.println("Music is now playing");
						AudioPlayer.playAndLoop("optionsmusic");
					}
				}
				if(soundOption == OFF) {
					AudioPlayer.stopAll();
					AudioPlayer.setMuted(true);
				}
				// check new resolution
				if(resolutionOption != OptionsData.getResolution()) {
					if(resolutionOption == FULLSCREEN) ColesteRage.changeWindowSize(GamePanel.FULLSCREEN);
					if(resolutionOption == RES1280X720) ColesteRage.changeWindowSize(GamePanel.SCALE1280X720);
					if(resolutionOption == RES960X540) ColesteRage.changeWindowSize(GamePanel.SCALE960X540);
					if(resolutionOption == RES640X360) ColesteRage.changeWindowSize(GamePanel.SCALE640X360);
				}
				
				// save changes to OptionsData
				OptionsData.setDifficulty(difficultyOption);
				OptionsData.setSound(soundOption);
				OptionsData.setResolution(resolutionOption);
				
				ticksSaved = 0;
			}
			if(currentOption == BACK) {
				AudioPlayer.play("menu_back");
				AudioPlayer.stop("optionsmusic");
				ImagesLoader.removeImages("options_imagenes.txt");
				AudioLoader.removeAudio("options_sonido.txt");
				gsm.setState(GameStateManager.MENU);
			}
		}
		ticks++;
		ticksSaved++;
		ticks %= 16;
		if(ticksSaved > 20) ticksSaved = 20;
		
	}
	
	public void draw(Graphics2D g) {
		
		bg.draw(g);
		Text.drawString(g, "Opciones", Text.CENTERED, 100, "leishoFont", null);
		// main option
		Text.drawString(g, "Dificultad", xOptions, 300, "leishoFont", null);
		// suboption
		if(currentOption == DIFFICULTY) {
			if(difficultyOption == EASY) Text.drawString(g, "< Fácil >", xSubOptions, 300, "leishoFont", null);
			if(difficultyOption == NORMAL) Text.drawString(g, "< Normal >", xSubOptions, 300, "leishoFont", null);
			if(difficultyOption == HARD) Text.drawString(g, "< Difícil >", xSubOptions, 300, "leishoFont", null);
		}
		else {
			if(difficultyOption == EASY) Text.drawString(g, "   Fácil", xSubOptions, 300, "leishoFont", null);
			if(difficultyOption == NORMAL) Text.drawString(g, "   Normal", xSubOptions, 300, "leishoFont", null);
			if(difficultyOption == HARD) Text.drawString(g, "   Difícil", xSubOptions, 300, "leishoFont", null);
		}
		
		// main option
		Text.drawString(g, "Sonido", xOptions, 350, "leishoFont", null);
		// suboption
		if(currentOption == SOUND) {
			if(soundOption == ON) Text.drawString(g, "< On >", xSubOptions, 350, "leishoFont", null);
			if(soundOption == OFF) Text.drawString(g, "< Off >", xSubOptions, 350, "leishoFont", null);
		}
		else {
			if(soundOption == ON) Text.drawString(g, "   On", xSubOptions, 350, "leishoFont", null);
			if(soundOption == OFF) Text.drawString(g, "   Off", xSubOptions, 350, "leishoFont", null);
		}
		
		// main option
		Text.drawString(g, "Resolución", xOptions, 400, "leishoFont", null);
		// suboption
		if(currentOption == RESOLUTION) {
			if(resolutionOption == FULLSCREEN) Text.drawString(g, "< Fullscreen >", xSubOptions, 400, "leishoFont", null);
			if(resolutionOption == RES1280X720) Text.drawString(g, "< 1280x720 >", xSubOptions, 400, "leishoFont", null);
			if(resolutionOption == RES960X540) Text.drawString(g, "< 960x540 >", xSubOptions, 400, "leishoFont", null);
			if(resolutionOption == RES640X360) Text.drawString(g, "< 640x360 >", xSubOptions, 400, "leishoFont", null);
		}
		else {
			if(resolutionOption == FULLSCREEN) Text.drawString(g, "   Fullscreen", xSubOptions, 400, "leishoFont", null);
			if(resolutionOption == RES1280X720) Text.drawString(g, "   1280x720", xSubOptions, 400, "leishoFont", null);
			if(resolutionOption == RES960X540) Text.drawString(g, "   960x540", xSubOptions, 400, "leishoFont", null);
			if(resolutionOption == RES640X360) Text.drawString(g, "   640x360", xSubOptions, 400, "leishoFont", null);
		}
		// save changes
		Text.drawString(g, "Realizar cambios", xOptions, 500, "leishoFont", null);
		// saved confirmation
		if(ticksSaved < 20) Text.drawString(g, "  Hecho", xSubOptions, 500, "leishoFont", null);
		// go back option
		Text.drawString(g, "Regresar", xOptions, 550, "leishoFont", null);
		
		// draw the indicator
		if(currentOption == DIFFICULTY)
			if(ticks < 8)
				Text.drawString(g, Character.toString((char)16), xIndicator, 300, "leishoFont", null);
		if(currentOption == SOUND)
			if(ticks < 8)
				Text.drawString(g, Character.toString((char)16), xIndicator, 350, "leishoFont", null);
		if(currentOption == RESOLUTION)
			if(ticks < 8)
				Text.drawString(g, Character.toString((char)16), xIndicator, 400, "leishoFont", null);
		if(currentOption == SAVE)
			if(ticks < 8)
				Text.drawString(g, Character.toString((char)16), xIndicator, 500, "leishoFont", null);
		if(currentOption == BACK)
			if(ticks < 8)
				Text.drawString(g, Character.toString((char)16), xIndicator, 550, "leishoFont", null);
		
		
	}
	public void input() {
		
		up = Keys.isPressed(Keys.UP);
		down = Keys.isPressed(Keys.DOWN);
		left = Keys.isPressed(Keys.LEFT);
		right = Keys.isPressed(Keys.RIGHT);
		enter = Keys.isPressed(Keys.ENTER);
		if(Keys.isPressed(Keys.ESCAPE)) {
			AudioPlayer.stop("optionsmusic");
			ImagesLoader.removeImages("options_imagenes.txt");
			gsm.setState(GameStateManager.MENU);
		}
		/*
		if(Keys.isPressed(Keys.DOWN)) currentOption++;
		if(Keys.isPressed(Keys.UP)) currentOption--;
		if(currentOption > NUM_OPTIONS - 1) currentOption = 0;
		if(currentOption < 0) currentOption = NUM_OPTIONS - 1;
		if(Keys.isPressed(Keys.ENTER)) {
			if(currentOption == RESOLUTION) {
				ColesteRage.changeWindowSize(0.9);
			}
		}
		*/
	}
	
	public void clearAll() {
    	ImagesLoader.clearImages();
    	AudioPlayer.clearAudio();
    }
	
	
	
}