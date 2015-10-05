package GameStates;

import Audio.AudioLoader;
import Audio.AudioPlayer;
import Game.GamePanel;
import Images.ImagesLoader;
import Keyboard.Keys;
import Text.Text;

import java.awt.Graphics2D;

public class InstructionsState extends GameState {
	
	private double x;
	private double y;
	private double xdest;
	private double ydest;
	private double dx;
	private double dy;
	
	private double moveSpeed;
	private double maxSpeed;
	
	private int width;
	private int height;
	
	private boolean next;
	private boolean previous;
	private boolean transitioning;
	
	private int currentImage;
	private final int IMAGES_NUMBER = 4;
	
	private int ticks;
	
	public InstructionsState(GameStateManager gsm) {
		
		super(gsm);
		init();
		
	}
	
	public void init() {
		ImagesLoader.loadFromFile("instrucciones_imagenes.txt");
		AudioLoader.loadFromFile("instrucciones_sonido.txt");
		
		// play audio
		AudioPlayer.playAndLoop("instruccionesmusic");
		
		width = 1280;
		height = 720;
		
		moveSpeed = 3;
		maxSpeed = 70;
	}
	
	public void update() {
		input();
		
		ticks++;
		ticks = ticks % 18;
		
		x += dx;
		y += dy;
		xdest = x + dx;
		ydest = y + dy;
		
		if(next) {
			transitioning = true;
			dx += moveSpeed;
			if(dx > maxSpeed) dx = maxSpeed;
			if(xdest > width * (currentImage + 1)) {
				x = width * (currentImage + 1);
				dx = 0;
				currentImage++;
				next = transitioning = false;
			}
		}
		if(previous) {
			transitioning = true;
			dx -= moveSpeed;
			if(dx < -maxSpeed) dx = -maxSpeed;
			if(xdest < width * (currentImage - 1)) {
				x = width * (currentImage - 1);
				dx = 0;
				currentImage--;
				previous = transitioning = false;
			}
		}
		
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(
				ImagesLoader.getImage("instructions"), 
				0, 
				0, 
				GamePanel.WIDTH, 
				GamePanel.HEIGHT, 
				(int)x, 
				0, 
				(int)x + width, 
				GamePanel.HEIGHT, 
				null
		);
		if(ticks < 9)
			Text.drawString(g, "<<    Presiona Enter para regresar    >>", Text.CENTERED, 640, "leishoFont", null);
		Text.drawString(g, currentImage + 1 + "/" + IMAGES_NUMBER, 1150, 640, "leishoFont", null);
	}
	
	public void input() {
		if(transitioning) return;
		
		if(Keys.isPressed(Keys.RIGHT)) {
			if(currentImage < IMAGES_NUMBER - 1) {
				next = true;
				AudioPlayer.play("menu_mover");
			}
		}
		if(Keys.isPressed(Keys.LEFT)) {
			if(currentImage > 0) {
				previous = true;
				AudioPlayer.play("menu_mover");
			}
		}
		
		if(Keys.isPressed(Keys.ENTER)) {
			AudioPlayer.play("menu_back");
			AudioPlayer.stop("instruccionesmusic");
			ImagesLoader.removeImages("instrucciones_imagenes.txt");
			AudioLoader.removeAudio("instrucciones_sonido.txt");
			gsm.setState(GameStateManager.MENU);
		}
	}
}
