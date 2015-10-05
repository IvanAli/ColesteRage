package StateMisc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReadPlayers {

	private static ArrayList<User> topPlayers;
	
	public static void readTopPlayers(String s) {
		if(topPlayers == null) topPlayers = new ArrayList<>();
		else return;
		InputStream in = ReadPlayers.class.getResourceAsStream("/TopPlayers/" + s);
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
	}
	
	public static User getPlayerAt(int index) {
		return topPlayers.get(index);
	}
	
	public static ArrayList<User> getTopPlayers() { return topPlayers; }
	public static int playersSize() { return topPlayers.size(); }
}
