package GameStates;
import java.awt.Graphics2D;
import java.awt.Color;

import Game.GamePanel;
import Images.ImagesLoader;
import Keyboard.Keys;
import Text.Text;
import Text.TextAnimation;

public class Story6State extends GameState {
	
	// images loader
	private ImagesLoader imsLoader;
	
	
	// next image flag
	private boolean next;
	private boolean transitioning;
	private boolean lastDialog;
	
	// current image being shown
	private int currentImage;
	
	// current dialog being shown
	private int currentDialog;
	
	private final String NAME = "historia";
	
	// dialogs
	private final int NUM_DIALOGS = 5;
	private String[] dialogs;
	
	private int ticks;
	
	private int alpha;
	private final int FADEIN = 80;
	private final int FADEOUT = 80;
	// private final int LENGTH;
	
	private TextAnimation txtAnimation;
	
	
	public Story6State(GameStateManager gsm) {
		
		super(gsm);
		
		init();
		
	}
	
	public void init() {
		
		// debugging
		System.out.println("Story mode loaded");
		
		currentImage = 0;
		
		imsLoader = new ImagesLoader("final_historia.txt");
		
		dialogs = new String[NUM_DIALOGS];
		
		dialogs[0] = "Esta fue la �ltima parada de Bodoque.";
		dialogs[1] = "Despu�s de ganar el concurso, su mam� logr� alcanzarlo pero fue muy tarde...";
		dialogs[2] = "Bodoque ten�a sus arterias llenas de colesterol y estaba al borde del infarto.";
		dialogs[3] = "La se�ora pidi� una ambulancia, y se llevaron al pobre Bodoque al hospital.";
		dialogs[4] = "Inmediatamente le realizaron una cirug�a a coraz�n abierto.";
		
		
		txtAnimation = new TextAnimation(60);
		
		transitioning = true;
		
	}
	
	
	public void update() {
		input();
		
		if(transitioning) ticks++;
		
		if(ticks < FADEIN) alpha = (int)(255 - 255 * (1.0 * ticks / FADEIN));
		if(ticks > FADEIN && ticks < FADEIN + FADEOUT) alpha = (int)(255 * ((1.0 * ticks - FADEIN) / FADEOUT));
		if(alpha < 0) alpha = 0;
		if(alpha > 255) alpha = 255;
		
		if(ticks == FADEIN) transitioning = false;
		if(ticks == FADEIN + FADEOUT) transitioning = false;
		
		if(next) {
			next = false;
			if(!lastDialog) {
				currentDialog++;
				if(currentDialog == 2 || currentDialog == 4)
					currentImage++;
				txtAnimation.reset();
			}
			// debugging
			System.out.println("Current image: " + currentImage);
		}
		
		if(currentDialog == dialogs.length - 1) lastDialog = true;
		
		if(lastDialog) currentDialog = dialogs.length - 1;
		
		txtAnimation.update();
		
		// System.out.println("Current tick: " + ticks);
		if(ticks == FADEIN + FADEOUT) gsm.setState(GameStateManager.CREDITS);
		
	}
	
	public void draw(Graphics2D g) {
		
		
		//g.drawImage(imsLoader.getImage("historia3"), 0, 0, null);
		
		g.drawImage(imsLoader.getImage(NAME + currentImage), 0, 0, null);
		
		g.drawImage(
				imsLoader.getImage("dialogBox"),
				GamePanel.WIDTH / 2 - imsLoader.getImage("dialogBox").getWidth() / 2,
				580 - imsLoader.getImage("dialogBox").getHeight() / 2,
				null
		);
		//g.setColor(new Color(0, 0, 0, 128));
		//g.fillRect(20, 500, GamePanel.WIDTH - 100, 200);
		
		Text.drawString(g, dialogs[currentDialog], 70, 460, "leishoFont", txtAnimation);
		
		g.setColor(new Color(0, 0, 0, alpha));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		
		
	}
	
	public void input() {
		
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
		
	}
	
}
