package GameStates;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import Audio.AudioLoader;
import Audio.AudioPlayer;
import Background.Background;
import Data.PlayerData;
import Game.GamePanel;
import Images.ImagesLoader;
import Keyboard.Keys;
import StateMisc.ReadPlayers;
import StateMisc.User;
import Text.Letter;
import Text.Text;

public class NameEntryState extends GameState {
	
	private HashMap<String, Integer> top;
	//private User[] topPlayers;
	private ArrayList<User> topPlayers;
	private Letter[][] letterMatrix;
	private int currentOptionx;
	private int currentOptiony;
	private int currentLetter;
	
	// background
	private Background foodPattern;
	
	// animation ticks and alpha
	private int ticks;
	private int ticksFadeOut;
	private int alpha;
	private boolean transitioning;
	
	// width
	private int textwidth;
	
	// letter boolean
	private boolean letterTyped;
	
	private String playerName;
	
	// made the top 5?
	private boolean topFive;
	
	// done saving
	private boolean saved;
	
	
	private final String fnm = "topplayers.txt";
	
	public NameEntryState(GameStateManager gsm) {
		
		super(gsm);
		init();
		
	}
	
	public void init() {
		ImagesLoader.loadFromFile("nameentry_imagenes.txt");
		AudioLoader.loadFromFile("nameentry_sonido.txt");
		
		// play the audio
		AudioPlayer.playAndLoop("nameentrymusic");
		// init the background
		
		foodPattern = new Background(
				0, 
				0, 
				ImagesLoader.getImage("foodpattern"), 
				Background.MOVEUP
		);
		foodPattern.setVector(0, 2);
		
		top = new HashMap<>();
		//topPlayers = new User[5];
		topPlayers = new ArrayList<>();
		playerName = "";
		currentLetter = 65;
		ReadPlayers.readTopPlayers(fnm);
		topPlayers = ReadPlayers.getTopPlayers();
		topFive = isTopFive();
		if(topFive) createLetters("leishoFont");
	}
	/*
	public void readTopPlayers(String s) {
		InputStream in = getClass().getResourceAsStream("/TopPlayers/" + s);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		try {
			while((line = br.readLine()) != null) {
				String[] tokens;
				//line = br.readLine();
				if(line.length() == 0) continue;
				if(line.startsWith("//")) {
					//i--;
					continue;
				}
				tokens = line.split(",");
				System.out.println("Token 0: " + tokens[0]);
				System.out.println("Token 1: " + tokens[1]);
				//topPlayers[i] = new User(tokens[0], Integer.parseInt(tokens[1]));
				topPlayers.add(new User(tokens[0], Integer.parseInt(tokens[1])));
			}
			br.close();
		}
		catch(Exception e) { e.printStackTrace(); }
		System.out.println("List length: " + topPlayers.size());
		//sortTopPlayers(topPlayers);
		
	}*/
	
