package Data;

public class PlayerData {

	private static int score = 0;
	private static int eatenDishes = 0;
	
	public static int getScore() { return score; }
	public static int getEatenDishes() { return eatenDishes; }
	public static void setScore(int i) { score = i; }
	public static void addEatenDishes(int i) { eatenDishes += i; }
	public static void setEatenDishes(int i) { eatenDishes = i; }
	
}
