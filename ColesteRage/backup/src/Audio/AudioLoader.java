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
    private HashMap<String, Clip> audio;
    private final String FILEDIR = "/";
    private final String AUDIODIR = "/Audio/";
    
    public AudioLoader(String fnm) {
        audio = new HashMap<>();
        loadAudioFiles(fnm);
       
    }
    
    public Set<String> getKeySet() {
    	return audio.keySet();
    }
    
    private void loadAudioFiles(String fnm) {
        try {
            InputStream in = getClass().getResourceAsStream("/AudioText/" + fnm);
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
    
    private void readAudioFile(String line) {
        String key = prefix(line);
        System.out.println(line);
        System.out.println(key);
        if(audio.containsKey(key)) {
            System.out.println("Ya existe un archivo de audio con el mismo nombre");
        }
        else {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(
                        getClass().getResource("/Audio/" + line)
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
    
    private String prefix(String line) {
       if(line.contains("/"))
           return line.substring(line.indexOf("/") + 1, line.indexOf("."));
       return line.substring(0, line.indexOf("."));
    }
    
    public Clip getAudio(String name) { return audio.get(name); }
    
    public boolean contains(String name) {
        if(audio.containsKey(name)) return true;
        return false;
    }
}
