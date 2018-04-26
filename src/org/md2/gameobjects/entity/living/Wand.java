package org.md2.gameobjects.entity.living;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.gameobjects.entity.Entity;
import org.md2.gameobjects.item.WandItem;
import org.md2.main.GraphicRendererV2;

public class Wand extends Entity
{

    private LivingEntity user;
    private WandItem usedItem;
    private int liveTime;

    public Wand(LivingEntity user, WandItem usedItem)
    {
        super(usedItem.getTextures().clone());
        renderType = RENDER_TYPE_3D;
        this.user = user;
        this.usedItem = usedItem;
        this.size = new Vec2(usedItem.getWandLength(), usedItem.getWandLength()/4);
        setDeltaY(0.25f*user.getHeight());
        liveTime = 0;
    }

    public void performTick()
    {
        liveTime ++;
        if(!(liveTime < 1000))
        {
            this.removeFromWorld();
            usedItem.setCurrentlyInUse(false);
            user.setCurrentlyUsing(null);
            return;
        }

    }

    public float getRenderAngle()
    {
        return user.getAngle();
    }

    public Vec2 getRenderSize(){return super.getRenderSize().mul(usedItem.getWandLength());}

    public void afterDeploySetup()
    {
        Vec2 userPos = user.getPosition();
        Vec2 thisPos = this.body.getPosition();
        Vec2 dif = userPos.sub(thisPos);
        dif.normalize();
        this.setTransform(this.body.getPosition(), getRenderAngle());
        this.user.setCurrentlyUsing(dif.negate());
    }

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
