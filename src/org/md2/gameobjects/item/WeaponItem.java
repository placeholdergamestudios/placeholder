package org.md2.gameobjects.item;

import org.md2.rendering.Texture;

public abstract class WeaponItem extends Item
{
    private float weaponSize;
    private float weaponSpeed;

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
