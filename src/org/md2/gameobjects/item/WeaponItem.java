package org.md2.gameobjects.item;

import org.md2.common.Texture;

public abstract class WeaponItem extends Item
{
    protected float weaponSize;
    protected float weaponSpeed;

    public WeaponItem(Texture[] textures, float weaponSize, float weaponSpeed)
    {
        super(textures, 1, true);
        this.weaponSize = weaponSize;
        this.weaponSpeed = weaponSpeed;
    }

    public float getWeaponSize()
    {
        return weaponSize;
    }

    public float getWeaponSpeed()
    {
        return weaponSpeed;
    }
}
