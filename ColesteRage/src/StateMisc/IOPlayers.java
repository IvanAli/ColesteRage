package StateMisc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.io.File;

public class IOPlayers {

	private static ArrayList<User> topPlayers;
	private static final String bestfnm = "colesteragebest.cr";
	
	public static void readTopPlayers() {
		if(topPlayers == null) topPlayers = new ArrayList<>();
		else return;
		
		File file = new File(bestfnm);
		if(!file.exists()) {
			System.out.println("File not found. Default top scores will be used.");
			for(int i = 0; i < 5; i++) 
				topPlayers.add(new User("Computer", 10000 - i * 1000));
			return;
		}
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			for(int i = 0; i < 5; i++) {
				User u = (User)ois.readObject();
				topPlayers.add(u);
			}
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public static void writeTopPlayers() {
		try {
			File file = new File(bestfnm);
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			for(int i = 0; i < topPlayers.size(); i++) 
				oos.writeObject(topPlayers.get(i));
			
			oos.flush();
			oos.close();
			
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	/*
	public static void readTopPlayers(String s) {
		if(topPlayers == null) topPlayers = new ArrayList<>();
		else return;
		InputStream in = ReadPlayers.class.getResourceAsStream("/TopPlayers/" + s);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		try {
			while((line = br.readLine()) != null) {
				String[] tokens;
				if(line.length() == 0) continue;
				if(line.startsWith("//")) {
					continue;
				}
				tokens = line.split(",");
				System.out.println("Token 0: " + tokens[0]);
				System.out.println("Token 1: " + tokens[1]);
				topPlayers.add(new User(tokens[0], Integer.parseInt(tokens[1])));
			}
			br.close();
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	*/
	
	public static User getPlayerAt(int index) {
		return topPlayers.get(index);
	}
	
	public static ArrayList<User> getTopPlayers() { return topPlayers; }
	public static int playersSize() { return topPlayers.size(); }
}
