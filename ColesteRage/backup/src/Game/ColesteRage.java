/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

/**
 *
 * @author Ivan
 */


/*
 * 
 * 	TO-DO-LIST
 * 	Mom changes rail at a lower speed
 *  Player's animation delay varies in each level
 *  More animation stuff for backgrounds
 *  Music for each level
 *  Music for each story 
 *  Bug when done playing (rail differs)
 *  Logo
 */


import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class ColesteRage {
	
	public static JFrame window;
	public static GamePanel gp;
	public static GraphicsDevice gd;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        /*
    	window = new JFrame("ColesteRage");
        gp = new GamePanel();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(gp);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);
        */
    	gp = new GamePanel();
    	changeWindowSize(GamePanel.SCALE);
    }
    
    public static void changeWindowSize(double scale) {
    	//window = null;
    	if(window != null) window.dispose();
    	window = new JFrame("ColesteRage");
    	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	if(scale == GamePanel.FULLSCREEN) {
    		window.setUndecorated(true);
    		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            gd = ge.getDefaultScreenDevice();
            
            if(!gd.isFullScreenSupported()){
                System.out.println("No se puede usar el modo full-screen");
                System.exit(0);
            }
            
            gd.setFullScreenWindow(window);
            GamePanel.WINDOW_WIDTH = window.getBounds().width;
            GamePanel.WINDOW_HEIGHT = window.getBounds().height;
            gp.setPreferredSize(new Dimension(window.getBounds().width, window.getBounds().height));
    	}
    	else {
    		window.setUndecorated(false);
    		gp.setWindowSize(scale);
    	}
    	gp.setPreferredSize(new Dimension(GamePanel.WINDOW_WIDTH, GamePanel.WINDOW_HEIGHT));
    	gp.setFocusable(true);
    	gp.requestFocus();
    	window.setContentPane(gp);
    	window.pack();
    	window.setLocationRelativeTo(null);
    	window.setResizable(false);
    	window.setVisible(true);
    	gp.requestFocusInWindow();
    }
    
}
