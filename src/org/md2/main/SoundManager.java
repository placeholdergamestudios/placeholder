package org.md2.main;

import org.md2.common.Sound;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;
import javax.sound.sampled.*;
public class SoundManager {
    private AudioInputStream audioIn;

    public boolean playSound(Sound sound)
    {
        return playClip(loadSound(sound));
    }

    private Clip loadSound(Sound sound)
    {
        Clip clip = null;
        URL url = null;
        url = this.getClass().getClassLoader().getResource("/" + sound + ".wav");

        try
        {
            AudioInputStream audIn = AudioSystem.getAudioInputStream(url);
        }
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try
        {
            clip = AudioSystem.getClip();
        }
        catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        return clip;
    }

    private boolean playClip(Clip c)
    {
        if(c != null)
        {
            try {
                c.open(audioIn);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            c.start();
            return true;
        }
        else
        {
            return false;
        }
    }

}

