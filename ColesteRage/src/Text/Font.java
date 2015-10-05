/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Text;

import java.awt.image.BufferedImage;

/**
 *
 * @author Ivan
 */
public class Font {
    
    private Letter[] letters;
    private String fontName;
    private int counter;
    
    public Font() {}
    public Font(String fontName) {
        this.fontName = fontName;
        letters = new Letter[256];
    }
    
    public void addLetter(char ch, BufferedImage image) {
        for(int i = 0; i < 256; i++) {
            if(letters[i] == null) {
                letters[i] = new Letter(ch, image);
                break;
            }
        }
    }
    
    public void increaseCounter() { counter++; }
    public int getCounter() { return counter; }
    public Letter getLetter(char ch) { return letters[ch]; }
}
