package org.md2.gameobjects.item.parts;

import org.md2.common.Texture;
import org.md2.gameobjects.item.Item;

public abstract class CompositeItem extends Item {

    protected WeaponPart part1;
    protected WeaponPart part2;
    protected WeaponPart part3;

    public CompositeItem(WeaponPart part1, WeaponPart part2, WeaponPart part3)
    {
        super(new Texture[]{part1.getTexture(), part2.getTexture(), part3.getTexture()}, 1, true);
        this.part1 = part1;
        this.part2 = part2;
        this.part3 = part3;
    }

    public WeaponPart getPart1(){return part1;}
    public WeaponPart getPart2(){return part2;}
    public WeaponPart getPart3(){return part3;}
}
