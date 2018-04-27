package org.md2.gameobjects.item;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.Texture;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.Wand;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.parts.CompositeItem;
import org.md2.gameobjects.item.parts.WeaponPart;
import org.md2.main.Game;
import org.md2.main.GraphicRendererV2;

public abstract class WandItem extends CompositeItem {

    protected float wandLength;
    protected float castingSpeed;

    public WandItem(float length, float castingSpeed, WeaponPart wandcap, WeaponPart wandhandle, WeaponPart wandorb)
    {
        super(wandcap, wandorb, wandhandle);
        this.wandLength = length;
        this.castingSpeed = castingSpeed;
    }

    @Override
    public boolean onUse(LivingEntity user)
    {
        if(currentlyInUse)
            return false;
        Vec2 mousePos = GraphicRendererV2.getMousePos();
        mousePos.normalize();
        Vec2 entityPos = user.getPosition().add(mousePos.mul(0.5F+0.5F*wandLength));
        WorldObject wo = new Wand(user, this);
        Game.getGame().getMechanicManager().getWorldManager().spawnObjectAt(wo, entityPos);
        setCurrentlyInUse(true);
        return super.onUse(user);
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



    public float getWandLength(){return wandLength;}
    public float getCastingSpeed(){return castingSpeed;}
}
