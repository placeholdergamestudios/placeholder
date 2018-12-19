package org.md2.main;

import org.md2.common.AI;
import org.md2.common.SimpleEnemyAI;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.worldmanagement.WorldManager;

import java.util.ArrayList;
public class AIManager {
    public static final int AISIMPLEENEMY = 1;
    private ArrayList <AI> ailist;
    private WorldManager wm;

    public AIManager(WorldManager wm)
    {
        this.wm = wm;
        ailist = new ArrayList<AI>();
        addAI(new SimpleEnemyAI(wm));
    }

    public void tick()
    {
        for(AI ai : ailist)
        {
            ai.onTick();
        }
    }

    public void addEntity(LivingEntity le, int aiidentifyer)
    {
        getAI(aiidentifyer).giveControl(le);
    }

    public void removeEntity(LivingEntity le, int aiidentifyer)
    {
        getAI(aiidentifyer).withdrawControl(le);
    }

    private void addAI(AI ai)
    {
        ailist.add(ai);
    }

    private void removeAI(AI ai)
    {
        ailist.remove(ai);
    }

    public AI getAI(int aiidentifyer)
    {
        switch (aiidentifyer)
        {
            case AISIMPLEENEMY:
            {
                for(AI ai : ailist)
                {
                    if(ai instanceof SimpleEnemyAI)
                    {
                        return ai;
                    }
                }
                break;
            }

        }
        return null;
    }

}
