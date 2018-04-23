package org.md2.main;

import org.md2.common.Sound;

import java.io.*;
import javax.sound.sampled.*;
import java.util.HashMap;
public class SoundManager {
    private AudioInputStream audioIn;
    private HashMap<Sound, Clip> clips;

    public boolean playSound(Sound sound)
    {
        return playClip(clips.get(sound));
    }

    public SoundManager()
    {
        createSounds();
    }

    private void createSounds()
    {
        clips = new HashMap<Sound, Clip>();
        for(Sound s: Sound.values())
        {
            loadSound(s);
        }
    }

    private void loadSound(Sound s)
    {
        Clip clip = null;
        InputStream in = SoundManager.class.getResourceAsStream("/" + s.getSoundName() + ".wav");
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
        try {
            clip.open(audioIn);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clips.put(s, clip);
    }

    private boolean playClip(Clip c)
    {
        c.setFramePosition(0);
        if(c != null)
        {
            c.start();
            return true;
        }
        else
        {
            return false;
        }
    }

}

