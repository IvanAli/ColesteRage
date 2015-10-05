/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;
import javax.sound.sampled.*;
import java.util.ArrayList;
/**
 *
 * @author Ivan
 */
public class AudioPlayer {
    private static ArrayList<String> playingAudio;
    private static boolean muted = false;
    
    public static void init(String fnm) {
    	AudioLoader.loadFromFile(fnm);
    }
    
    public static void setMuted(boolean b) { muted = b; }
    
    public static void play(String file) {
    	if(muted) return;
        if(AudioLoader.contains(file)) {
            Clip clip = AudioLoader.getAudio(file);
            if(clip == null) return;
            stop(file);
            clip.setFramePosition(0);
            clip.start();
            if(playingAudio == null) playingAudio = new ArrayList<>();
            playingAudio.add(file);
        }
        else {
            System.out.println("Archivo de audio no encontrado para reproducirse");
        }
    }
    
    public static void playAndLoop(String file) {
    	if(muted) return;
        if(AudioLoader.contains(file)) {
            Clip clip = AudioLoader.getAudio(file);
            if(clip == null) return;
            stop(file);
            clip.setFramePosition(0);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            if(playingAudio == null) playingAudio = new ArrayList<>();
            playingAudio.add(file);
        }
    }
    
    public static void stop(String file) {
        if(AudioLoader.contains(file)) {
            Clip clip = AudioLoader.getAudio(file);
            if(clip.isRunning()) {
            	clip.stop();
            	if(playingAudio == null) return;
            	playingAudio.remove(file);
            }
        }
        else {
            System.out.println("Archivo de audio no encontrado para detenerse");
        }
    }
    
    public static void stopAll() {
    	if(playingAudio == null) return;
    	for(int i = 0; i < playingAudio.size(); i++) {
    		//Clip clip = AudioLoader.getAudio(playingAudio.get(i));
    		stop(playingAudio.get(i));
    	}
    	playingAudio.clear();
    }
    
    public static void clearAudio() {
    	AudioLoader.clearLoader();
    }
    /*
    public boolean isPlaying(String file) {
        if(AudioLoader.contains(file)) {
            Clip clip = AudioLoader.getAudio(file);
            if(clip.isRunning()) 
            	return true;
        }
        else 
            System.out.println("No existe el archivo de audio");
        return false;
    }*/
    
    public static boolean isPlaying(String name) {
    	if(playingAudio == null) return false;
    	for(int i = 0; i < playingAudio.size(); i++) {
    		if(playingAudio.get(i).equals(name)) 
    			return true;
    	}
    	return false;
    }
    
    public static void setVolume(String file, float volume) {
        if(AudioLoader.contains(file)) {
            FloatControl gainControl = (FloatControl)AudioLoader.getAudio(file).getControl(
                    FloatControl.Type.MASTER_GAIN
            );
            gainControl.setValue(volume);
        }
    }
}
