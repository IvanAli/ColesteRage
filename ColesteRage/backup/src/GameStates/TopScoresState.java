package GameStates;
import java.awt.Graphics2D;
import java.awt.Color;

import Audio.AudioLoader;
import Audio.AudioPlayer;
import Background.Background;
import Game.GamePanel;
import HUD.HUD;
import Images.ImagesLoader;
import Keyboard.Keys;
import StateMisc.ReadPlayers;
import Text.Text;

public class TopScoresState extends GameState {
	
	private ImagesLoader imsLoader;
	private Background bg;
	private Color[] colors;
	
	private AudioPlayer audio;
	
	private int ticks;
	private int colorTick;
	
	
	public TopScoresState(GameStateManager gsm) {
		
		super(gsm);
		init();
		
	}
	
	public void init() {
		
		// init the images loader
		imsLoader = new ImagesLoader("topscores_imagenes.txt");
		bg = new Background(
				0,
				0,
				imsLoader.getImage("bgtopscores"),
				Background.MOVEUP
		);
		bg.setVector(0, 2);
		
		// init the audio
		audio = new AudioPlayer(new AudioLoader("topscores_sonido.txt"));
		
		// colors init
		colors = new Color[5];
		colors[0] = new Color(235, 53, 53, 200);
		colors[1] = new Color(255, 207, 93, 200);
		colors[2] = new Color(212, 255, 93, 200);
		colors[3] = new Color(64, 183, 234, 200);
		colors[4] = new Color(255, 93, 120, 200);
		
		ReadPlayers.readTopPlayers("topplayers.txt");
		
	}
	
	public void update() {
		input();
		bg.update();
		ticks++;
		ticks %= 18;
		if(ticks == 0) {
			colorTick++;
		}
	}
	
	public void draw(Graphics2D g) {
		
		// draw the background
		bg.draw(g);
		
		// draw the names background
		/*g.drawImage(
				imsLoader.getImage("namesbg"),
				GamePanel.WIDTH / 2 - imsLoader.getImage("namesbg").getWidth() / 2,
				380 - imsLoader.getImage("namesbg").getHeight() / 2,
				null
		);*/
		g.setColor(new Color(255, 255, 255, 160));
		g.fillRect(0, 185, GamePanel.WIDTH, 410);
		for(int i = 0; i < colors.length; i++) {
			int pos = (i + colorTick) % colors.length;
			g.setColor(colors[pos]);
			g.fillRect(0, 195 + i * 80, GamePanel.WIDTH, 70);
		}
		
		Text.drawString(g, "Mejores puntajes", Text.CENTERED, 100, "leishoFont", null);
		
		for(int i = 0; i < ReadPlayers.playersSize(); i++) {
			String name = ReadPlayers.getPlayerAt(i).getName();
			String score = HUD.getScore(ReadPlayers.getPlayerAt(i).getScore());
			Text.drawString(
					g,
					(i + 1) + ". " + name, 
					200, 
					200 + i * 80,
					"leishoFont", 
					null
			);
			Text.drawString(
					g,
					score, 
					850, 
					200 + i * 80,
					"leishoFont", 
					null
			);
		}
		if(ticks < 9)
			Text.drawString(g, "Presiona Enter para regresar", Text.CENTERED, 640, "leishoFont", null);
	}
	
	public void input() {
		if(Keys.isPressed(Keys.ENTER)) {
			audio.play("menu_back");
			gsm.setState(GameStateManager.MENU);
		}
	}
}
