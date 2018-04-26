package org.md2.gameobjects.item.parts;

import org.md2.common.Texture;
import org.md2.gameobjects.item.Item;

public abstract class Weaponpart extends Item {


    public Weaponpart(Texture[] texture)
    {
        super(texture, 1, true);
    }

    public abstract void onUse();
}
