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

public class Story1State extends GameState {
	
	// next image flag
	private boolean next;
	private boolean transitioning;
	private boolean lastDialog;
	
	// current image being shown
	private int currentImage;
	
	// current dialog being shown
	private int currentDialog;
	
	private final String NAME = "historia";
	private final String musicFnm = "story1music";
	
	// dialogs
	private final int NUM_DIALOGS = 15;
	private String[] dialogs;
	
	// ticks
	private int ticks;
	private int enterTicks;
	
	private int alpha;
	private final int FADEIN = 40;
	private final int FADEOUT = 40;
	// private final int LENGTH;
	
	private TextAnimation txtAnimation;
	
	
	public Story1State(GameStateManager gsm) {
		
		super(gsm);
		
		init();
		
	}
	
	public void init() {
		
		// debugging
		System.out.println("Story mode loaded");
		
		alpha = 255;
		
		currentImage = 0;
		
		ImagesLoader.loadFromFile("historia1_imagenes.txt");
		AudioLoader.loadFromFile("historia1_sonido.txt");
		
		AudioPlayer.playAndLoop(musicFnm);
		
		dialogs = new String[NUM_DIALOGS];
		
		dialogs[0] = "Esta es la historia de Bodoque, un ni�o que desde peque�o fue notorio que era diferente.";
		dialogs[1] = "Bodoque amaba comer, �l com�a a todas horas: d�a y noche sin parar.";
		dialogs[2] = "Su mam� estaba realmente preocupada por la exageraci�n de su peque�o Bodoque, "
				+ "a tal grado de que lo llev� al m�dico para saber si hab�a alg�n problema con que comiera mucho.";
		dialogs[3] = "El Doctor le dijo a la mam� que no hab�a que temer, mientras que Bodoque "
				+ "comiera comida natural y saludable. ";
		dialogs[4] = "La mam� estuvo aliviada.";
		dialogs[5] = "Sin embargo el Doctor le advirti� que si com�a "
				+ "en alg�n momento comida chatarra, Bodoque podr�a sufrir un colapso nervioso.";
		dialogs[6] = "Y su amor "
				+ "por la comida se transformar�a en un amor por la comida chatarra.";
		dialogs[7] = "Han pasado ya 8 a�os sin ning�n accidente y ahora Bodoque tiene 13 a�os, la edad donde "
				+ "quieren saber m�s del mundo que los rodea.";
		dialogs[8] = "Un d�a, Bodoque se encontraba preparando su lunch para la escuela, "
				+ "cuando de repente �vio una caja escondida en la alacena!";
		dialogs[9] = "Eran donas Chispy Crim.";
		dialogs[10] = "Bodoque quer�a saber a qu� sab�an esos aros de pan con brillo encima, "
				+ "y se atrevi� a probar una Chispy Crim.";
		dialogs[11] = "�Bodoque no volver�a a ser el mismo, debido a que entr� en un colapso nervioso inducido por el placer "
				+ "absoluto del exquisito sabor de las donas prohibidas!";
		dialogs[12] = "En ese momento, el sexto sentido de la mam� se activ�, al sentir que su peque�o estaba en peligro. "
				;
		dialogs[13] = "Fue de inmediato a su casa, pero ya era muy tarde.";
		dialogs[14] = "Bodoque hab�a entrado en un ColesteRage.";
		
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
				if(currentDialog == 3 || currentDialog == 7 || currentDialog == 9 || currentDialog == 11)
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
		if(ticks == FADEIN + FADEOUT) {
			AudioPlayer.stop(musicFnm);
			ImagesLoader.removeImages("historia1_imagenes.txt");
			AudioLoader.removeAudio("historia1_sonido.txt");
			gsm.setState(GameStateManager.LEVEL1);
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
