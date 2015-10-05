package Background;
import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;

import Game.GamePanel;

public class BackgroundAnimation {
	
	private BufferedImage image;
	private AlphaComposite alphaComposite;
	private float alpha;
	
	private int width;
	private int height;
	private int nwidth;
	private int nheight;
	
	private int ticks;
	
	// play sound flag
	private boolean playSound;
	
	private int type;
	public static final int HEARTATTACK = 0;
	public static final int WARNING = 1;
	
	public BackgroundAnimation(BufferedImage image, int type) {
		this.image = image;
		this.type = type;
		width = nwidth = image.getWidth();
		height = nheight = image.getHeight();
		alpha = 1.0f;
		ticks = 0;
	}
	
	public void update() {
		if(ticks == 0) playSound = true;
		else playSound = false;
		if(type == HEARTATTACK) {
			nwidth += 25;
			nheight += 25;
			alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
			alpha -= 0.05;
			if(alpha < 0) alpha = 0;
			ticks++;
			ticks %= 40;
			if(ticks == 0) {
				nwidth = width;
				nheight = height;
				alpha = 1.0f;
			}
		}
		if(type == WARNING) {
			ticks++;
			ticks %= 40;
		}
	}
	
	public void draw(java.awt.Graphics2D g) {
		if(type == HEARTATTACK) {
			g.setComposite(alphaComposite);
			g.drawImage(
					image,
					GamePanel.WIDTH / 2 - nwidth / 2,
					GamePanel.HEIGHT / 2 - nheight / 2,
					nwidth,
					nheight,
					null
			);
			alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
			g.setComposite(alphaComposite);
		}
		if(type == WARNING) {
			if(ticks < 20) {
				g.drawImage(
						image,
						GamePanel.WIDTH / 2 - width / 2,
						GamePanel.HEIGHT / 2 - height / 2,
						width,
						height,
						null
				);
			}
		}
		
	}
	
	public boolean shouldPlaySound() { return playSound; }
	
}
