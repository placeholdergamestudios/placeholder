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

    private boolean isPlaying(Clip c)
    {
        return c.getFramePosition() < c.getFrameLength();
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
        InputStream bufferedIn = new BufferedInputStream(in);
        try
        {
            audioIn = AudioSystem.getAudioInputStream(bufferedIn);
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
        return playClip(c,1.0f);
    }

    private boolean playClip(Clip c, float volume)
    {
        c.setFramePosition(0);
        if(c != null)
        {
            if(isPlaying(c))
            {
                setVolume(c,volume);
                c.start();
            }

            return true;
        }
        else
            return false;
    }

    public void setVolume(Clip c, float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

}

