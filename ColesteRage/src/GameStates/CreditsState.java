package GameStates;
import java.awt.Graphics2D;
import java.awt.Color;

import Game.GamePanel;
import Keyboard.Keys;
import Text.Text;

public class CreditsState extends GameState {
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	private int ticks;
	
	public CreditsState(GameStateManager gsm) {
		
		super(gsm);
		init();
		
	}
	
	public void init() {
		y = GamePanel.HEIGHT;
		dy = -2;
	}
	
	public void update() {
		input();
		x += dx;
		y += dy;
		if(y + 1090 < 0) gsm.setState(GameStateManager.NAMEENTRY);
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		Text.drawString(g, "ColesteRage", Text.CENTERED, (int)y, "leishoFont", null);
		Text.drawString(g, "PROGRAMACI�N", Text.CENTERED, (int)(y + 150), "leishoFont", null);
		Text.drawString(g, "Iv�n Alejandro Soto Vel�zquez", Text.CENTERED, (int)(y + 190), "leishoFont", null);
		Text.drawString(g, "CONSTRUCCI�N DE GR�FICAS", Text.CENTERED, (int)(y + 260), "leishoFont", null);
		Text.drawString(g, "Gustavo Guti�rrez G�mez", Text.CENTERED, (int)(y + 300), "leishoFont", null);
		Text.drawString(g, "DOCUMENTACI�N", Text.CENTERED, (int)(y + 370), "leishoFont", null);
		Text.drawString(g, "Gerardo Garc�a S�nchez", Text.CENTERED, (int)(y + 410), "leishoFont", null);
		Text.drawString(g, "ARTE", Text.CENTERED, (int)(y + 480), "leishoFont", null);
		Text.drawString(g, "Rodrigo Reyes Murillo", Text.CENTERED, (int)(y + 520), "leishoFont", null);
		Text.drawString(g, "RECURSOS PRESTADOS", Text.CENTERED, (int)(y + 590), "leishoFont", null);
		Text.drawString(g, "Gr�ficos", Text.CENTERED, (int)(y + 650), "leishoFont", null);
		Text.drawString(g, "Lunar: Silvar Star Harmony - Game Arts", Text.CENTERED, (int)(y + 690), "leishoFont", null);
		Text.drawString(g, "M�sica", Text.CENTERED, (int)(y + 750), "leishoFont", null);
		Text.drawString(g, "Mario games - Nintendo", Text.CENTERED, (int)(y + 790), "leishoFont", null);
		Text.drawString(g, Character.toString((char)169) + "2015 INTENSE FORCE", Text.CENTERED, (int)(y + 1000), "leishoFont", null);
	}
	
	public void input() {
		if(Keys.keyState[Keys.ENTER]) {
			if(y + 1030 > 0) dy = -8;
		}
		else dy = -2;
	}
	
	
}
