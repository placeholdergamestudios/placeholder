package org.md2.gameobjects.item.parts.wand;

import org.md2.common.Texture;

public class CrystalOrb extends WandOrb {

    public CrystalOrb()
    {
        super(new Texture[]{Texture.CRYSTALORB});
        initVars();
    }

    protected void initVars()
    {
        varOnAttack = 0;
        varOnUse = 5;
        varOnThrow = 0;
    }
}