	public void createLetters(String fontName) {
		Letter[] letters = new Letter[52];
		letterMatrix = new Letter[4][13];
		for(int i = 0; i < 26; i++) {
			char c = (char)(i + 65);
			//System.out.println("Char: " + c);
			letters[i] = new Letter(
					c, 
					Text.getFont(fontName).getLetter(c).getImage()
			);
		}
		for(int i = 26; i < 52; i++) {
			char c = (char)(i - 26 + 97);
			//System.out.println("Char: " + c);
			letters[i] = new Letter(
					c, 
					Text.getFont(fontName).getLetter(c).getImage()
			);
		}
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 13; j++) {
				letterMatrix[i][j] = letters[j + i * (13)];
				//System.out.println("Matrix char: " + letterMatrix[i][j].getCh());
				letterMatrix[i][j].setx(100 + j * 90);
				letterMatrix[i][j].sety(300 + i * 90);
			}
		}
	}
	
	public void sortTopPlayers(User[] players) {
		/*
		int max = players[0].getScore();
		for(int i = 0; i < players.length; i++) {
			
		}*/
		Arrays.sort(players, new Comparator<User>() {
			@Override
				public int compare(User u1, User u2) {
					if(u1.getScore() > u2.getScore()) return u1.getScore(); 
					return u2.getScore();
				}
			}	
		);
	}
	
	public void update() {
		
		input();
		foodPattern.update();
		ticks++;
		ticks %= 18;
		if(letterTyped) {
			AudioPlayer.play("menu_ok");
			playerName += letterMatrix[currentOptiony][currentOptionx].getCh();
			currentLetter = playerName.length();
			textwidth += Text.getFont("leishoFont").getLetter(playerName.charAt(currentLetter - 1)).getWidth();
			letterTyped = false;
		}
		if(transitioning) {
			ticksFadeOut++;
			alpha = (int)(255 * (ticksFadeOut * 1.0 / 40));
			if(alpha > 255) alpha = 255;
			if(ticksFadeOut > 40) {
				ImagesLoader.removeImages("nameentry_imagenes.txt");
				AudioLoader.removeAudio("nameentry_sonido.txt");
				gsm.setState(GameStateManager.MENU);
			}
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		if(saved || !topFive) {
			g.drawImage(
					ImagesLoader.getImage("seeyou"),
					0,
					0,
					null
			);
		}
		else if(topFive) {
			// draw the background
			//g.drawImage(ImagesLoader.getImage("foodpattern"), 0, 0, null);
			
			// draw the animated background
			foodPattern.draw(g);
			
			// draw a black box
			g.setColor(new Color(0, 0, 0, 200));
			g.fillRect(
					GamePanel.WIDTH / 2 - 600, 
					470 - 210, 
					1200, 
					420
			);
			g.setStroke(new BasicStroke(7));
			g.setColor(Color.white);
			g.drawRect(
					GamePanel.WIDTH / 2 - 600, 
					470 - 210, 
					1200, 
					420
			);
			
			// draw the score
			Text.drawString(g, "Score: " + PlayerData.getScore(), Text.CENTERED, 150, "leishoFont", null);
			
			// draw the text
			Text.drawString(g, "¡Felicidades!", Text.CENTERED, 50, "leishoFont", null);
			Text.drawString(g, "Has hecho el top 5", Text.CENTERED, 100, "leishoFont", null);
			/*
			for(int i = 0; i < topPlayers.length; i++) {
				Text.drawString(g, topPlayers[i].getName(), 200, 200 + i * 70, "leishoFont", null);
				Text.drawString(g, String.valueOf(topPlayers[i].getScore()), 800, 200 + i * 70, "leishoFont", null);
			}*/
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 13; j++) {
					Letter l = letterMatrix[i][j];
					g.drawImage(
							l.getImage(),
							(int)l.getx() - l.getImage().getWidth() / 2,
							(int)l.gety() - l.getImage().getHeight() / 2,
							null
					);
				}
			}
			if(currentOptiony == 4) {
				if(currentOptionx == 11) {
					if(ticks < 9) {
						Text.drawString(g, "DEL", 1040, 620, "leishoFont", null);
					}
					Text.drawString(g, "OK", 1160, 620, "leishoFont", null);
				}
				if(currentOptionx == 12) {
					if(ticks < 9) {
						Text.drawString(g, "OK", 1160, 620, "leishoFont", null);
					}
					Text.drawString(g, "DEL", 1040, 620, "leishoFont", null);
				}
			}
			else {
				Text.drawString(g, "OK", 1160, 620, "leishoFont", null);
				Text.drawString(g, "DEL", 1040, 620, "leishoFont", null);
			}
			
			g.setColor(Color.blue);
			g.setStroke(new BasicStroke(7));
			/*
			g.drawRect(
					100 + currentOptionx * 90 - 25, 
					300 + currentOptiony * 90 - 25, 
					50, 
					50
			);
			*/
			if(currentOptiony < 4)
			g.drawImage(
					ImagesLoader.getImage("letterBox"),
					100 + currentOptionx * 90 - ImagesLoader.getImage("letterBox").getWidth() / 2,
					300 + currentOptiony * 90 - ImagesLoader.getImage("letterBox").getHeight() / 2,
					null
			);
			
			// draw the textbox
			g.drawImage(
					ImagesLoader.getImage("textbox"),
					GamePanel.WIDTH / 2 + 30 - ImagesLoader.getImage("textbox").getWidth() / 2,
					225 - ImagesLoader.getImage("textbox").getHeight() / 2,
					null
			);
			
			// draw text to the left of the box
			Text.drawString(g, "Jugador:", 250, 195, "leishoFont", null);
			
			// draw the player's name
			Text.drawString(g, playerName, 510, 198, "leishoFont", null);
			
			// draw the underscore
			g.setColor(new Color(0, 102, 204));
			
			if(ticks < 9)
				Text.drawString(g, playerName + "_", 510, 198, "leishoFont", null);
				/*
				g.fillRect(
						450 + textwidth, 
						210, 
						35, 
						5
				);*/
			
		}
		
		if(transitioning) {
			g.setColor(new Color(0, 0, 0, alpha));
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		}
	
	}
	
	public void input() {
		if(saved || !topFive) {
			if(Keys.isPressed(Keys.ENTER)) transitioning = true;
		}
		else if(topFive) {
			if(Keys.isPressed(Keys.LEFT)) {
				AudioPlayer.play("menu_mover2");
				currentOptionx--;
				if(currentOptionx < 0) currentOptionx = 12;
			}
			if(Keys.isPressed(Keys.RIGHT)) {
				AudioPlayer.play("menu_mover2");
				currentOptionx++;
				if(currentOptionx > 12) currentOptionx = 0;
			}
			if(Keys.isPressed(Keys.UP)) {
				AudioPlayer.play("menu_mover2");
				currentOptiony--;
				if(currentOptiony < 0) {
					currentOptiony = 4;
					if(currentOptionx < 11) currentOptionx = 11;
				}
			}
			if(Keys.isPressed(Keys.DOWN)) {
				AudioPlayer.play("menu_mover2");
				currentOptiony++;
				if(currentOptiony > 4) currentOptiony = 0;
				if(currentOptiony == 4) {
					if(currentOptionx < 11) currentOptionx = 11;
				}
			}
			if(Keys.isPressed(Keys.ENTER)) {
				if(currentOptionx == 12 && currentOptiony == 4) {
					AudioPlayer.play("menu_accept");
					saveNewData(fnm);
					saved = true;
					AudioPlayer.stop("nameentrymusic");
				}
				else if(currentOptionx == 11 && currentOptiony == 4) {
					AudioPlayer.play("menu_back");
					if(playerName.length() > 0)
						playerName = playerName.substring(0, playerName.length() - 1);
				}
				else if(playerName.length() < 9) letterTyped = true;
			}
		}
	}
	
	public boolean isTopFive() {
		for(int i = 0; i < topPlayers.size(); i++) 
			if(topPlayers.get(i).getScore() < PlayerData.getScore()) 
				return true;
		return false;
	}
	
	public void saveNewData(String s) {
		System.out.println("Saving data");
		for(int i = 0; i < topPlayers.size(); i++) {
			System.out.println("Top player " + (i + 1) + ": " + topPlayers.get(i).getScore() + 
					". My score: " + PlayerData.getScore());
			if(topPlayers.get(i).getScore() < PlayerData.getScore()) {
				System.out.println("Player is within top 5");
				topPlayers.add(i, new User(playerName, PlayerData.getScore()));
				topPlayers.remove(topPlayers.size() - 1);
				break;
			}
		}
		System.out.println("TOP PLAYERS: ");
		for(int i = 0; i < topPlayers.size(); i++) {
			System.out.println(topPlayers.get(i).getName());
		}
		try {
			PrintWriter writer = 
					new PrintWriter(
		                     new File(this.getClass().getResource("/TopPlayers/" + s).getPath()));
			for(int i = 0; i < topPlayers.size(); i++) {
				User u = topPlayers.get(i);
				writer.write(u.getName() + "," + u.getScore() + "\n");
			}
			System.out.println("Data saved");
			writer.close();
		}
		catch(Exception e) { e.printStackTrace(); }
		
		// clear data from this game session
		PlayerData.clearData();
	}
	
	public void clearAll() {
    	ImagesLoader.clearImages();
    	AudioPlayer.clearAudio();
    }
	
}
