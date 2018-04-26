package org.md2.gameobjects.item.parts.wand;

import org.md2.common.Texture;

public class BronzeHandle extends WandHandle{

    public BronzeHandle()
    {
        super(new Texture[]{Texture.BRONZEHANDLE});
        initVars();
    }

    protected void initVars()
    {
        varOnAttack = 2;
        varOnUse = 0;
        varOnThrow = 0;
    }
}
