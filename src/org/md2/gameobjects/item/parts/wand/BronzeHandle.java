package org.md2.gameobjects.item.parts.wand;

import org.md2.rendering.Texture;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.WeaponItem;
import org.md2.gameobjects.item.parts.WeaponPart;

public class BronzeHandle implements WeaponPart {

    @Override
    public Texture getTexture() {
        return Texture.BRONZEHANDLE;
    }

    @Override
    public Texture getEffectTexture(){return null;}

    @Override
    public void onUse(LivingEntity user, WeaponItem weapon) {

    }
}
