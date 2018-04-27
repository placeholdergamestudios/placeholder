package org.md2.gameobjects.item.weapons;

import org.md2.gameobjects.item.parts.wand.BronzeCap;
import org.md2.gameobjects.item.parts.wand.BronzeHandle;
import org.md2.gameobjects.item.parts.wand.CrystalOrb;

public class TestWand extends WandItem {

    public TestWand()
    {
        super(1, 2f, new BronzeCap(), new BronzeHandle(), new CrystalOrb());
    }
}
