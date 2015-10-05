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
		
		dialogs[0] = "Esta es la historia de Bodoque, un niño que desde pequeño fue notorio que era diferente.";
		dialogs[1] = "Bodoque amaba comer, él comía a todas horas: día y noche sin parar.";
		dialogs[2] = "Su mamá estaba realmente preocupada por la exageración de su pequeño Bodoque, "
				+ "a tal grado de que lo llevó al médico para saber si había algún problema con que comiera mucho.";
		dialogs[3] = "El Doctor le dijo a la mamá que no había que temer, mientras que Bodoque "
				+ "comiera comida natural y saludable. ";
		dialogs[4] = "La mamá estuvo aliviada.";
		dialogs[5] = "Sin embargo el Doctor le advirtió que si comía "
				+ "en algún momento comida chatarra, Bodoque podría sufrir un colapso nervioso.";
		dialogs[6] = "Y su amor "
				+ "por la comida se transformaría en un amor por la comida chatarra.";
		dialogs[7] = "Han pasado ya 8 años sin ningún accidente y ahora Bodoque tiene 13 años, la edad donde "
				+ "quieren saber más del mundo que los rodea.";
		dialogs[8] = "Un día, Bodoque se encontraba preparando su lunch para la escuela, "
				+ "cuando de repente ¡vio una caja escondida en la alacena!";
		dialogs[9] = "Eran donas Chispy Crim.";
		dialogs[10] = "Bodoque quería saber a qué sabían esos aros de pan con brillo encima, "
				+ "y se atrevió a probar una Chispy Crim.";
		dialogs[11] = "¡Bodoque no volvería a ser el mismo, debido a que entró en un colapso nervioso inducido por el placer "
				+ "absoluto del exquisito sabor de las donas prohibidas!";
		dialogs[12] = "En ese momento, el sexto sentido de la mamá se activó, al sentir que su pequeño estaba en peligro. "
				;
		dialogs[13] = "Fue de inmediato a su casa, pero ya era muy tarde.";
		dialogs[14] = "Bodoque había entrado en un ColesteRage.";
		
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
