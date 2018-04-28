package org.md2.gameobjects.item.parts;

import org.md2.common.Texture;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.WeaponItem;

public interface WeaponPart{

    public Texture getTexture();

    public Texture getEffectTexture();

    public  void onUse(LivingEntity user, WeaponItem weapon);
}
