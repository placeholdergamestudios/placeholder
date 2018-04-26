package org.md2.gameobjects.item.parts.wand;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.Texture;
import org.md2.gameobjects.item.parts.Weaponpart;

public abstract class WandHandle extends Weaponpart {

    public WandHandle(Texture[] texture)
    {
        super(texture);
    }

    public void onUse()
    {

    }

    public FixtureDef getFixtureDef()
    {
        PolygonShape cs = new PolygonShape();
        cs.setAsBox(0.5f, 0.5f);

        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 0.0f;
        fd.friction = 0.0f;
        fd.restitution = 0.0f;
        fd.setSensor(true);

        return fd;
    }
}
