package org.md2.gameobjects.item.parts;

import org.md2.common.Texture;
import org.md2.gameobjects.item.Item;

public abstract class CompositeItem extends Item {

    protected Weaponpart part1;
    protected Weaponpart part2;
    protected Weaponpart part3;

    public CompositeItem(Texture [] texture, Weaponpart part1, Weaponpart part2, Weaponpart part3)
    {
        super(texture, 1, true);
        this.part1 = part1;
        this.part2 = part2;
        this.part3 = part3;
    }

    public Weaponpart getPart1(){return part1;}
    public Weaponpart getPart2(){return part2;}
    public Weaponpart getPart3(){return part3;}
}
