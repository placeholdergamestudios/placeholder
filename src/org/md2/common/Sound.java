package org.md2.common;

public enum Sound {
    AUA("aua"), WALK("walk"), BOWEQUIP("bowequip"), BOWTENSION("bowtension"), BOWRELEASE("bowrelease"),
    SWORDSLASH("swordslash"), MUSIC("musicmystic"), FIREBALL("fireball"), BOOMERANG("boomerang"), PICKUP("pickup");

    private final String soundName;

    private Sound(String soundName){this.soundName = soundName;};

    public String getSoundName(){return this.soundName;};
}


