package org.md2.gameobjects.item;

import org.md2.common.Texture;
import org.md2.gameobjects.item.Item;
import org.md2.gameobjects.item.parts.WeaponPart;

public abstract class CompositeWeapon extends WeaponItem {

    protected WeaponPart part1;
    protected WeaponPart part2;
    protected WeaponPart part3;

    public CompositeWeapon(WeaponPart part1, WeaponPart part2, WeaponPart part3, float weaponSize, float weaponSpeed)
    {
        super(new Texture[]{part1.getTexture(), part2.getTexture(), part3.getTexture()}, weaponSize, weaponSpeed);
        this.part1 = part1;
        this.part2 = part2;
        this.part3 = part3;
    }

    public WeaponPart getPart1(){return part1;}
    public WeaponPart getPart2(){return part2;}
    public WeaponPart getPart3(){return part3;}
}
