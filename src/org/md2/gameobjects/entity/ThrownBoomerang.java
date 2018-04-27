package org.md2.gameobjects.entity;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.WeaponItem;

public class ThrownBoomerang extends WeaponEntity
{

	private int liveTime;
	private int collisionCounter;
	
	public ThrownBoomerang(LivingEntity user, WeaponItem usedItem)
	{
		super(user, usedItem);
		this.size = new Vec2(usedItem.getSize().x , usedItem.getSize().y/2);
		collisionCounter = 0;
		liveTime = 0;
	}
	
	public void performTick()
	{
		liveTime++;
		Vec2 userPos = user.getPosition();
		Vec2 thisPos = this.getPosition();
		Vec2 dif = userPos.sub(thisPos);
		if(collisionCounter > 10){
			dif.normalize();
			dif.mulLocal(usedItem.getWeaponSpeed());
			this.body.setLinearVelocity(dif);
			return;
		}
		this.body.applyForceToCenter(dif);
		this.user.setCurrentlyUsing(dif.negate());
	}
	
	public void afterDeploySetup()
	{
		super.afterDeploySetup();
		this.body.setLinearVelocity(initialDirectionVec2.mul(usedItem.getWeaponSpeed()));
	}

	public float getRenderAngle()
	{
		return super.getRenderAngle()+liveTime/20F*(float)Math.PI;
	}
	
	public void onCollision(WorldObject o)
	{
		if(!o.isSensor())
			collisionCounter++;
		if(collisionCounter > 10)
			this.body.getFixtureList().setSensor(true);
		if(o instanceof LivingEntity){
			LivingEntity le = (LivingEntity)o;
			if(le == user){
				this.removeFromWorld();
				usedItem.setCurrentlyInUse(false);
				user.setCurrentlyUsing(null);
				return;
			}
			le.damage(usedItem.getVarOnThrow());
		}

			
	}
	
	public FixtureDef getFixtureDef()
    {
		PolygonShape cs = new PolygonShape();
		cs.setAsBox(size.x/2, size.y/2);

    	FixtureDef fd = new FixtureDef();
    	fd.shape = cs;
    	fd.density = 0.5f;
    	fd.friction = 0.0f;        
    	fd.restitution = 0.3f;
     	
    	return fd;
    
    }
}
