package org.md2.gameobjects.item.parts.wand;

import org.md2.common.Texture;

public class BronzeCap extends WandCap{

    public BronzeCap()
    {
        super(new Texture[]{Texture.BRONZECAP});
    }

    protected void initVars()
    {
        varOnAttack = 0;
        varOnUse = 0;
        varOnThrow = 2;
    }
}
