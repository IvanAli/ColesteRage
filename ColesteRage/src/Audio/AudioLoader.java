/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;
import java.util.HashMap;
import java.util.Set;

import javax.sound.sampled.*;

import java.io.*;
/**
 *
 * @author Ivan
 */
public class AudioLoader {
    private static HashMap<String, Clip> audio;
    private final String FILEDIR = "/";
    private final String AUDIODIR = "/Audio/";
    
    public AudioLoader(String fnm) {
        audio = new HashMap<>();
        loadFromFile(fnm);
    }
    
    public Set<String> getKeySet() {
    	return audio.keySet();
    }
    
    public static void loadFromFile(String fnm) {
    	if(audio == null) audio = new HashMap<>();
        try {
            InputStream in = AudioLoader.class.getResourceAsStream("/AudioText/" + fnm);
            //FileInputStream fis = new FileInputStream(FILEDIR + fnm);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = br.readLine()) != null) {
                if(line.startsWith("//")) continue;
                if(line.length() == 0) continue;
                if(line.endsWith(".mp3") || line.endsWith(".wav")) readAudioFile(line);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void removeAudio(String fnm) {
        try {
            InputStream in = AudioLoader.class.getResourceAsStream("/AudioText/" + fnm);
            //FileInputStream fis = new FileInputStream(FILEDIR + fnm);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = br.readLine()) != null) {
                String key = prefix(line);
                if(audio.containsKey(key)) audio.remove(key);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void readAudioFile(String line) {
        String key = prefix(line);
        System.out.println(line);
        System.out.println(key);
        if(audio.containsKey(key)) {
            System.out.println("Ya existe un archivo de audio con el mismo nombre");
        }
        else {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(
                        AudioLoader.class.getResource("/Audio/" + line)
                );
                AudioFormat format = ais.getFormat();
                /*
                AudioFormat decoded = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        format.getSampleRate(),
                        format.getSampleSizeInBits() * 2,
                        format.getChannels(),
                        format.getFrameSize(),
                        format.getFrameRate(),
                        true
                
                );
                        */
                AudioFormat decoded = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        format.getSampleRate(),
                        16,
                        format.getChannels(),
                        format.getChannels() * 2,
                        format.getSampleRate(),
                        false
                );
               AudioInputStream dais = AudioSystem.getAudioInputStream(
                       decoded, ais
               );
               Clip clip = AudioSystem.getClip();
               clip.open(dais);
               audio.put(key, clip);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private static String prefix(String archivo) {
        int posA;
        int posB;
        if((posB = archivo.lastIndexOf(".")) == -1){
            System.out.println("No se encontro el prefix del archivo");
            return archivo;
        }
        else if(archivo.contains("/")) {
            posA = archivo.lastIndexOf("/");
            return archivo.substring(posA + 1, posB);
        }
        else
            return archivo.substring(0, posB);
    }
    
    public static Clip getAudio(String name) { return audio.get(name); }
    
    public static boolean contains(String name) {
    	if(audio == null) return false;
        if(audio.containsKey(name)) return true;
        return false;
    }
    
    public static void clearLoader() {
    	
    	audio.clear();
    	audio = null;
    	
    }
}
