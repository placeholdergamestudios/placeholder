package org.md2.gameobjects.item;

import org.md2.common.Texture;
import org.md2.gameobjects.item.WandItem;
import org.md2.gameobjects.item.parts.wand.BronzeCap;
import org.md2.gameobjects.item.parts.wand.BronzeHandle;
import org.md2.gameobjects.item.parts.wand.CrystalOrb;

public class TestWand extends WandItem {

    public TestWand()
    {
        super(new Texture[]{new BronzeCap().getTexture(), new CrystalOrb().getTexture(), new BronzeHandle().getTexture()}, 1, 2f, new BronzeCap(), new BronzeHandle(), new CrystalOrb());
        setVars();
    }

    protected void setVars()
    {
        varOnThrow = getPart1().getVarOnThrow();
        varOnAttack = getPart2().getVarOnAttack();
        varOnUse = getPart3().getVarOnUse();
    }
}
