package org.md2.common;

import org.md2.gameobjects.entity.living.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AI {
    private ArrayList <LivingEntity> controlled;
    private HashMap<LivingEntity, Boolean> frozen;

    public AI()
    {
        controlled = new ArrayList<LivingEntity>();
        frozen = new HashMap<LivingEntity, Boolean>();
    }

    public void giveControl(LivingEntity le)
    {
        controlled.add(le);
        frozen.put(le, false);
    }

    public void withdrawControl(LivingEntity le)
    {
        controlled.remove(le);
        frozen.remove(le);
    }

    public boolean iscontrolledby(LivingEntity le)
    {
        return controlled.contains(le);
    }

    public void togglefreeze(LivingEntity le)
    {
        frozen.replace(le, !frozen.get(le));
    }

    public boolean isfrozen(LivingEntity le)
    {
        return frozen.get(le);
    }

    public void onTick()
    {
        for(LivingEntity e : controlled)
        {
            if(!isfrozen(e))
            {
                //  Processing
            }
        }
    }
}
