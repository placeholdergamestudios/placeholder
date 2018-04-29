package org.md2.gameobjects.entity;

import org.jbox2d.common.Vec2;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.WeaponItem;

public abstract class WeaponEntity extends Entity
{
    protected LivingEntity user;
    protected WeaponItem usedItem;
    protected Vec2 initialDirectionVec2;
    protected float initialDirection;
    protected float initialDistance;

    public WeaponEntity(LivingEntity user, WeaponItem usedItem)
    {
        super(usedItem.getTextures().clone());
        this.usedItem = usedItem;
        this.user = user;
        this.renderType = RENDER_TYPE_3D;
        this.setDeltaY(0.25F*user.getHeight());
    }

    public void afterDeploySetup()
    {
        super.afterDeploySetup();
        Vec2 userPos = user.getPosition();
        Vec2 thisPos = this.body.getPosition();
        Vec2 dif = thisPos.sub(userPos);
        initialDistance = dif.normalize();
        initialDirectionVec2 = dif;
        initialDirection = (float)Math.atan2(dif.y, dif.x);
        this.user.setCurrentlyUsing(dif);
    }

    public float getRenderAngle()
    {
        return getAngle()-0.25F*(float)Math.PI;
    }
}
