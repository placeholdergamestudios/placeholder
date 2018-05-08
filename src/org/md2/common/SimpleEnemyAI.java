package org.md2.common;

import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.entity.living.SimpleEnemy;
import org.md2.gameobjects.item.Item;
import org.md2.gameobjects.item.weapons.Shortsword;
import org.md2.worldmanagement.WorldManager;

public class SimpleEnemyAI extends AI {

    public SimpleEnemyAI(WorldManager wm)
    {
        super(wm);
    }

    @Override
    public void giveControl(LivingEntity le)
    {
        controlled.add(le);
        frozen.put(le, false);
    }

    protected void onTick(LivingEntity le)
    {
        if(!le.willBeRemoved())
        {
            if(Math.random()*1000000 >= 999999)
            {
                le.setShouldMove((int)(Math.random()*1));
                le.setShouldRotate((int)(Math.random()*2));
                le.useItem(new Shortsword(), Item.USAGE_TYPE_1);
            }
        }
    }

    @Override
    public void onTick()
    {
        addEntities();
        for(LivingEntity e : controlled)
        {
            if(!isfrozen(e))
            {
                onTick(e);
            }
        }
    }

    private void addEntities()
    {
        System.out.println("Test2");
        for(WorldObject wo : wm.getAllWorldObjects())
        {
            if(wo instanceof SimpleEnemy)
            {
                giveControl((SimpleEnemy)wo);
            }
        }
    }
}
