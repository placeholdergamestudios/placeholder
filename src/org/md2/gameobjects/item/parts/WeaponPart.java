package org.md2.gameobjects.item.parts;

import org.md2.common.Texture;
import org.md2.gameobjects.item.Item;

public interface WeaponPart{

    public Texture getTexture();

    public Texture getEffectTexture();

    public  void onUse();
}
