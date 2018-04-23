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
            Clip clip = null;
            InputStream in = SoundManager.class.getResourceAsStream("/" + s + ".wav");
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
            clips.put(s, clip);
        }
    }

    private void loadSound(Sound s)
    {
        Clip clip = null;
        InputStream in = SoundManager.class.getResourceAsStream("/" + s + ".wav");
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
        clips.put(s, clip);
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

