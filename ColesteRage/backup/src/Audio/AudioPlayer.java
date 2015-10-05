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
    private AudioLoader audioLoader;
    private static ArrayList<String> playingAudio;
    private static boolean muted = false;
    
    public AudioPlayer() {}
    public AudioPlayer(AudioLoader al) {
        audioLoader = al;
        if(playingAudio == null) playingAudio = new ArrayList<>();
    }
    
    public static void setMuted(boolean b) { muted = b; }
    
    public void play(String file) {
    	if(muted) return;
        if(audioLoader.contains(file)) {
            Clip clip = audioLoader.getAudio(file);
            if(clip == null) return;
            stop(file);
            clip.setFramePosition(0);
            clip.start();
            playingAudio.add(file);
        }
        else {
            System.out.println("Archivo de audio no encontrado para reproducirse");
        }
    }
    
    public void playAndLoop(String file) {
    	if(muted) return;
        if(audioLoader.contains(file)) {
            Clip clip = audioLoader.getAudio(file);
            if(clip == null) return;
            stop(file);
            clip.setFramePosition(0);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            playingAudio.add(file);
        }
    }
    
    public void stop(String file) {
        if(audioLoader.contains(file)) {
            Clip clip = audioLoader.getAudio(file);
            if(clip.isRunning()) {
            	clip.stop();
            	playingAudio.remove(file);
            	System.out.println("Audio stopped");
            }
        }
        else {
            System.out.println("Archivo de audio no encontrado para detenerse");
        }
    }
    
    public void stopAll() {
    	for(int i = 0; i < playingAudio.size(); i++) {
    		Clip clip = audioLoader.getAudio(playingAudio.get(i));
    		if(clip.isRunning()) clip.stop();
    	}
    	playingAudio.clear();
    }
    /*
    public boolean isPlaying(String file) {
        if(audioLoader.contains(file)) {
            Clip clip = audioLoader.getAudio(file);
            if(clip.isRunning()) 
            	return true;
        }
        else 
            System.out.println("No existe el archivo de audio");
        return false;
    }*/
    
    public static boolean isPlaying(String name) {
    	for(int i = 0; i < playingAudio.size(); i++) {
    		if(playingAudio.get(i).equals(name)) 
    			return true;
    	}
    	return false;
    }
    
    public void setVolume(String file, float volume) {
        if(audioLoader.contains(file)) {
            FloatControl gainControl = (FloatControl)audioLoader.getAudio(file).getControl(
                    FloatControl.Type.MASTER_GAIN
            );
            gainControl.setValue(volume);
        }
    }
}
