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
        FixtureDef fd = super.getFixtureDef();
        fd.setSensor(true);
        return fd;
    }
}
