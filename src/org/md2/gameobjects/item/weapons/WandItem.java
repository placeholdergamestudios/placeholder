package org.md2.gameobjects.item.weapons;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.Wand;
import org.md2.gameobjects.entity.living.LivingEntity;

import org.md2.gameobjects.item.CompositeWeapon;
import org.md2.gameobjects.item.parts.WeaponPart;
import org.md2.main.Game;
import org.md2.main.GraphicRendererV2;

public abstract class WandItem extends CompositeWeapon {


    public WandItem(float length, float castingSpeed, WeaponPart wandcap, WeaponPart wandhandle, WeaponPart wandorb)
    {
        super(wandcap, wandorb, wandhandle, length, castingSpeed);

    }

    @Override
    public boolean onPrimaryUse(LivingEntity user)
    {
        if(currentlyInUse)
            return false;
        Vec2 mousePos = GraphicRendererV2.getMousePos();
        mousePos.normalize();
        Vec2 entityPos = user.getPosition().add(mousePos.mul(0.5F+0.5F*this.getWeaponSize()));
        WorldObject wo = new Wand(user, this);
        Game.getGame().getMechanicManager().getWorldManager().spawnObjectAt(wo, entityPos);
        setCurrentlyInUse(true);
        return true;
    }

    @Override
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
