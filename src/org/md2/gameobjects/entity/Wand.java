package org.md2.gameobjects.entity;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.WeaponItem;
import org.md2.gameobjects.item.weapons.WandItem;

public class Wand extends WeaponEntity
{

    private int liveTime;

    public Wand(LivingEntity user, WeaponItem usedItem)
    {
        super(user, usedItem);
        this.size = new Vec2(usedItem.getWeaponSize(), usedItem.getWeaponSize()/2);
        liveTime = 0;
        shadow.setStatic();
    }

    public void performTick()
    {
        liveTime ++;
        if(!(liveTime < 100/usedItem.getWeaponSpeed()))
        {
            this.removeFromWorld();
            usedItem.setCurrentlyInUse(false);
            user.setCurrentlyUsing(null);
            return;
        }
        this.setTransform(user.getPosition().add(initialDirectionVec2.mul(initialDistance)), this.getAngle());

    }

    public Vec2 getRenderSize(){return super.getRenderSize().mul(usedItem.getWeaponSize());}

    @Override
    public void afterDeploySetup()
    {
        super.afterDeploySetup();
        this.setTransform(this.body.getPosition(), initialDirection);
    }

    public FixtureDef getFixtureDef()
    {
        PolygonShape cs = new PolygonShape();
        cs.setAsBox(0.0f, 0.0f);

        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.setSensor(true);
        return fd;
    }
}
