/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Text;

import Game.GamePanel;
import Images.ImagesLoader;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

/**
 *
 * @author Ivan
 */
public class Text {
    private static HashMap<String, Font> fonts;
    private static boolean typing;
    public static final int CENTERED = -1;
    
    private static boolean isTransparent(int x, int y, BufferedImage img) {
        int pixel = img.getRGB(x,y);
        if((pixel>>24) == 0x00) {
            return true;
        }
        return false;
    }
    
    private static BufferedImage adjustedLetter(BufferedImage im) {
        int x0 = 0;
        int x1 = 12;
        Graphics g;
        BufferedImage copia;
        
        boolean x0Encontrado = false;
        boolean x1Encontrado = false;
        boolean columnaVacia = true;
        
        for(int i=0; i<im.getWidth(); i++){
            columnaVacia = true;
            for(int j=0; j<im.getHeight(); j++){
                if(!isTransparent(i, j, im)){
                    columnaVacia = false;
                    if(!x0Encontrado){
                        x0 = i;
                        x0Encontrado = true;
                    }
                }
                if(columnaVacia && (j == im.getHeight() - 1) && x0Encontrado){
                   if(!x1Encontrado){
                        x1 = i - 1;
                        x1Encontrado = true;
                    } 
                }
            }
        }
        //System.out.println("x0: " + x0 + ". x1: " + x1);
        
        int nWidth = x1 - x0;
        //height = im.getHeight();
        //System.out.println("new width: " + nWidth + ". im height:" + im.getHeight());
        try{
            copia = new BufferedImage(nWidth, im.getHeight(), im.getColorModel().getTransparency());
            g = copia.getGraphics();
            g.drawImage(im, 0, 0, nWidth, im.getHeight(), x0, 0, x1, im.getHeight(), null);
            return copia;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        return null;
    }
    
    public static boolean containsFont(String font) {
        if(fonts.containsKey(font)) return true;
        return false;
    }
    /*
    public void drawString(Graphics2D g, String texto, String font, int x, int y){
        for(int i=0; i<texto.length(); i++) {
            g.drawImage((BufferedImage)letras.get(texto.charAt(i)), x, y, null);
            x += letras.get(texto.charAt(i)).getWidth(); //Para dejar un espacio entre letras
        }
    }
    */
    /*
    public static void drawString(Graphics2D g, String text, int x, int y, String font) {
        for(int i = 0; i < text.length(); i++) {
            g.drawImage(fontsImg.get(font).get(text.charAt(i)), 
                    x, 
                    y, 
                    null);
            x += fontsImg.get(font).get(text.charAt(i)).getWidth();
        }
    }
    */
    public static void drawString(Graphics2D g, String text, int x, int y, String font, TextAnimation txtAnimation) {
    	Letter letter;
    	if(x == CENTERED) {
    		for(int i = 0; i < text.length(); i++) {
    			letter = fonts.get(font).getLetter(text.charAt(i)); 
    			x += letter.getWidth();
    		}
    		x++;
    		x = GamePanel.WIDTH / 2 - x / 2;
    	}
        String[] tokens = text.split(" ");
        int initX = x;
        if(txtAnimation == null) {
            for(int i = 0; i < text.length(); i++) {
                letter = fonts.get(font).getLetter(text.charAt(i));
                g.drawImage(letter.getImage(), 
                        x, 
                        y, 
                        null);
                x += letter.getWidth();
            }
        }
        else {
            txtAnimation.setLength(text.length());
            for(int i = 0; i < txtAnimation.getCounter(); i++) {
                letter = fonts.get(font).getLetter(text.charAt(i));
                g.drawImage(letter.getImage(), 
                        x, 
                        y, 
                        null);
                x += letter.getWidth();
                
                /*
                if(text.charAt(i) == 32)
                	System.out.println("Length from " + i + " to " + text.indexOf(' ', i + 1) + ": " + text.substring(i, text.indexOf(' ', i + 1)).length());
                */
                
                if((x > 1120 && text.charAt(i) == 32)/* || (x > 950 && 
                		text.charAt(i) == 32 && text.substring(i, text.indexOf(' ', i + 1)).length() > 6)*/) {
                	x = initX;
                	y += fonts.get(font).getLetter(text.charAt(i)).getHeight();
                }
                if(text.length() > txtAnimation.getCounter()) typing = true;
                else typing = false;
            }
        }
        
    }
    
    public static boolean isTyping() { return typing; }
    
    public static void drawStringLetters(Graphics2D g, String text, int x, int y, String font, int counter) {
        Letter letter;
        for(int i = 0; i < fonts.get(font).getCounter(); i++) {
            letter = fonts.get(font).getLetter(text.charAt(i));
            g.drawImage(letter.getImage(), 
                    x, 
                    y, 
                    null);
            x += letter.getWidth();
            
        }
        
        System.out.println("Counter: " + fonts.get(font).getCounter());
            if(text.length() > fonts.get(font).getCounter())
                fonts.get(font).increaseCounter();
                
    }
    
    public static Font getFont(String font) { return fonts.get(font); }
    public static void clearFont(String font) { fonts.remove(font); }
    
    // font loading
    
    public static void loadFont(String file) {
        String key = prefix(file);
        BufferedImage[][] ch = loadFontMatrix(file);
        if(ch == null)
            System.out.println("Tira nula");
        else {
            if(fonts == null) fonts = new HashMap<>();
            if(fonts.containsKey(file)) return;
            fonts.put(key, new Font(key));
            for(int i = 0; i < 16; i++){
                for(int j = 0; j < 16; j++) {
                    fonts.get(key).addLetter((char)(16 * i + j), adjustedLetter(ch[i][j]));
                }
            }
        }
    }
    
    private static String prefix(String file) {
        int pos;
        if((pos = file.lastIndexOf(".")) == -1) {
            System.out.println("No se encontro el prefijo del archivo");
            return file;
        }
        else
            return file.substring(0, pos);
    }
    
    private static BufferedImage[][] loadFontMatrix(String archivo){
        BufferedImage tiraCompleta;
        BufferedImage[][] imagenesTira = new BufferedImage[16][16];
        Graphics gCopiado;
        
        int nWidth;
        int nHeight;
        
        try {
            tiraCompleta = ImageIO.read(
            		Text.class.getResourceAsStream("/Fonts/" + archivo)
    		);
            nWidth = tiraCompleta.getWidth()/16;
            nHeight = tiraCompleta.getHeight()/16;
            
            for(int i=0; i<16; i++){
                for(int j=0; j<16; j++){
                    imagenesTira[i][j] = new BufferedImage(nWidth, nHeight, tiraCompleta.getTransparency());
                    gCopiado = imagenesTira[i][j].getGraphics();
                    gCopiado.drawImage(tiraCompleta, 0, 0, nWidth, nHeight, j*nWidth, i*nHeight, (j*nWidth)+nWidth, (i*nHeight)+nHeight, null);
                    gCopiado.dispose();
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return imagenesTira;
    }    
}
