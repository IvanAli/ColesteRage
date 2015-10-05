package GameStates;
import java.awt.Graphics2D;
import java.awt.Color;

import Audio.AudioLoader;
import Audio.AudioPlayer;
import Game.GamePanel;
import Images.ImagesLoader;
import Keyboard.Keys;
import Text.Text;
import Text.TextAnimation;

public class Story5State extends GameState {

	// next image flag
	private boolean next;
	private boolean transitioning;
	private boolean lastDialog;
	
	// current image being shown
	private int currentImage;
	
	// current dialog being shown
	private int currentDialog;
	
	private final String NAME = "historia";
	private final String musicFnm = "story5music";
	
	// dialogs
	private final int NUM_DIALOGS = 2;
	private String[] dialogs;
	
	// ticks
	private int ticks;
	private int enterTicks;
	
	private int alpha;
	private final int FADEIN = 40;
	private final int FADEOUT = 40;
	// private final int LENGTH;
	
	private TextAnimation txtAnimation;
	
	
	public Story5State(GameStateManager gsm) {
		
		super(gsm);
		
		init();
		
	}
	
	public void init() {
		
		alpha = 255;
		
		currentImage = 0;
		
		ImagesLoader.loadFromFile("historia5_imagenes.txt");
		AudioLoader.loadFromFile("historia5_sonido.txt");
		
		AudioPlayer.playAndLoop(musicFnm);
		
		dialogs = new String[NUM_DIALOGS];
		
		dialogs = new String[NUM_DIALOGS];
		
		dialogs[0] = "Al dejar vac�o el restaurante, Bodoque sali� a la calle y encontr� tirado un volante "
				+ "sobre un concurso de comida que se iba a hacer esa misma tarde en el parque de la ciudad.";
		dialogs[1] = "Por lo que Bodoque no dud� ni un segundo y se dirigi� hacia all�.";
		
		
		txtAnimation = new TextAnimation(60);
		
		transitioning = true;
		
	}
	
	
	public void update() {
		input();
		
		if(transitioning) ticks++;
		
		// update the enter key ticks
				enterTicks++;
				enterTicks %= 30;
				
		
		if(ticks < FADEIN) alpha = (int)(255 - 255 * (1.0 * ticks / FADEIN));
		if(ticks > FADEIN && ticks < FADEIN + FADEOUT) alpha = (int)(255 * ((1.0 * ticks - FADEIN) / FADEOUT));
		if(alpha < 0) alpha = 0;
		if(alpha > 255) alpha = 255;
		
		if(ticks == FADEIN) transitioning = false;
		if(ticks == FADEIN + FADEOUT) transitioning = false;
		
		if(next) {
			next = false;
			if(!lastDialog) {
				AudioPlayer.play("story_next");
				currentDialog++;
				/*
				if(currentDialog == 3 || currentDialog == 7 || currentDialog == 9 || currentDialog == 11)
					currentImage++;
				*/
				txtAnimation.reset();
			}
			// debugging
			System.out.println("Current image: " + currentImage);
		}
		
		if(currentDialog == dialogs.length - 1) lastDialog = true;
		
		if(lastDialog) currentDialog = dialogs.length - 1;
		
		txtAnimation.update();
		
		// System.out.println("Current tick: " + ticks);
		if(ticks == FADEIN + FADEOUT) {
			AudioPlayer.stop(musicFnm);
			ImagesLoader.removeImages("historia5_imagenes.txt");
			AudioLoader.removeAudio("historia5_sonido.txt");
			gsm.setState(GameStateManager.LEVEL5);
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		
		//g.drawImage(ImagesLoader.getImage("historia3"), 0, 0, null);
		
		g.drawImage(ImagesLoader.getImage(NAME + currentImage), 0, 0, null);
		
		g.drawImage(
				ImagesLoader.getImage("dialogBox"),
				GamePanel.WIDTH / 2 - ImagesLoader.getImage("dialogBox").getWidth() / 2,
				580 - ImagesLoader.getImage("dialogBox").getHeight() / 2,
				null
		);
		//g.setColor(new Color(0, 0, 0, 128));
		//g.fillRect(20, 500, GamePanel.WIDTH - 100, 200);
		
		Text.drawString(g, dialogs[currentDialog], 70, 460, "leishoFont", txtAnimation);
		
		// draw the enter key
					if(enterTicks < 15)
						g.drawImage(ImagesLoader.getImage("enterkey"), 1120, 395, null);
					
		g.setColor(new Color(0, 0, 0, alpha));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		
		
	}
	
	public void input() {
		if(transitioning) return;
		if(Keys.isPressed(Keys.ENTER)) {
			// some transition before going to level 1
			if(lastDialog) {
				if(!Text.isTyping())
					transitioning = true;
				// debugging
				System.out.println("Last dialog");
				
			}
			if(Text.isTyping()) txtAnimation.setCounter(dialogs[currentDialog].length());
			else next = true;
		}
		if(Keys.isPressed(Keys.ESCAPE)) transitioning = true;
		
	}
	
	public void clearAll() {
    	ImagesLoader.clearImages();
    	AudioPlayer.clearAudio();
    }
	
}
