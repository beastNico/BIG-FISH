package main;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[10];
    FloatControl fc;
    
    public Sound() {
        soundURL[0] = getClass().getResource("/sound/background.wav");
        soundURL[1] = getClass().getResource("/sound/Eat.wav");
        soundURL[2] = getClass().getResource("/sound/dead.wav");
        soundURL[3] = getClass().getResource("/sound/levelup.wav");
        soundURL[4] = getClass().getResource("/sound/congrats.wav");
    }
    public void setFile(int i) {
        try{
         
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            
        }catch(IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            
        }
    }
    public void play(){
        
        clip.start();
        fc.setValue(-20f);
    }
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        fc.setValue(-30f);
    }
    public void stop() {
        clip.stop();
    }
    public void decreaseVolume() {
        fc.setValue(-12f);
        
    }
    public void increaseVolume() {
        fc.setValue(6f);
        
    }
    
}
