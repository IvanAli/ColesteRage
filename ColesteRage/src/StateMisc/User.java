package StateMisc;

public class User implements java.io.Serializable {
	
	private String name;
	private int score;
	
	public User(String name, int score) {
		this.name = name;
		this.score = score;
	}
	
	public String getName() { return name; }
	public int getScore() { return score; }
	public void setName(String s) { name = s; }
	public void setScore(int i) { score = i; }
	
}
