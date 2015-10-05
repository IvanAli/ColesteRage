package Data;

import GameStates.OptionsState;

public class OptionsData {

	private static int difficulty = OptionsState.NORMAL;
	private static int sound = OptionsState.ON;
	private static int resolution = OptionsState.RES960X540;
	
	public static int getDifficulty() { return difficulty; }
	public static int getSound() { return sound; }
	public static int getResolution() { return resolution; }
	
	public static void setDifficulty(int i) { difficulty = i; }
	public static void setSound(int i) { sound = i; }
	public static void setResolution(int i) { resolution = i; }
	
}
