package org.md2.gameobjects.entity;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.rendering.Texture;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.WeaponItem;

public abstract class Missile extends WeaponEntity
{
    protected WorldObject hitObject;

    public Missile(LivingEntity user, WeaponItem usedItem, Texture[] textures)
    {
        super(user, usedItem);
        this.setTextures(textures);
        hitObject = null;
    }

    public abstract void onCollision(WorldObject o);

    @Override
    public void performTick()
    {
        if(hitObject != null){
            this.removeFromWorld();
        }
    }

    @Override
    public void afterDeploySetup()
    {
        super.afterDeploySetup();
        this.body.setLinearVelocity(initialDirectionVec2.mul(15/usedItem.getWeaponSpeed()));
    }

    @Override
    public float getRenderAngle()
    {
        return super.getRenderAngle()+initialDirection;
    }

    @Override
    public FixtureDef getFixtureDef()
    {
        PolygonShape cs = new PolygonShape();
        cs.setAsBox(size.x/2, size.y/2);


        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 0.0f;
        fd.friction = 0.0f;
        fd.restitution = 0.0f;
        fd.setSensor(true);
        return fd;
    }
}
