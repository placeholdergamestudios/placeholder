package org.md2.common;

import org.jbox2d.dynamics.World;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.worldmanagement.WorldManager;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AI {
    protected ArrayList <LivingEntity> controlled;
    protected HashMap<LivingEntity, Boolean> frozen;
    protected WorldManager wm;

    public AI(WorldManager wm)
    {
        controlled = new ArrayList<LivingEntity>();
        frozen = new HashMap<LivingEntity, Boolean>();
        this.wm = wm;
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
                onTick(e);
            }
        }
    }

    protected abstract void onTick(LivingEntity le);
}
