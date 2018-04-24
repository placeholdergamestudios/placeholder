package org.md2.common;

public enum Sound {
    AUA("aua"), WALK("walk");

    private final String soundName;

    private Sound(String soundName){this.soundName = soundName;};

    public String getSoundName(){return this.soundName;};
}


