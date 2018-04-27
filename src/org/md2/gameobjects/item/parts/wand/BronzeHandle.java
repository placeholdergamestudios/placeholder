package org.md2.gameobjects.item.parts.wand;

import org.md2.common.Texture;
import org.md2.gameobjects.item.parts.WeaponPart;

public class BronzeHandle implements WeaponPart {

    @Override
    public Texture getTexture() {
        return Texture.BRONZEHANDLE;
    }

    @Override
    public void onUse() {

    }
}
