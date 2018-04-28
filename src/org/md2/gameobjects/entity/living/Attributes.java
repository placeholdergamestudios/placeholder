package org.md2.gameobjects.entity.living;

public class Attributes {

    public static final int str = 1;
    public static final int dex = 2;
    public static final int vit = 3;
    public static final int itl = 4;
    private int strength;
    private int dexterity;
    private int vitality;
    private int intelligence;
    private int level;
    private int points;

    public Attributes(int strength, int dexterity, int vitality, int intelligence)
    {
        this.strength = strength;
        this.dexterity = dexterity;
        this.vitality = vitality;
        this.intelligence = intelligence;
        level = 0;
        points = 0;
    }

    public void levelUp()
    {
        level++;
        points = points + (level);
    }

    public void spendPoint(int attributeid)
    {
        switch (attributeid)
        {
            case str:{strength++;break;}
            case dex:{dexterity++;break;}
            case vit:{vitality++;break;}
            case itl:{intelligence++;break;}
        }
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getStrength() {
        return strength;
    }

    public int getVitality() {
        return vitality;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getLevel() {
        return level;
    }

    public int getPoints() {
        return points;
    }
}
