package org.md2.gameobjects.entity;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.Damage;
import org.md2.common.Texture;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.weapons.BowItem;
import org.md2.gameobjects.item.weapons.WandItem;

public class Fireball extends WeaponEntity
{


    private WorldObject hitObject;

    public Fireball(LivingEntity user, WandItem usedWand)
    {
        super(user, usedWand);
        this.setTextures(new Texture[]{usedWand.getPart2().getEffectTexture()});
        hitObject = null;
        size = new Vec2(0.6F, 0.4F);
    }



    public void onCollision(WorldObject o)
    {
        if(o.equals(user))
            return;
        if(hitObject != null)
            return;
        if(o.isSensor())
            return;
        hitObject = o;
        this.body.setLinearVelocity(new Vec2());
        if(o instanceof LivingEntity){
            new Damage(Damage.DAMAGETHRUST, usedItem.getVarOnUse(), user, (LivingEntity)hitObject);
        }
    }

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
