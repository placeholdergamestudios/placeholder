package org.md2.common;

import org.md2.gameobjects.entity.living.LivingEntity;

public class Damage {
    public static final int DAMAGESLASH = 1;
    public static final int DAMAGETHRUST = 2;
    public static final int DAMAGELIGHT = 3;
    public static final int DAMAGEDARKNESS = 4;

    protected int damagetype;
    protected int damageamount;
    protected LivingEntity source;
    protected LivingEntity target;

    public Damage(int damagetype, int damageamount, LivingEntity source, LivingEntity target)
    {
        this.damagetype = damagetype;
        this.damageamount = damageamount;
        this.source = source;
        this.target = target;
        dealDamage();
    }

    private void dealDamage()
    {
        target.damage(calculateactualdamage());
    }

    private int calculateactualdamage()
    {
        int stat = 0;
        switch(damagetype)
        {
            case DAMAGESLASH:{stat = source.getAttributes().getStrength();break;}
            case DAMAGETHRUST:{stat = source.getAttributes().getDexterity();break;}
            case DAMAGELIGHT:{stat = source.getAttributes().getIntelligence();break;}
            case DAMAGEDARKNESS:{stat = source.getAttributes().getIntelligence();break;}
        }
        return damageamount+stat*6;
    }
}
