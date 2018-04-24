package org.md2.main;

import org.jbox2d.dynamics.World;
import org.md2.common.Sound;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.Entity;
import org.md2.gameobjects.entity.living.Player;

import java.io.*;
import javax.sound.sampled.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

public class SoundManager {
    private AudioInputStream audioIn;
    private HashMap<Sound, Clip> clips;
    private HashSet<Soundentry> toplay;
    public static final int SOUNDWALK = 1;
    public static final int SOUNDBOWEQUIP = 2;
    public static final int SOUNDBOWTENSION = 3;
    public static final int SOUNDBOWRELEASE = 4;
    public static final int SOUNDAUA = 5;



    public SoundManager()
    {
        createSounds();
    }

    public void tick(ArrayList<WorldObject> worldObjects)
    {
        addEntitysounds(worldObjects);
        for(Soundentry s: toplay)
        {
            executeClip(s.getClip(),s.getVolume());
        }
        toplay.clear();
    }

    public void playSoundID(int i)
    {
        switch(i)
        {
            case SOUNDWALK:{playSound(Sound.WALK, 0.05f);break;}
            case SOUNDBOWEQUIP:{playSound(Sound.BOWEQUIP, 0.3f);break;}
            case SOUNDBOWTENSION:{playSound(Sound.BOWTENSION, 0.3f);break;}
            case SOUNDBOWRELEASE:{playSound(Sound.BOWRELEASE, 0.3f);break;}
            case SOUNDAUA:{playSound(Sound.AUA, 0.5f);break;}
        }
    }

    private boolean playSound(Sound sound) {return playClip(clips.get(sound));}

    private boolean playSound(Sound sound, float volume) {return playClip(clips.get(sound), volume); }

    private boolean isPlaying(Clip c)
    {
        return c.isRunning();
    }

    private void createSounds()
    {
        clips = new HashMap<Sound, Clip>();
        for(Sound s: Sound.values())
        {
            loadSound(s);
        }
        toplay = new HashSet<Soundentry>();
    }

    private boolean playClip(Clip c)
    {
        return playClip(c, 1.0f);
    }

    private boolean playClip(Clip c, Float volume)
    {
        toplay.add(new Soundentry(c, volume));
        return true;
    }

    private void addEntitysounds(ArrayList<WorldObject> worldObjects)
    {
        for(WorldObject wo: worldObjects)
            if (wo instanceof Entity) {
                if (((Entity) wo).isMoving())
                    if (((Entity) wo).getwalkingSound() != null)
                        playSoundID(SOUNDWALK);
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

    private boolean executeClip(Clip c)
    {
        return executeClip(c,1.0f);
    }

    private boolean executeClip(Clip c, float volume)
    {
        if(c != null)
        {
            if(!isPlaying(c))
            {
                c.setFramePosition(0);
                setVolume(c,volume);
                c.start();
            }

            return true;
        }
        else
            return false;
    }

    private void setVolume(Clip c, float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    private class Soundentry{
        private Clip clip;
        private float volume;

        public Soundentry(Clip clip, float volume)
        {
            this.clip = clip;
            this.volume = volume;
        }

        public Clip getClip()
        {
            return clip;
        }

        public float getVolume()
        {
            return volume;
        }

        public void setVolume(float volume)
        {
            this.volume = volume;
        }
    }

}

