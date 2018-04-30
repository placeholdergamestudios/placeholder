package org.md2.gameobjects.entity;

import org.jbox2d.common.Vec2;
import org.md2.common.Damage;
import org.md2.rendering.Texture;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.WeaponItem;

public class Fireball extends Missile
{


    public Fireball(LivingEntity user, WeaponItem usedItem, Texture[] textures)
    {
        super(user, usedItem, textures);
        size = new Vec2(0.3F, 0.2F);
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
            new Damage(Damage.DAMAGELIGHT, usedItem.getVarOnUse(), user, (LivingEntity)hitObject);
        }
    }

    @Override
    public float getRenderAngle()
    {
        return 0;
    }




}
