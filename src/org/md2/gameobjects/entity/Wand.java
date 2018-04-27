package org.md2.gameobjects.entity;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.WandItem;

public class Wand extends Entity
{

    private LivingEntity user;
    private WandItem usedItem;
    private int liveTime;
    private Vec2 usingDirection;

    public Wand(LivingEntity user, WandItem usedItem)
    {
        super(usedItem.getTextures().clone());
        renderType = RENDER_TYPE_3D;
        this.user = user;
        this.usedItem = usedItem;
        this.size = new Vec2(usedItem.getWandLength(), usedItem.getWandLength()/2);
        setDeltaY(0.25f*user.getHeight());
        liveTime = 0;
    }

    public void performTick()
    {
        liveTime ++;
        if(!(liveTime < 30))
        {
            this.removeFromWorld();
            usedItem.setCurrentlyInUse(false);
            user.setCurrentlyUsing(null);
            return;
        }
        this.setTransform(user.getPosition().add(usingDirection.mul(0.5F+0.5F*usedItem.getWandLength())), this.getAngle());

    }

    public float getRenderAngle()
    {
        return super.getAngle() + -0.25F*(float)Math.PI;
    }

    public Vec2 getRenderSize(){return super.getRenderSize().mul(usedItem.getWandLength());}

    public void afterDeploySetup()
    {
        Vec2 userPos = user.getPosition();
        Vec2 thisPos = this.body.getPosition();
        Vec2 dif = thisPos.sub(userPos);
        dif.normalize();
        usingDirection = dif;
        this.setTransform(this.body.getPosition(), (float)Math.atan2(dif.y, dif.x));
        this.user.setCurrentlyUsing(dif);
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
