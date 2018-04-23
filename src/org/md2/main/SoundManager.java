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
        InputStream in = SoundManager.class.getResourceAsStream("/" + sound + ".wav");

        try
        {
            audioIn = AudioSystem.getAudioInputStream(in);
            clip = AudioSystem.getClip();
        }
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
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

